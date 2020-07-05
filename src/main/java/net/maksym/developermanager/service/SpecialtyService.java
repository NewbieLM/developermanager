package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Specialty;

import java.util.List;

public interface SpecialtyService {

    Specialty get(Long id);

    List<Specialty> getAll();

    void delete(Long id);

    Specialty saveOrUpdate(Specialty specialty);

}
