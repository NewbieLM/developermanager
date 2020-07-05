package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Developer;
import net.maksym.developermanager.repository.DeveloperRepository;
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
public class DeveloperServiceImplTest {

    @Autowired
    private DeveloperServiceImpl developerService;

    @MockBean
    private DeveloperRepository developerRepository;

    @Test
    public void get() {
        Long id = 1L;
        Developer expected = new Developer();
        expected.setId(id);
        doReturn(expected).when(developerRepository).getOne(id);

        Developer actual = developerService.get(id);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAll() {
        List<Developer> expected = new ArrayList<>();
        expected.add(new Developer());

        doReturn(expected).when(developerRepository).findAll();

        List<Developer> actual = developerService.getAll();
        Assert.assertThat(expected, is(actual));
    }

    @Test
    public void delete() {
        Long id = 1L;
        developerService.delete(id);
        verify(developerRepository, times(1)).deleteById(id);
    }

    @Test
    public void saveOrUpdate() {
        Developer developer = new Developer();
        developerRepository.save(developer);
        verify(developerRepository, times(1)).save(developer);

    }
}