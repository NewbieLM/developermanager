package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Role;
import net.maksym.developermanager.repository.RoleRepository;
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
public class RoleServiceImplTest {

    @Autowired
    private RoleServiceImpl roleService;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void get() {
        Long id = 1L;
        Role expected = new Role(id, "SOME_ROLE");
        doReturn(expected).when(roleRepository).getOne(id);

        Role actual = roleService.get(id);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAll() {
        List<Role> expected = new ArrayList<>();
        expected.add(new Role());

        doReturn(expected).when(roleRepository).findAll();

        List<Role> actual = roleService.getAll();
        Assert.assertThat(expected, is(actual));
    }

    @Test
    public void delete() {
        Long id = 1L;
        roleService.delete(id);
        verify(roleRepository, times(1)).deleteById(id);
    }

    @Test
    public void saveOrUpdate() {
        Role role = new Role();
        roleService.saveOrUpdate(role);
        verify(roleRepository, times(1)).save(role);
    }
}