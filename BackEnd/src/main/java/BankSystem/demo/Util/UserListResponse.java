package BankSystem.demo.Util;

import BankSystem.demo.DataAccessLayer.DTOs.User.UserResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse implements Serializable {
    private List<UserResponseDTO> users;

    @JsonIgnore
    public int size() {
        return users != null ? users.size() : 0;
    }

    @JsonIgnore
    public  UserResponseDTO get(int index){
        if (users != null && index >= 0 && index < users.size()) {
            return users.get(index);
        }
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
    }

    @JsonIgnore
    public boolean isEmpty() {
        return users == null || users.isEmpty();
    }
}