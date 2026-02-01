package BankSystem.demo.DataAccessLayer.Repositories;

import BankSystem.demo.DataAccessLayer.Entites.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
    }

    @Test
    void itShouldSaveRole() {
        // given
        Role role = Role.builder()
                .name("ROLE_USER")
                .build();

        // when
        Role savedRole = roleRepository.save(role);

        // then
        assertNotNull(savedRole);
        assertNotNull(savedRole.getId());
        assertEquals("ROLE_USER", savedRole.getName());
    }

    @Test
    void itShouldCheckIfRoleExistsByName() {
        // given
        Role role = Role.builder()
                .name("ROLE_ADMIN")
                .build();
        roleRepository.save(role);

        // when
        Boolean exists = roleRepository.existsByName("ROLE_ADMIN");

        // then
        assertTrue(exists);
    }

    @Test
    void itShouldReturnFalseWhenRoleDoesNotExist() {
        // when
        Boolean exists = roleRepository.existsByName("ROLE_NON_EXISTENT");

        // then
        assertFalse(exists);
    }

    @Test
    void itShouldFindRoleById() {
        // given
        Role role = Role.builder()
                .name("ROLE_MANAGER")
                .build();
        Role savedRole = roleRepository.save(role);

        // when
        Role foundRole = roleRepository.findById(savedRole.getId()).orElse(null);

        // then
        assertNotNull(foundRole);
        assertEquals("ROLE_MANAGER", foundRole.getName());
    }

    @Test
    void itShouldFindAllRoles() {
        // given
        Role role1 = Role.builder().name("ROLE_USER").build();
        Role role2 = Role.builder().name("ROLE_ADMIN").build();
        Role role3 = Role.builder().name("ROLE_MANAGER").build();
        roleRepository.saveAll(List.of(role1, role2, role3));

        // when
        List<Role> roles = roleRepository.findAll();

        // then
        assertEquals(3, roles.size());
    }

    @Test
    void itShouldDeleteRole() {
        // given
        Role role = Role.builder()
                .name("ROLE_TO_DELETE")
                .build();
        Role savedRole = roleRepository.save(role);
        Long roleId = savedRole.getId();

        // when
        roleRepository.deleteById(roleId);

        // then
        assertTrue(roleRepository.findById(roleId).isEmpty());
    }

    @Test
    void itShouldUpdateRoleName() {
        // given
        Role role = Role.builder()
                .name("ROLE_OLD_NAME")
                .build();
        Role savedRole = roleRepository.save(role);

        // when
        savedRole.setName("ROLE_NEW_NAME");
        Role updatedRole = roleRepository.save(savedRole);

        // then
        assertEquals("ROLE_NEW_NAME", updatedRole.getName());
    }
}
