package net.maksym.developermanager.rest;

import net.maksym.developermanager.model.Developer;
import net.maksym.developermanager.service.DeveloperService;
import net.maksym.developermanager.uitl.AuthorizationUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DeveloperControllerTest {

    @Autowired
    private DeveloperController developerController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeveloperService developerService;

    @Test
    public void getAll() throws Exception {
        Developer dev1 = new Developer();
        dev1.setFirstName("name1");
        dev1.setLastName("last_name1");
        Developer dev2 = new Developer();
        dev2.setFirstName("name2");
        dev2.setLastName("last_name2");

        List<Developer> developers = new ArrayList<>();
        developers.add(dev1);
        developers.add(dev2);
        when(developerService.getAll()).thenReturn(developers);

        String token = "Bearer_" + AuthorizationUtil.getToken(mockMvc);

        this.mockMvc.perform(get("/developers/user/all")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[*].firstName", containsInAnyOrder("name1", "name2")))
                .andExpect(jsonPath("$[*].lastName", containsInAnyOrder("last_name1", "last_name2")));
    }

    @Test
    public void getById() throws Exception {

        Developer dev = getDeveloper();

        when(developerService.get(dev.getId())).thenReturn(dev);

        String token = "Bearer_" + AuthorizationUtil.getToken(mockMvc);

        this.mockMvc.perform(get("/developers/user/" + dev.getId())
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(dev.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(dev.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dev.getLastName())));
    }


    @Test
    public void delete() throws Exception {
        Long id = 1L;
        String token = "Bearer_" + AuthorizationUtil.getToken(mockMvc);

        when(developerService.get(id)).thenReturn(getDeveloper());

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/developers/moderator/" + id)
                .header("Authorization", token))
                .andExpect(status().isNoContent());

        verify(developerService, times(1)).delete(id);
    }

    @Test
    public void save() throws Exception {
        Developer dev = this.getDeveloper();
        dev.setId(null);
        String jsonDeveloper = "{\"firstName\": \"" + dev.getFirstName() + "\", " +
                "\"lastName\": \"" + dev.getLastName() + "\"}";

        Mockito.when(developerService.saveOrUpdate(dev)).thenReturn(this.getDeveloper());

        String token = "Bearer_" + AuthorizationUtil.getToken(mockMvc);
        this.mockMvc.perform(post("/developers/moderator/create")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonDeveloper))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is(dev.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dev.getLastName())));

        verify(developerService, times(1)).saveOrUpdate(dev);
    }

    @Test
    public void update() throws Exception {
        Developer dev = this.getDeveloper();
        String jsonDeveloper = "{\"id\": \"" + dev.getId() + "\", " +
                "\"firstName\": \"" + dev.getFirstName() + "\", " +
                "\"lastName\": \"" + dev.getLastName() + "\"}";

        Mockito.when(developerService.saveOrUpdate(dev)).thenReturn(this.getDeveloper());

        String token = "Bearer_" + AuthorizationUtil.getToken(mockMvc);
        this.mockMvc.perform(put("/developers/moderator/update")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonDeveloper))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(dev.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(dev.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(dev.getLastName())));

        verify(developerService, times(1)).saveOrUpdate(dev);
    }

    private static Developer getDeveloper() {
        Long id = 1L;
        String firstName = "John";
        String lastName = "Smith";

        Developer dev = new Developer();
        dev.setId(id);
        dev.setFirstName(firstName);
        dev.setLastName(lastName);
        return dev;
    }
}