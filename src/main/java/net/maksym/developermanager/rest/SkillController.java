package net.maksym.developermanager.rest;

import io.swagger.annotations.ApiOperation;
import net.maksym.developermanager.model.Skill;
import net.maksym.developermanager.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillController {

    private final SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping(value = "/user/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "View list of all developers skills")
    public ResponseEntity<List<Skill>> getAll() {
        List<Skill> skills = skillService.getAll();
        if (skills.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(skills, HttpStatus.OK);
    }


    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Get skill by ID")
    public ResponseEntity<Skill> getById(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Skill skill = skillService.get(id);
        if (skill == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(skill, HttpStatus.OK);

    }

    @DeleteMapping(value = "/moderator/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Delete skill by ID")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Skill skill = skillService.get(id);
        if (skill == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        skillService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = {"/moderator/save"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Save new skill")
    public ResponseEntity<Skill> save(@RequestBody @Valid Skill skill) {
        if (skill == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (skill.getId() != null) {
            skill.setId(null);
        }

        skill = skillService.saveOrUpdate(skill);
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @PutMapping(value = {"/moderator/update"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Update skill")
    public ResponseEntity<Skill> update(@RequestBody @Valid Skill skill) {
        if (skill == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        skill = skillService.saveOrUpdate(skill);
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

}
