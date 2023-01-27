package springboot.app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import springboot.app.model.Role;
import springboot.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.app.dao.RoleDAO;
import springboot.app.dao.UserDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private UserDAO userDAO;
    private RoleDAO roleDAO;


    @Autowired
    public UserServiceImpl(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @Override
    public List<User> getAllUsers() {
        List users = userDAO.getAllUsers();
        return users;
    }

    @Override
    public User getUser(long id) {
        return userDAO.getUser(id);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        userDAO.deleteUser(id);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDAO.getAllRoles();
    }

    @Override
    public Role getRole(long id) {
        return roleDAO.getRole(id);
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        roleDAO.saveRole(role);
    }

    @Override
    @Transactional
    public void deleteRole(long id) {
        roleDAO.deleteRole(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    @Override
    public User encodeUserPassword(User user) {
//        Если новый пользователь, шифруем пароль сразу
        if (getUserByUsername(user.getUsername()) == null) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            return user;
        }
//        Если пользователь есть в базе, проверяем изменился ли пароль, а потом шифруем
        if (!getUserByUsername(user.getUsername()).getPassword().equals(user.getPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        return user;
    }

    @Override
    public User addUserRoles(User user) {
//        List<String> roles = Arrays.asList(user.getRole().split(","));
        List<String> roles = user.getStrRoles();
        List<Role> roleList = roleDAO.getAllRoles();
        List<Role> userRoles = new ArrayList<>();
        for (Role roleFromList : roleList) {
            for (String role : roles)
                if (roleFromList.getName().equals(role)) {
                    userRoles.add(roleFromList);
                }
        }
        user.setRoles(userRoles);
        return user;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.getUserByUsername(username);
        user.getRoles().get(0);
        if (user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return user;
    }
}

