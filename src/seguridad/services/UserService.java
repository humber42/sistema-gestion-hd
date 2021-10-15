package seguridad.services;

import seguridad.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(int id);
    User getUserByUserName(String username);
    int saveUser(User user);
    int updateUser(User user);
    void deleteUserById(int id);
}
