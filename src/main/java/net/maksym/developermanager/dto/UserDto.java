package net.maksym.developermanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.maksym.developermanager.model.User;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;

    public static UserDto userToDto(User user) {
        UserDto result = new UserDto();
        result.setId(user.getId());
        result.setUsername(user.getUsername());
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setPassword(user.getPassword());
        result.setPhoneNumber(user.getPhoneNumber());
        return result;
    }

    public User dtoToUser(User user) {
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setUsername(this.username);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        return user;
    }


    public User dtoToNewUser() {
        User user = new User();
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setUsername(this.username);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        return user;
    }
}
