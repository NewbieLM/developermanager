package net.maksym.developermanager.repository;

import net.maksym.developermanager.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
