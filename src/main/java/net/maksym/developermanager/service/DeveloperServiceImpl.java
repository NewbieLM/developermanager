package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Developer;
import net.maksym.developermanager.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;

    @Autowired
    public DeveloperServiceImpl(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    @Override
    public Developer get(Long id) {
        return developerRepository.getOne(id);
    }

    @Override
    public List<Developer> getAll() {
        return developerRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        developerRepository.deleteById(id);
    }

    @Override
    public Developer saveOrUpdate(Developer developer) {
        return developerRepository.save(developer);
    }

}
