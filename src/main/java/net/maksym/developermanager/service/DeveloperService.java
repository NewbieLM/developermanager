package net.maksym.developermanager.service;

import net.maksym.developermanager.model.Developer;

import java.util.List;

public interface DeveloperService {

    Developer get(Long id);

    List<Developer> getAll();

    void delete(Long id);

    Developer saveOrUpdate(Developer developer);

}
