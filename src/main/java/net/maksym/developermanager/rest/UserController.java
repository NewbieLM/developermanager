package net.maksym.developermanager.rest;

import net.maksym.developermanager.dto.UserDto;
import net.maksym.developermanager.model.User;
import net.maksym.developermanager.security.jwt.JwtUser;
import net.maksym.developermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/moderator/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/moderator/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.get(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(value = "/admin/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity delete(@PathVariable("id") Long id) {
        User user = userService.get(id);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        userService.delete(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @PutMapping(value = "/admin/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> updateProfile(@RequestBody @Valid User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        user = userService.update(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PutMapping(value = "/user/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserDto userDto) {
        if (userDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        GrantedAuthority adminAuthority = jwtUser.getAuthorities().stream()
                .filter(a -> ((GrantedAuthority) a).getAuthority().equals("ROLE_ADMIN"))
                .findAny().orElse(null);

        if (userDto.getId() != jwtUser.getId() && adminAuthority == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = userService.get(userDto.getId());
        user = userDto.dtoToUser(user);
        userService.update(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


}
