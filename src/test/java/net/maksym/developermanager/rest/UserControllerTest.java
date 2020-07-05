package net.maksym.developermanager.rest;

import net.maksym.developermanager.model.User;
import net.maksym.developermanager.service.UserService;
import net.maksym.developermanager.uitl.AuthorizationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private UserController userController;

    @SpyBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAll() throws Exception {
        User user1 = new User();
        user1.setUsername("username_1");
        user1.setFirstName("firstName_1");
        user1.setLastName("lastName_1");

        User user2 = new User();
        user2.setUsername("username_2");
        user2.setFirstName("firstName_2");
        user2.setLastName("lastName_2");

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        when(userService.getAll()).thenReturn(users);

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/moderator/all")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[*].username", containsInAnyOrder("username_1", "username_2")))
                .andExpect(jsonPath("$[*].firstName", containsInAnyOrder("firstName_1", "firstName_2")))
                .andExpect(jsonPath("$[*].lastName", containsInAnyOrder("lastName_1", "lastName_2")));
    }

    @Test
    public void getById() throws Exception {
        Long id = 1L;
        User user = this.getUser();
        user.setId(id);

        when(userService.get(id)).thenReturn(user);

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/moderator/" + id)
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())));
    }

    @Test
    public void delete() throws Exception {
        Long id = 1L;
        when(userService.get(id)).thenReturn(this.getUser());
        doNothing().when(userService).delete(id);

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/users/admin/" + id)
                .header("Authorization", token))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(id);
    }

    @Test
    public void updateProfile() throws Exception {
        Long id = 3L;
        User user = this.getUser();
        user.setId(id);

        String jsonUser = "{\"id\": " + user.getId() + ", " +
                "\"username\": \"" + user.getUsername() + "\"," +
                "\"firstName\": \"" + user.getFirstName() + "\"," +
                "\"lastName\": \"" + user.getLastName() + "\"," +
                "\"password\": \"" + user.getPassword() + "\"}";

        doReturn(user).when(userService).update(Mockito.any(User.class));

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);
        mockMvc.perform(put("/users/admin/update")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.password", is(user.getPassword())));
    }

    @Test
    public void updateUser() throws Exception {
        User user = this.getUser();
        user.setId(3L);

        String jsonUser = "{\"id\": " + user.getId() + ", " +
                "\"username\": \"" + user.getUsername() + "\"," +
                "\"firstName\": \"" + user.getFirstName() + "\"," +
                "\"lastName\": \"" + user.getLastName() + "\"}";

        doReturn(user).when(userService).get(user.getId());
        doReturn(user).when(userService).update(Mockito.any(User.class));

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);
        mockMvc.perform(put("/users/user/update")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())));
    }


    private User getUser() {
        User user = new User();
        user.setId(12345L);
        user.setUsername("some_username");
        user.setFirstName("some_firstName");
        user.setLastName("some_lastName");
        user.setPassword("pass");
        user.setPhoneNumber("+123456789123");
        return user;
    }
}