package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.Aspect.Auditing.AuditLog;
import BankSystem.demo.Aspect.Preformance.PerformanceAspect;
import BankSystem.demo.Aspect.Security.OnlyForSameUser;
import BankSystem.demo.Aspect.Security.RequiresAdmin;
import BankSystem.demo.BusinessLogic.Services.UserService;
import BankSystem.demo.Config.CurrentUserProvider;
import BankSystem.demo.Config.SecurityConfig.JwtService;
import BankSystem.demo.DataAccessLayer.DTOs.Auth.AuthenticationRequest;
import BankSystem.demo.DataAccessLayer.DTOs.Auth.AuthenticationResponse;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserUpdateRequestDTO;
import BankSystem.demo.DataAccessLayer.Entites.*;
import BankSystem.demo.DataAccessLayer.Repositories.RoleRepository;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;
import BankSystem.demo.DataAccessLayer.Repositories.WalletRepository;
import BankSystem.demo.Util.UserListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepositorie userRepositorie;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CurrentUserProvider currentUserProvider;
    private final WalletServiceImp walletService;
    private final WalletRepository walletRepository;

    private User convertRequestToUser(UserRequestDTO dto, Role role) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        return User.builder()
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .roles(roles)
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
    }


    private UserResponseDTO ConvertUserToResponse(User user) {
        

        return UserResponseDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .wallets(user.getWallets()
                        .stream()
                        .map(walletService::convertWalletToResponseDTO)
                        .toList()
                )
                .roles(user.getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet()))
                .build();
    }

    @Override
    @PerformanceAspect
    @CacheEvict(value = "usersAll_v4", allEntries = true)
    public AuthenticationResponse registerUser(UserRequestDTO userRequestDTO) {
        if (userRepositorie.existsByEmail(userRequestDTO.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        if (userRepositorie.existsByUserName(userRequestDTO.getUserName())) {
            throw new RuntimeException("Username is already taken");
        }

        Role userRole = roleRepository.findByName(RoleType.User.name());
        if (userRole == null) {
            userRole = roleRepository.save(Role.builder().name(RoleType.User.name()).build());
        }

        User user = convertRequestToUser(userRequestDTO, userRole);
        userRepositorie.save(user);

        SecurityUser securityUser = new SecurityUser(user);
        String jwtToken = jwtService.generateToken(securityUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userName(user.getUserName())
                .build();
    }

    @Override
    public AuthenticationResponse loginUser(AuthenticationRequest authenticationRequest) {
        // Retrieve user by email
        User user = userRepositorie.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        SecurityUser securityUser = new SecurityUser(user);
        String jwtToken = jwtService.generateToken(securityUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userName(user.getUserName())
                .build();
    }

    @Override
    @AuditLog
    @OnlyForSameUser
    @CacheEvict(
            cacheNames = {"usersById", "usersAll_v4", "usersByEmail", "isAdmin"},
            allEntries = true
    )
    public UserResponseDTO updatePassword(UserUpdateRequestDTO userUpdateRequestDTO) {
        Long currentUserId = currentUserProvider.getCurrentUserId();

        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }

        User user = userRepositorie.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(userUpdateRequestDTO.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(userUpdateRequestDTO.getNewPassword()));

        userRepositorie.save(user);
        UserResponseDTO Response = ConvertUserToResponse(user);

        return Response;
    }

    @Override
    @PerformanceAspect
    @RequiresAdmin
    @Cacheable(cacheNames = "usersAll_v4")
    public UserListResponse getUsers() {
        List<User> users = userRepositorie.findAll();
        List<UserResponseDTO> userResponses = users.stream()
                .map(this::ConvertUserToResponse)
                .toList();

        return new UserListResponse(userResponses);
    }

    @Override
    @OnlyForSameUser
    @Cacheable(cacheNames = "usersById", key = "#id")
    public UserResponseDTO getUserById(Long id) {
        User user = userRepositorie.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ConvertUserToResponse(user);
    }

    @Override
    @RequiresAdmin
    @AuditLog
    @CacheEvict(
            cacheNames = {"usersById", "usersAll_v4", "usersByEmail", "isAdmin"},
            allEntries = true
    )
    public UserResponseDTO promoteToAdmin(Long id) {
        User user = userRepositorie.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role adminRole = roleRepository.findByName(RoleType.Admin.name());
        if (adminRole == null) {
            adminRole = roleRepository.save(Role.builder().name(RoleType.Admin.name()).build());
        }

        user.getRoles().add(adminRole);
        userRepositorie.save(user);
        return ConvertUserToResponse(user);
    }

    @Override
    @AuditLog
    @CacheEvict(
            cacheNames = {"usersById", "usersAll_v4", "usersByEmail", "isAdmin"},
            allEntries = true
    )
    public UserResponseDTO demoteFromAdmin(Long id) {
        User user = userRepositorie.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!needOwner(user)) {
            throw new RuntimeException("Only Owner can demote Admin users");
        }

        Role adminRole = roleRepository.findByName(RoleType.Admin.name());
        if (adminRole != null) {
            user.getRoles().remove(adminRole);
        }

        userRepositorie.save(user);
        return ConvertUserToResponse(user);
    }

    @Override
    public UserResponseDTO getUserDetails() {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }
        String userEmail= userRepositorie.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getEmail();
       User user=userRepositorie.findByEmailWithWallets(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        return ConvertUserToResponse(user);
    }


    private Boolean needOwner(User user) {
        Long currentUserId= currentUserProvider.getCurrentUserId();
        User currentUser = userRepositorie.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        if (user.getRoles().contains(RoleType.Admin) && !currentUser.getRoles().contains(RoleType.Owner.name()) ) {
            return false;
        }
        return true;
    }


    @Override
    @RequiresAdmin
    @AuditLog
    @CacheEvict(
            cacheNames = {"usersById", "usersAll_v4", "usersByEmail", "isAdmin"},
            allEntries = true
    )
    public UserResponseDTO deleteUser(Long id) {
        User user = userRepositorie.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRoles().contains(RoleType.Admin) && !needOwner(user)) {
            throw new RuntimeException("Only Owner can delete Admin users");
        }
        userRepositorie.delete(user);
        return ConvertUserToResponse(user);
    }

    @Override
    @Cacheable(cacheNames = "isAdmin", key = "#userId")
    public Boolean isAdmin(Long userId) {
        User user = userRepositorie.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRoles().contains(RoleType.Admin)) {
            return true;
        }
        return false;
    }

    @Override
    @CacheEvict(
            cacheNames = {"usersById", "usersAll_v4", "usersByEmail", "isAdmin"},
            allEntries = true
    )
    public void save(User newUser) {
        userRepositorie.save(newUser);
    }

    @Override
    @Cacheable(cacheNames = "usersByEmail", key = "#finalEmail")
    public Optional<User> findByEmail(String finalEmail) {
        User user = userRepositorie.findByEmail(finalEmail).orElse(null);
        if (user != null) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
