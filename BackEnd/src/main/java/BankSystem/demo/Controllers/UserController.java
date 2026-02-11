package BankSystem.demo.Controllers;

import BankSystem.demo.BusinessLogic.Services.UserService;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserUpdateRequestDTO;
import BankSystem.demo.Util.UserListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "APIs for user management")
@RestController
@RequestMapping("/user/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @Operation(summary = "Update Password", description = "Update user's password")
    @PostMapping("/updatePassword")
    public ResponseEntity<UserResponseDTO> updatePassword(
            @RequestBody @Valid UserUpdateRequestDTO userUpdateRequestDTO) {
        UserResponseDTO response = userService.updatePassword(userUpdateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @Operation(summary = "Get User by ID", description = "Retrieve user details by user ID")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        UserResponseDTO response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @Operation(summary = "get all users", description = "Retrieve all users in the system")
    @GetMapping("/all")
    public ResponseEntity<UserListResponse> getAllUsers() {
        UserListResponse response = userService.getUsers();
        return ResponseEntity.ok(response);
    }

    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @Operation(summary = "Delete User", description = "Delete a user by user ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @Operation(summary = "Promote User to Admin", description = "Promote a user to admin by user ID")
    @PutMapping("/promote/{userId}")
    public ResponseEntity<UserResponseDTO> promoteToAdmin(@PathVariable Long userId) {
        UserResponseDTO response = userService.promoteToAdmin(userId);
        return ResponseEntity.ok(response);
    }

    // todo: remove the wallets response List from user details
    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @Operation(summary = "Get Current User Details", description = "Retrieve details of the currently authenticated user")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUserDetails() {
        UserResponseDTO response = userService.getUserDetails();
        return ResponseEntity.ok(response);
    }


    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @Operation(summary = "Demote Admin to User", description = "Demote an admin to regular user by user ID")
    @PutMapping("/demote/{userId}")
    public ResponseEntity<UserResponseDTO> demoteFromAdmin(@PathVariable Long userId) {
        UserResponseDTO response = userService.demoteFromAdmin(userId);
        return ResponseEntity.ok(response);
    }
}
