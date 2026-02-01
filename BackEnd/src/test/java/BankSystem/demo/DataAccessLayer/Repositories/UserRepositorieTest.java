package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositorieTest {


    @Autowired
    private UserRepositorie userRepositorie;



    @AfterEach
    void tearDown() {
        userRepositorie.deleteAll();
    }


    @Test
    void itShouldCheckIfUserNameExists() {
        //given
        User user = User.builder()
                .userName("john_doe")
                .email("john@gamil.com")
                .firstName("John")
                .lastName("Doe")
                .password("hashed_password")
                .build();
        userRepositorie.save(user);
        String userName = "john_doe";

        //when
        Boolean expected = userRepositorie.existsByUserName(userName);

        //then
        assertTrue(expected);
    }

    @Test
    void itShouldFindUserByUserName() {
        //given
        User user = User.builder()
                .userName("john_doe")
                .email("john@gamil.com")
                .firstName("John")
                .lastName("Doe")
                .password("hashed_password")
                .build();
        userRepositorie.save(user);
        String userName = "john_doe";

        //when
        User expected = userRepositorie.findByUserName(userName).orElse(null);

        //then
        assertNotNull(expected);
    }
}