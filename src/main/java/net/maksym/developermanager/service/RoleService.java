package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Role;
import net.maksym.developermanager.model.Skill;

import java.util.List;

public interface RoleService {

    Role get(Long id);

    List<Role> getAll();

    void delete(Long id);

    Role saveOrUpdate(Role role);

}
