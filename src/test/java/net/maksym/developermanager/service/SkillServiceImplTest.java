package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Skill;
import net.maksym.developermanager.repository.SkillRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SkillServiceImplTest {

    @Autowired
    private SkillServiceImpl skillService;

    @MockBean
    private SkillRepository skillRepository;

    @Test
    public void get() {
        Long id = 1L;
        Skill expected = new Skill(id, "SOME_SKILL");
        doReturn(expected).when(skillRepository).getOne(id);

        Skill actual = skillService.get(id);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAll() {
        List<Skill> expected = new ArrayList<>();
        expected.add(new Skill());

        doReturn(expected).when(skillRepository).findAll();

        List<Skill> actual = skillService.getAll();
        Assert.assertThat(expected, is(actual));
    }

    @Test
    public void delete() {
        Long id = 1L;
        skillService.delete(id);
        verify(skillRepository, times(1)).deleteById(id);
    }

    @Test
    public void saveOrUpdate() {
        Skill skill = new Skill();
        skillService.saveOrUpdate(skill);
        verify(skillRepository, times(1)).save(skill);
    }
}