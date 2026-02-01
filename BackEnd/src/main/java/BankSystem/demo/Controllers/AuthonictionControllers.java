package BankSystem.demo.Controllers;

import BankSystem.demo.BusinessLogic.Services.UserService;
import BankSystem.demo.DataAccessLayer.DTOs.Auth.AuthenticationRequest;
import BankSystem.demo.DataAccessLayer.DTOs.Auth.AuthenticationResponse;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication Controller", description = "APIs for user authentication and registration")
@RequiredArgsConstructor
public class AuthonictionControllers {

    private final UserService userService;

    // api for register new user
    @Operation(summary = "Register new user", description = " Creates a new user account with the provided registration details")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody @Valid UserRequestDTO request) {
        AuthenticationResponse response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // api to login to the system
    @Operation(summary = "Login", description = "Login to your account with email and password")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody @Valid AuthenticationRequest request) {
        AuthenticationResponse response = userService.loginUser(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
