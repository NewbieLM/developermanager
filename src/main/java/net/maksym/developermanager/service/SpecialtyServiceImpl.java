package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Specialty;
import net.maksym.developermanager.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    @Autowired
    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public Specialty get(Long id) {
        return specialtyRepository.getOne(id);
    }

    @Override
    public List<Specialty> getAll() {
        return specialtyRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        specialtyRepository.deleteById(id);
    }

    @Override
    public Specialty saveOrUpdate(Specialty specialty) {
        return specialtyRepository.save(specialty);
    }


}
