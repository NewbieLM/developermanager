package net.maksym.developermanager.service;

import net.maksym.developermanager.model.User;

import java.util.List;

public interface UserService {

    User get(Long id);

    List<User> getAll();

    void delete(Long id);

    User register(User user);

    User update(User user);

    User findByUsername(String username);

    User confirmRegistration(String username, String confirmationCode);
}
