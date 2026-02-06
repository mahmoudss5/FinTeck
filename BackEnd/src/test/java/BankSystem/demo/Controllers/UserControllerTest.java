package BankSystem.demo.Controllers;

import BankSystem.demo.BusinessLogic.Services.UserService;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserUpdateRequestDTO;
import BankSystem.demo.Util.UserListResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import BankSystem.demo.DataAccessLayer.DTOs.User.UserResponseDTO;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean // not just mock to avoid context load issues
        private UserService userService;

        private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        public UserRequestDTO getUserRequestTest() {
                UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                                .userName("john_doe")
                                .email("johan@gmail.com")
                                .firstName("John")
                                .lastName("Doe")
                                .password(passwordEncoder.encode("hashed_password"))
                                .build();
                return userRequestDTO;
        }

        @Test
        void itShouldReturnUserDetails() throws Exception {
                // Arrange
                UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                                .id(1L)
                                .userName("john_doe")
                                .email("johan@gmail.com")
                                .firstName("John")
                                .lastName("Doe")
                                .createdAt(java.time.LocalDateTime.now())
                                .roles(java.util.Set.of("User"))
                                .build();

                given(userService.getUserById(1L))
                                .willReturn(userResponseDTO);

                // Act & Assert
                mockMvc.perform(get("/user/api/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.userName").value("john_doe"))
                                .andExpect(jsonPath("$.email").value("johan@gmail.com"))
                                .andExpect(jsonPath("$.firstName").value("John"))
                                .andExpect(jsonPath("$.lastName").value("Doe"));
        }

        @Test
        void itShouldReturnBadRequest() throws Exception {
                // Arrange - newPassword must be at least 8 characters
                UserUpdateRequestDTO updateRequestDTO = UserUpdateRequestDTO.builder()
                                .oldPassword("oldpass")
                                .newPassword("short")
                                .build();

                // Act & Assert
                mockMvc.perform(post("/user/api/updatePassword")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateRequestDTO)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void itShouldReturtnAllUsers() throws Exception {
                // Arrange
                UserListResponse users = new UserListResponse(java.util.List.of(
                                UserResponseDTO.builder()
                                                .id(1L)
                                                .userName("john_doe")
                                                .email("johtn@gmail.com")
                                                .firstName("John")
                                                .lastName("Doe")
                                                .createdAt(java.time.LocalDateTime.now())
                                                .roles(java.util.Set.of("User"))
                                                .build(),
                                UserResponseDTO.builder()
                                                .id(2L)
                                                .userName("jame_doe")
                                                .email("jame@gmail.com")
                                                .firstName("Jame")
                                                .lastName("Doe")
                                                .createdAt(java.time.LocalDateTime.now())
                                                .roles(java.util.Set.of("User"))
                                                .build()));
                given(userService.getUsers()).willReturn(users);

                // Act & Assert
                mockMvc.perform(get("/user/api/all"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.users.length()").value(2))
                                .andExpect(jsonPath("$.users[0].id").value(1L))
                                .andExpect(jsonPath("$.users[1].id").value(2L));
        }

        @Test
        void itShouldUpdatePasswordSuccessfully() throws Exception {
                // Arrange
                UserUpdateRequestDTO updateRequestDTO = UserUpdateRequestDTO.builder()
                                .oldPassword("oldpassword")
                                .newPassword("newpassword123")
                                .build();
                UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                                .id(1L)
                                .userName("john_doe")
                                .email("johan@gmail.com")
                                .firstName("John")
                                .lastName("Doe")
                                .createdAt(java.time.LocalDateTime.now())
                                .roles(java.util.Set.of("User"))
                                .build();

                given(userService.updatePassword(org.mockito.ArgumentMatchers.any(UserUpdateRequestDTO.class)))
                                .willReturn(userResponseDTO);

                // Act & Assert
                mockMvc.perform(post("/user/api/updatePassword")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateRequestDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.userName").value("john_doe"))
                                .andExpect(jsonPath("$.email").value("johan@gmail.com"));
        }

        @Test
        void itShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
                // Arrange
                given(userService.getUserById(999L))
                                .willThrow(new RuntimeException("User not found"));

                // Act & Assert
                mockMvc.perform(get("/user/api/999"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("User not found"))
                                .andExpect(jsonPath("$.status").value("failed"));
        }

        @Test
        void itShouldDeleteUserSuccessfully() throws Exception {
                // Arrange
                UserResponseDTO deletedUser = UserResponseDTO.builder()
                                .id(1L)
                                .userName("john_doe")
                                .email("johan@gmail.com")
                                .firstName("John")
                                .lastName("Doe")
                                .createdAt(java.time.LocalDateTime.now())
                                .roles(java.util.Set.of("User"))
                                .build();

                given(userService.deleteUser(1L)).willReturn(deletedUser);

                // Act & Assert
                mockMvc.perform(delete("/user/api/1"))
                                .andExpect(status().isOk());

                // Verify the service method was called
                org.mockito.Mockito.verify(userService, org.mockito.Mockito.times(1)).deleteUser(1L);
        }

        @Test
        void itShouldReturnErrorWhenDeletingNonExistentUser() throws Exception {
                // Arrange
                given(userService.deleteUser(999L))
                                .willThrow(new RuntimeException("User not found"));

                // Act & Assert
                mockMvc.perform(delete("/user/api/999"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("User not found"))
                                .andExpect(jsonPath("$.status").value("failed"));
        }

        @Test
        void itShouldReturnEmptyListWhenNoUsersExist() throws Exception {
                // Arrange
                given(userService.getUsers()).willReturn(new UserListResponse(java.util.List.of()));

                // Act & Assert
                mockMvc.perform(get("/user/api/all"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.users.length()").value(0));
        }

        @Test
        void itShouldReturnErrorWhenUpdatePasswordForNonExistentUser() throws Exception {
                // Arrange
                UserUpdateRequestDTO updateRequestDTO = UserUpdateRequestDTO.builder()
                                .oldPassword("oldpassword")
                                .newPassword("newpassword123")
                                .build();
                given(userService.updatePassword(org.mockito.ArgumentMatchers.any(UserUpdateRequestDTO.class)))
                                .willThrow(new RuntimeException("User not found"));

                // Act & Assert
                mockMvc.perform(post("/user/api/updatePassword")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updateRequestDTO)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("User not found"))
                                .andExpect(jsonPath("$.status").value("failed"));
        }

}