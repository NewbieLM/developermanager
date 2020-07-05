package net.maksym.developermanager.rest;

import net.maksym.developermanager.model.Developer;
import net.maksym.developermanager.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private final DeveloperService developerService;

    @Autowired
    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @GetMapping(value = "/user/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Developer>> getAll() {
        List<Developer> developers = developerService.getAll();
        if (developers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(developers, HttpStatus.OK);
    }


    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Developer> getById(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Developer developer = developerService.get(id);
        if (developer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(developer, HttpStatus.OK);
    }

    @DeleteMapping(value = "/moderator/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Developer developer = developerService.get(id);
        if (developer == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        developerService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @PostMapping(value = {"/moderator/create"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Developer> save(@RequestBody @Valid Developer developer) {
        if (developer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (developer.getId() != null) {
            developer.setId(null);
        }

        developer = developerService.saveOrUpdate(developer);
        return new ResponseEntity<>(developer, HttpStatus.OK);
    }

    @PutMapping(value = {"/moderator/update"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Developer> update(@RequestBody @Valid Developer developer) {
        if (developer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        developer = developerService.saveOrUpdate(developer);
        return new ResponseEntity<>(developer, HttpStatus.OK);
    }


}
