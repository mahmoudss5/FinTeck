package BankSystem.demo.Config;

import BankSystem.demo.BusinessLogic.Services.UserService;
import BankSystem.demo.DataAccessLayer.Entites.Role;
import BankSystem.demo.DataAccessLayer.Entites.RoleType;
import BankSystem.demo.DataAccessLayer.Repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@AllArgsConstructor
public class DataSeeder {

    private final UserService userService;
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            Arrays.stream(RoleType.values()).forEach(roleType -> {
                if (!roleRepository.existsByName(roleType.name())) {
                    roleRepository.save(Role.builder().name(roleType.name()).build());
                    System.out.println("Seeded Role: " + roleType.name());
                }
            });

        };
    }

}
