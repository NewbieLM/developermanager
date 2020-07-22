package net.maksym.developermanager.rest;

import io.swagger.annotations.ApiOperation;
import net.maksym.developermanager.model.Role;
import net.maksym.developermanager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping(value = "/moderator/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "View list of all users roles")
    public ResponseEntity<List<Role>> getAll() {
        List<Role> roles = roleService.getAll();
        if (roles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(roles, HttpStatus.OK);
    }


    @GetMapping(value = "/moderator/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Get role by ID")
    public ResponseEntity<Role> getById(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Role role = roleService.get(id);
        if (role == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(role, HttpStatus.OK);

    }

    @DeleteMapping(value = "/admin/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Delete role by ID (admin only)")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Role role = roleService.get(id);
        if (role == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        roleService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = {"/admin/save"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Save new role (admin only)")
    public ResponseEntity<Role> save(@RequestBody @Valid Role role) {

        if (role == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (role.getId() != null) {
            role.setId(null);
        }

        role = roleService.saveOrUpdate(role);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PutMapping(value = {"/admin/update"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Update role (admin only)")
    public ResponseEntity<Role> update(@RequestBody @Valid Role role) {
        if (role == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        role = roleService.saveOrUpdate(role);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

}
