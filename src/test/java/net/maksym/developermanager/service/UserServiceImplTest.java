package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Role;
import net.maksym.developermanager.model.User;
import net.maksym.developermanager.repository.RoleRepository;
import net.maksym.developermanager.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;


    @Test
    public void get() {
        User expected = new User();
        expected.setId(1L);
        expected.setFirstName("Ivan");
        expected.setLastName("Ivanov");

        doReturn(expected).when(userRepository).getOne(1L);

        Assert.assertEquals(expected, userService.get(1L));
    }

    @Test
    public void getAll() {
        List<User> expected = new ArrayList<>();
        expected.add(new User());

        doReturn(expected).when(userRepository).findAll();

        List<User> actual = userService.getAll();

        Assert.assertThat(expected, is(actual));
    }

    @Test
    public void delete() {
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void register() {
        Role role = new Role();
        role.setName("ROLE_USER");
        role.setId(3L);


        doReturn(role).when(roleRepository).findByname("ROLE_USER");

        doReturn("Encoded: pass").when(passwordEncoder).encode("pass");

        List<Role> roles = new ArrayList<>();
        roles.add(role);

        String username = "username";
        String firstName = "firstname";
        String lastName = "lastNname";
        String mockConfirmationCode = "12345";
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword("pass");

        User expected = new User();
        expected.setUsername(username);
        expected.setFirstName(firstName);
        expected.setLastName(lastName);
        expected.setRoles(roles);
        expected.setPassword("Encoded: pass");

        doReturn(user).when(userRepository).save(user);

        userService.register(user);

        expected.setConfirmationCode(mockConfirmationCode);
        user.setConfirmationCode(mockConfirmationCode);

        Assert.assertEquals(expected, user);
    }

    @Test
    public void update() {
        User user = new User();
        userService.update(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void findByUsername() {
        String username = "someusername";
        User expected = new User();
        expected.setUsername(username);

        doReturn(expected).when(userRepository).findByusername(username);

        User user = userService.findByUsername(username);

        Assert.assertEquals(expected, user);
    }

    @Test
    public void confirmRegistration() {
        String username = "someusername";
        String confirmationCode = "confirmationCode";

        User user = new User();
        user.setUsername(username);
        user.setConfirmationCode(confirmationCode);

        doReturn(user).when(userRepository).findByusername(username);
        doReturn(user).when(userRepository).save(user);

        User expected = userService.confirmRegistration(username, confirmationCode);

        Assert.assertTrue(expected.isActive());
    }
}