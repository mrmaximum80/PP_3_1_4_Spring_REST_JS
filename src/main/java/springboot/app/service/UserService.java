package springboot.app.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import springboot.app.model.Role;
import springboot.app.model.User;

import java.util.ArrayList;
import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    User getUser(long id);

    void saveUser(User user);

    void deleteUser(long id);

    List<Role> getAllRoles();

    Role getRole(long id);

    void saveRole(Role role);

    void deleteRole(long id);

    User getUserByUsername(String username);

    User encodeUserPassword(User user);

    User addUserRoles(User user);

    @Transactional
    default void defaultUsers() {
        Role user = new Role("ROLE_USER", "USER", "Пользователь");
        Role admin = new Role("ROLE_ADMIN", "ADMIN", "Администратор");

        saveRole(user);
        saveRole(admin);

        List<Role> userRoles = new ArrayList<>();
        userRoles.add(user);

        List<Role> adminRoles = new ArrayList<>();
        adminRoles.add(user);
        adminRoles.add(admin);

        saveUser(new User("admin", "admin", 35, "admin@mail.ru"
                , "$2y$10$iJm74MemM/FtHG9DbfVEhOg/kHEtU685G5gcKE1CstCJNoXm1mV7e", adminRoles));
        saveUser(new User("user", "user", 30, "user@mail.ru"
                , "$2y$10$ziSaVXp8tXOltf7XE3Q4cO3gApSy4x5TSUz/I/hmiBK8khR5Jxfv2", userRoles));

    }



}
