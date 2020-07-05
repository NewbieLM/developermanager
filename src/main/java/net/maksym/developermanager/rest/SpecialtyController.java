package net.maksym.developermanager.rest;

import net.maksym.developermanager.model.Specialty;
import net.maksym.developermanager.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @Autowired
    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping(value = "/user/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Specialty>> getAll() {
        List<Specialty> specialties = specialtyService.getAll();
        if (specialties.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(specialties, HttpStatus.OK);
    }


    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Specialty> getById(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Specialty specialties = specialtyService.get(id);
        if (specialties == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(specialties, HttpStatus.OK);

    }

    @DeleteMapping(value = "/moderator/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity delete(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Specialty specialty = specialtyService.get(id);
        if (specialty == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        specialtyService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = {"/moderator/save"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Specialty> save(@RequestBody @Valid Specialty specialty) {
        if (specialty == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (specialty.getId() != null) {
            specialty.setId(null);
        }

        specialty = specialtyService.saveOrUpdate(specialty);
        return new ResponseEntity<>(specialty, HttpStatus.OK);
    }

    @PutMapping(value = {"/moderator/update"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Specialty> update(@RequestBody @Valid Specialty specialty) {
        if (specialty == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        specialty = specialtyService.saveOrUpdate(specialty);
        return new ResponseEntity<>(specialty, HttpStatus.OK);
    }

}
