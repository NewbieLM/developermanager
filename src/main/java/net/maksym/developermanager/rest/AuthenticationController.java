package net.maksym.developermanager.rest;

import io.swagger.annotations.ApiOperation;
import net.maksym.developermanager.dto.LoginDto;
import net.maksym.developermanager.dto.UserDto;
import net.maksym.developermanager.messanger.MessageSender;
import net.maksym.developermanager.model.User;
import net.maksym.developermanager.security.jwt.JwtTokenProvider;
import net.maksym.developermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final MessageSender messageMessageSender;

    @Autowired
    public AuthenticationController(UserService userService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, MessageSender messageMessageSender) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.messageMessageSender = messageMessageSender;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "New user registration")
    public ResponseEntity register(@RequestBody @Valid UserDto userDto) {
        if (userDto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userDto.dtoToNewUser();
        user = userService.register(user);
        messageMessageSender.sendMessage(user.getPhoneNumber(), "Your confirmation code: " + user.getConfirmationCode());
        return new ResponseEntity(UserDto.userToDto(user), HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.TEXT_PLAIN_VALUE})
    @ApiOperation(value = "Login to the app")
    public ResponseEntity login(@RequestBody @Valid LoginDto loginDto) {

        String username = loginDto.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginDto.getPassword()));
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        String token = jwtTokenProvider.createToken(username, user.getRoles());

        Map<Object, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("token", token);

        return ResponseEntity.ok(response);

    }

    @PutMapping(value = "/confirm-registration")
    @ApiOperation(value = "Confirmation of registration of a new user")
    public ResponseEntity confirmRegistration(@RequestParam("confirmationCode") String confirmationCode, @RequestParam("username") String username) {

        if (username == null || confirmationCode == null) {
            return new ResponseEntity("Wrong username or confirmation code", HttpStatus.NOT_FOUND);
        }

        User user = userService.confirmRegistration(username, confirmationCode);

        if (user == null) {
            return new ResponseEntity("Wrong username or confirmation code", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
