package net.maksym.developermanager.rest;

import net.maksym.developermanager.model.Role;
import net.maksym.developermanager.service.RoleService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RoleControllerTest {

    @Autowired
    private RoleController roleController;

    @MockBean
    private RoleService roleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAll() throws Exception {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(1L, "role_1"));
        roles.add(new Role(2L, "role_2"));

        Mockito.when(roleService.getAll()).thenReturn(roles);

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);
        mockMvc.perform(get("/roles/moderator/all")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("role_1", "role_2")));
    }

    @Test
    public void getById() throws Exception {
        Long id = 1L;
        Mockito.when(roleService.get(id)).thenReturn(new Role(id, "role_1"));

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);

        mockMvc.perform(get("/roles/moderator/" + id).
                header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("role_1")));
    }

    @Test
    public void delete() throws Exception {
        Long id = 1L;
        Mockito.when(roleService.get(id)).thenReturn(new Role(id, "role_1"));

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);

        mockMvc.perform(MockMvcRequestBuilders.delete("/roles/admin/" + id)
                .header("Authorization", token))
                .andExpect(status().isNoContent());

        Mockito.verify(roleService, Mockito.times(1)).delete(id);
    }

    @Test
    public void save() throws Exception {
        Long id = 1L;
        String name = "role_1";
        Role role = new Role(id, name);

        String jsonRole = "{\"name\": \"" + name + "\" }";

        Mockito.when(roleService.saveOrUpdate(new Role(null, name))).thenReturn(role);

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);
        mockMvc.perform(post("/roles/admin/save")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRole))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.name", is(name)));
    }

    @Test
    public void update() throws Exception {
        Long id = 1L;
        String name = "role_1";
        Role role = new Role(id, name);

        String jsonRole = "{ \"id\": \"1\", \"name\": \"role_1\" }";

        Mockito.when(roleService.saveOrUpdate(role)).thenReturn(role);

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);
        mockMvc.perform(put("/roles/admin/update")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonRole))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.name", is(name)));
    }
}