package BankSystem.demo.BusinessLogic.Services;

import BankSystem.demo.DataAccessLayer.DTOs.Auth.AuthenticationRequest;
import BankSystem.demo.DataAccessLayer.DTOs.Auth.AuthenticationResponse;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserRequestDTO;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserResponseDTO;
import BankSystem.demo.DataAccessLayer.DTOs.User.UserUpdateRequestDTO;

import java.util.List;

public interface UserService {

    AuthenticationResponse registerUser(UserRequestDTO userRequestDTO);

    AuthenticationResponse loginUser(AuthenticationRequest authenticationRequest);

    UserResponseDTO updatePassword(UserUpdateRequestDTO userUpdateRequestDTO);

    UserResponseDTO deleteUser(Long id);

    List<UserResponseDTO> getUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO promoteToAdmin(Long id);
    UserResponseDTO demoteFromAdmin(Long id);
    UserResponseDTO getUserDetails();
    Boolean isAdmin(Long userId);
}
