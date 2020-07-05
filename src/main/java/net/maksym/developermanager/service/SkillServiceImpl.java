package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Skill;
import net.maksym.developermanager.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    @Autowired
    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill get(Long id) {
        return skillRepository.getOne(id);
    }

    @Override
    public List<Skill> getAll() {
        return skillRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        skillRepository.deleteById(id);
    }

    @Override
    public Skill saveOrUpdate(Skill skill) {
        return skillRepository.save(skill);
    }

}
