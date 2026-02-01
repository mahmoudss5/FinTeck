package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.Config.CurrentUserProvider;
import BankSystem.demo.Config.JwtService;
import BankSystem.demo.DataAccessLayer.DTOs.Auth.AuthenticationRequest;
import BankSystem.demo.DataAccessLayer.DTOs.Auth.AuthenticationResponse;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserUpdateRequestDTO;
import BankSystem.demo.DataAccessLayer.Entites.Role;
import BankSystem.demo.DataAccessLayer.Entites.SecurityUser;
import BankSystem.demo.DataAccessLayer.Entites.User;
import BankSystem.demo.DataAccessLayer.Repositories.RoleRepository;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {
    // remember Arrange Act Assert

    @Mock
    private UserRepositorie userRepositorie;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private UserServiceImp userServiceImp;

    private UserRequestDTO userRequestDTO;
    private AuthenticationRequest authenticationRequest;
    private User user;
    private BCryptPasswordEncoder realPasswordEncoder;

    @BeforeEach
    void setUp() {
        realPasswordEncoder = new BCryptPasswordEncoder();

        userRequestDTO = UserRequestDTO.builder()
                .userName("testuser")
                .email("test@gmail.com")
                .firstName("Test")
                .lastName("User")
                .password("password123")
                .build();

        authenticationRequest = AuthenticationRequest.builder()
                .email("test@gmail.com")
                .password("password123")
                .build();

        Role role = Role.builder().id(1L).name("User").build();

        user = User.builder()
                .id(1L)
                .userName("testuser")
                .email("test@gmail.com")
                .firstName("Test")
                .lastName("User")
                .password(realPasswordEncoder.encode("password123"))
                .createdAt(LocalDateTime.now())
                .roles(Set.of(role))
                .build();
    }

    // ==================== REGISTER USER TESTS ====================

    @Test
    public void testRegisterUser_Success() {
        // Arrange
        Role userRole = Role.builder().id(1L).name("User").build();
        when(userRepositorie.existsByEmail(userRequestDTO.getEmail())).thenReturn(false);
        when(userRepositorie.existsByUserName(userRequestDTO.getUserName())).thenReturn(false);
        when(roleRepository.findByName("User")).thenReturn(userRole);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepositorie.save(any(User.class))).thenAnswer(i -> {
            User savedUser = i.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });
        when(jwtService.generateToken(any(SecurityUser.class))).thenReturn("mock-jwt-token");

        // Act
        AuthenticationResponse result = userServiceImp.registerUser(userRequestDTO);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals("mock-jwt-token", result.getToken());
        verify(userRepositorie, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(SecurityUser.class));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists_ThrowsException() {
        // Arrange
        when(userRepositorie.existsByEmail(userRequestDTO.getEmail())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImp.registerUser(userRequestDTO));

        assertEquals("Email is already in use", exception.getMessage());
        verify(userRepositorie, never()).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameAlreadyExists_ThrowsException() {
        // Arrange
        when(userRepositorie.existsByEmail(userRequestDTO.getEmail())).thenReturn(false);
        when(userRepositorie.existsByUserName(userRequestDTO.getUserName())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImp.registerUser(userRequestDTO));

        assertEquals("Username is already taken", exception.getMessage());
        verify(userRepositorie, never()).save(any(User.class));
    }

    // ==================== LOGIN USER TESTS ====================

    @Test
    public void testLoginUser_Success() {
        // Arrange
        when(userRepositorie.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(any(SecurityUser.class))).thenReturn("mock-jwt-token");

        // Act
        AuthenticationResponse result = userServiceImp.loginUser(authenticationRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals("mock-jwt-token", result.getToken());
        verify(userRepositorie, times(1)).findByEmail(authenticationRequest.getEmail());
        verify(jwtService, times(1)).generateToken(any(SecurityUser.class));
    }

    @Test
    public void testLoginUser_EmailNotFound_ThrowsException() {
        // Arrange
        when(userRepositorie.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImp.loginUser(authenticationRequest));

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    public void testLoginUser_WrongPassword_ThrowsException() {
        // Arrange
        AuthenticationRequest wrongPasswordRequest = AuthenticationRequest.builder()
                .email("test@gmail.com")
                .password("wrongpassword")
                .build();

        when(userRepositorie.findByEmail(wrongPasswordRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImp.loginUser(wrongPasswordRequest));

        assertEquals("Invalid email or password", exception.getMessage());
    }

    // ==================== UPDATE USER TESTS ====================

    @Test
    public void testUpdateUser_Success() {
        // Arrange
        UserUpdateRequestDTO updateRequestDTO = UserUpdateRequestDTO.builder()
                .oldPassword("password123")
                .newPassword("newpassword123")
                .build();

        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepositorie.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);
        when(userRepositorie.save(any(User.class))).thenReturn(user);

        // Act
        UserResponseDTO result = userServiceImp.updatePassword(updateRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUserName());
        assertEquals("test@gmail.com", result.getEmail());
        assertEquals("Test", result.getFirstName());
        assertEquals("User", result.getLastName());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getRoles());
        verify(userRepositorie, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser_OldPasswordIncorrect_ThrowsException() {
        // Arrange
        UserUpdateRequestDTO updateRequestDTO = UserUpdateRequestDTO.builder()
                .oldPassword("wrongpassword")
                .newPassword("newpassword123")
                .build();

        when(currentUserProvider.getCurrentUserId()).thenReturn(1L);
        when(userRepositorie.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImp.updatePassword(updateRequestDTO));

        assertEquals("Old password is incorrect", exception.getMessage());
        verify(userRepositorie, never()).save(any(User.class));
    }

    @Test
    public void testUpdateUser_UserNotFound_ThrowsException() {
        // Arrange
        UserUpdateRequestDTO updateRequestDTO = UserUpdateRequestDTO.builder()
                .oldPassword("password123")
                .newPassword("newpassword123")
                .build();

        when(currentUserProvider.getCurrentUserId()).thenReturn(999L);
        when(userRepositorie.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImp.updatePassword(updateRequestDTO));

        assertEquals("User not found", exception.getMessage());
        verify(userRepositorie, never()).save(any(User.class));
    }

    // ==================== GET USERS TESTS ====================

    @Test
    public void testGetUsers_Success_ReturnsUserList() {
        // Arrange
        Role role = Role.builder().id(1L).name("User").build();

        User user1 = User.builder()
                .id(1L)
                .userName("user1")
                .email("user1@gmail.com")
                .firstName("First1")
                .lastName("Last1")
                .password("hash1")
                .createdAt(LocalDateTime.now())
                .roles(Set.of(role))
                .build();

        User user2 = User.builder()
                .id(2L)
                .userName("user2")
                .email("user2@gmail.com")
                .firstName("First2")
                .lastName("Last2")
                .password("hash2")
                .createdAt(LocalDateTime.now())
                .roles(Set.of(role))
                .build();

        when(userRepositorie.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<UserResponseDTO> result = userServiceImp.getUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUserName());
        assertEquals("user2", result.get(1).getUserName());
        verify(userRepositorie, times(1)).findAll();
    }

    @Test
    public void testGetUsers_EmptyList_ReturnsEmptyList() {
        // Arrange
        when(userRepositorie.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<UserResponseDTO> result = userServiceImp.getUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepositorie, times(1)).findAll();
    }

    @Test
    public void testGetUsers_SingleUser_ReturnsSingleUserList() {
        // Arrange
        when(userRepositorie.findAll()).thenReturn(List.of(user));

        // Act
        List<UserResponseDTO> result = userServiceImp.getUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUserName());
        assertEquals("test@gmail.com", result.get(0).getEmail());
        assertNotNull(result.getFirst().getRoles());
        verify(userRepositorie, times(1)).findAll();
    }
}