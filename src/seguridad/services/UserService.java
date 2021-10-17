package seguridad.services;

import seguridad.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    List<User> getAllUsersButNoLoggedInUser(String username);
    User getUserById(int id);
    User getUserByUserName(String username);
    User getUserByIdAndUserName(int id, String username);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUserById(int id);
}
