package net.maksym.developermanager.rest;

import net.maksym.developermanager.messanger.MessageSender;
import net.maksym.developermanager.model.User;
import net.maksym.developermanager.service.UserService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private AuthenticationController authenticationController;

    @MockBean
    private MessageSender messageMessageSender;

    @SpyBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void register() throws Exception {
        Long id = 12345L;
        String username = "some_username";
        String firstName = "some_firstName";
        String lastName = "some_lastName";
        String password = "pass";
        String phoneNumber = "+123456789123";
        String confirmationCode = "abcd";

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setConfirmationCode(confirmationCode);

        String jsonRegisterUser = "{\"username\": \"" + username + "\"," +
                "\"firstName\": \"" + firstName + "\"," +
                "\"lastName\": \"" + lastName + "\"," +
                "\"phoneNumber\": \"" + phoneNumber + "\"," +
                "\"password\": \"" + password + "\"}";

        doReturn(user).when(userService).register(Mockito.any(User.class));
        doNothing().when(messageMessageSender).sendMessage(phoneNumber, confirmationCode);

        this.mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRegisterUser))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(id.intValue())))
                .andExpect(jsonPath("$.username", Matchers.is(username)))
                .andExpect(jsonPath("$.firstName", Matchers.is(firstName)))
                .andExpect(jsonPath("$.lastName", Matchers.is(lastName)))
                .andExpect(jsonPath("$.phoneNumber", Matchers.is(phoneNumber)));


    }

    @Test
    public void login() throws Exception {
        this.mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content("{ \"username\": \"Admin\", \"password\": \"pass\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", Matchers.anything()))
                .andExpect(jsonPath("$.username", Matchers.is("Admin")));
    }

    @Test
    public void confirmRegistration() throws Exception {
        String username = "John";
        String confirmationCode = "12345";

        doReturn(new User()).when(userService).confirmRegistration(username, confirmationCode);

        this.mockMvc.perform(put("/auth/confirm-registration")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("confirmationCode", confirmationCode))
                .andExpect(status().isOk());
    }
}