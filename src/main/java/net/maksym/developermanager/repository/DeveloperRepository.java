package net.maksym.developermanager.repository;

import net.maksym.developermanager.model.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
}
