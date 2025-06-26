package model;

import org.example.sklep_internetowy.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
                "janek123",
                "securePass1",
                "jan@example.com",
                "Jan",
                "Kowalski",
                User.Role.CUSTOMER
        );
    }

    @Test
    void testConstructorInitializesFieldsCorrectly() {
        assertThat(user.getUsername()).isEqualTo("janek123");
        assertThat(user.getPassword()).isEqualTo("securePass1");
        assertThat(user.getEmail()).isEqualTo("jan@example.com");
        assertThat(user.getFirstName()).isEqualTo("Jan");
        assertThat(user.getLastName()).isEqualTo("Kowalski");
        assertThat(user.getRole()).isEqualTo(User.Role.CUSTOMER);
        assertThat(user.isEnabled()).isTrue();
        assertThat(user.getCreatedAt()).isNotNull();
    }

    @Test
    void testSettersAndGetters() {
        user.setUsername("nowyUser");
        user.setPassword("newPassword");
        user.setEmail("nowy@example.com");
        user.setFirstName("Adam");
        user.setLastName("Nowak");
        user.setRole(User.Role.ADMIN);
        user.setEnabled(false);

        assertThat(user.getUsername()).isEqualTo("nowyUser");
        assertThat(user.getPassword()).isEqualTo("newPassword");
        assertThat(user.getEmail()).isEqualTo("nowy@example.com");
        assertThat(user.getFirstName()).isEqualTo("Adam");
        assertThat(user.getLastName()).isEqualTo("Nowak");
        assertThat(user.getRole()).isEqualTo(User.Role.ADMIN);
        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    void testUserDetailsImplementation() {
        Collection<?> authorities = user.getAuthorities();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().toString()).isEqualTo("ROLE_CUSTOMER");

        assertThat(user.isAccountNonExpired()).isTrue();
        assertThat(user.isAccountNonLocked()).isTrue();
        assertThat(user.isCredentialsNonExpired()).isTrue();
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void testCreatedAtIsSetAutomatically() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        User newUser = new User("a", "b123456", "a@a.com", "a", "b", User.Role.ADMIN);
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertThat(newUser.getCreatedAt()).isBetween(before, after);
    }
}

