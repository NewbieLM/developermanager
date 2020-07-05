package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Specialty;
import net.maksym.developermanager.repository.SpecialtyRepository;
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
public class SpecialtyServiceImplTest {

    @Autowired
    private SpecialtyServiceImpl specialtyService;

    @MockBean
    SpecialtyRepository specialtyRepository;

    @Test
    public void get() {
        Long id = 1L;
        Specialty expected = new Specialty(id, "SOME_ROLE");
        doReturn(expected).when(specialtyRepository).getOne(id);

        Specialty actual = specialtyService.get(id);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAll() {
        List<Specialty> expected = new ArrayList<>();
        expected.add(new Specialty());

        doReturn(expected).when(specialtyRepository).findAll();

        List<Specialty> actual = specialtyService.getAll();
        Assert.assertThat(expected, is(actual));
    }

    @Test
    public void delete() {
        Long id = 1L;
        specialtyService.delete(id);
        verify(specialtyRepository, times(1)).deleteById(id);

    }

    @Test
    public void saveOrUpdate() {
        Specialty specialty = new Specialty();
        specialtyService.saveOrUpdate(specialty);
        verify(specialtyRepository, times(1)).save(specialty);
    }
}