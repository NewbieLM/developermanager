package net.maksym.developermanager.repository;

import net.maksym.developermanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByusername(String username);
}
