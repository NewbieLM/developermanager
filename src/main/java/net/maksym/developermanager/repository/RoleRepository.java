package net.maksym.developermanager.repository;

import net.maksym.developermanager.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByname(String roleName);
}

