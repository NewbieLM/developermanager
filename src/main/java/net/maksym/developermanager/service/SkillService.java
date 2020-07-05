package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Skill;

import java.util.List;

public interface SkillService {

    Skill get(Long id);

    List<Skill> getAll();

    void delete(Long id);

    Skill saveOrUpdate(Skill skill);
}
