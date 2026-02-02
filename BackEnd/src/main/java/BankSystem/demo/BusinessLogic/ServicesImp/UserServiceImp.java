package BankSystem.demo.BusinessLogic.ServicesImp;

import BankSystem.demo.Aspect.Auditing.AuditLog;
import BankSystem.demo.Aspect.Preformance.PerformanceAspect;
import BankSystem.demo.Aspect.Security.OnlyForSameUser;
import BankSystem.demo.Aspect.Security.RequiresAdmin;
import BankSystem.demo.BusinessLogic.Services.UserService;
import BankSystem.demo.Config.CurrentUserProvider;
import BankSystem.demo.Config.JwtService;
import BankSystem.demo.DataAccessLayer.DTOs.Auth.AuthenticationRequest;
import BankSystem.demo.DataAccessLayer.DTOs.Auth.AuthenticationResponse;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserUpdateRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.Wallet.WalletResponseDTO;
import BankSystem.demo.DataAccessLayer.Entites.*;
import BankSystem.demo.DataAccessLayer.Repositories.RoleRepository;
import BankSystem.demo.DataAccessLayer.Repositories.UserRepositorie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepositorie userRepositorie;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CurrentUserProvider currentUserProvider;
    private final WalletServiceImp walletService;

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
    public List<UserResponseDTO> getUsers() {
        List<User> users = userRepositorie.findAll();
        List<UserResponseDTO> responseDTOS = users.stream()
                .map(this::ConvertUserToResponse)
                .toList();

        return responseDTOS;
    }

    @Override
    @OnlyForSameUser
    public UserResponseDTO getUserById(Long id) {
        User user = userRepositorie.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ConvertUserToResponse(user);
    }

    @Override
    @RequiresAdmin
    @AuditLog
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
    public UserResponseDTO getUserDetails() {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (currentUserId == null) {
            throw new RuntimeException("User not authenticated");
        }
        User user = userRepositorie.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ConvertUserToResponse(user);
    }

    @Override
    @RequiresAdmin
    @AuditLog
    public UserResponseDTO deleteUser(Long id) {
        User user = userRepositorie.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepositorie.delete(user);
        return ConvertUserToResponse(user);
    }

    @Override
    public Boolean isAdmin(Long userId) {
        User user = userRepositorie.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRoles().contains(RoleType.Admin.name())) {
            return true;
        }
        return false;
    }
}
