package net.maksym.developermanager.rest;

import net.maksym.developermanager.model.Specialty;
import net.maksym.developermanager.service.SpecialtyService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpecialtyControllerTest {

    @Autowired
    private SpecialtyController specialtyController;

    @MockBean
    private SpecialtyService specialtyService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAll() throws Exception {
        List<Specialty> specialties = new ArrayList<>();
        specialties.add(new Specialty(1L, "specialty_1"));
        specialties.add(new Specialty(2L, "specialty_2"));

        Mockito.when(specialtyService.getAll()).thenReturn(specialties);

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);
        mockMvc.perform(MockMvcRequestBuilders.get("/specialties/user/all")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("specialty_1", "specialty_2")));
    }

    @Test
    public void getById() throws Exception {
        Long id = 1L;
        Mockito.when(specialtyService.get(id)).thenReturn(new Specialty(id, "specialty_1"));

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);

        mockMvc.perform(get("/specialties/user/" + id).
                header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("specialty_1")));
    }

    @Test
    public void delete() throws Exception {
        Long id = 1L;
        Mockito.when(specialtyService.get(id)).thenReturn(new Specialty(id, "specialty_1"));

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);

        mockMvc.perform(MockMvcRequestBuilders.delete("/specialties/moderator/" + id)
                .header("Authorization", token))
                .andExpect(status().isNoContent());

        Mockito.verify(specialtyService, Mockito.times(1)).delete(id);
    }

    @Test
    public void save() throws Exception{
        Long id = 1L;
        String name = "specialty_1";
        Specialty specialty = new Specialty(id, name);

        String jsonRole = "{\"name\": \"" + name + "\" }";

        Mockito.when(specialtyService.saveOrUpdate(new Specialty(null, name))).thenReturn(specialty);

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);
        mockMvc.perform(post("/specialties/moderator/save")
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
        Specialty specialty = new Specialty(1L, "specialty_1");

        String jsonSpecialty = "{\"id\": \""+ specialty.getId() +"\", \"name\": \"" + specialty.getName() + "\" }";

        Mockito.when(specialtyService.saveOrUpdate(specialty)).thenReturn(specialty);

        String token = "Bearer_" + AuthorizationUtil.getToken(this.mockMvc);
        mockMvc.perform(put("/specialties/moderator/update")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(jsonSpecialty))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(specialty.getId().intValue())))
                .andExpect(jsonPath("$.name", is(specialty.getName())));
    }
}