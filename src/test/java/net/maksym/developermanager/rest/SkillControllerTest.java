package net.maksym.developermanager.rest;

import net.maksym.developermanager.model.Skill;
import net.maksym.developermanager.service.SkillService;
import net.maksym.developermanager.uitl.AuthorizationUtil;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SkillControllerTest {

    @Autowired
    private SkillController skillController;

    @MockBean
    private SkillService skillService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void getAll() throws Exception {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(1L, "Skill_1"));
        skills.add(new Skill(2L, "Skill_2"));

        Mockito.when(skillService.getAll()).thenReturn(skills);

        String token = "Bearer_" + AuthorizationUtil.getToken(mockMvc);

        mockMvc.perform(MockMvcRequestBuilders.get("/skills/user/all")
                .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", Matchers.containsInAnyOrder("Skill_1", "Skill_2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].id", Matchers.containsInAnyOrder(1, 2)));
    }


    @Test
    public void getById() throws Exception {
        Long id = 1L;
        Mockito.when(skillService.get(id)).thenReturn(new Skill(1L, "Skill_1"));

        String token = "Bearer_" + AuthorizationUtil.getToken(mockMvc);
        mockMvc.perform(MockMvcRequestBuilders.get("/skills/user/" + id)
                .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Skill_1")))
                .andExpect((MockMvcResultMatchers.jsonPath("$.id", Matchers.is(id.intValue()))));
    }

    @Test
    public void delete() throws Exception {
        Long id = 1L;
        String token = "Bearer_" + AuthorizationUtil.getToken(mockMvc);

        Mockito.when(skillService.get(id)).thenReturn(new Skill(id, "Some_skill"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/skills/moderator/" + id)
                .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(skillService, Mockito.times(1)).get(id);
    }

    @Test
    public void save() {
    }

    @Test
    public void update() {
    }
}