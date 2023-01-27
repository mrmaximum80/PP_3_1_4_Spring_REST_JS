package springboot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springboot.app.model.User;
import springboot.app.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
//@RequestMapping(value = "/admin")
public class RESTController {

    private UserService userService;

    @Autowired
    public RESTController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/all_users")
    public List<User> getUsers() {
        List<User> users = userService.getAllUsers();
        return users;
    }

    @PostMapping("/admin/users")
    public ResponseEntity<HttpStatus> addUser (@RequestBody User user) {
//         Добаляем роли и шифруем пароль
        User cryptedUser = userService.encodeUserPassword(user);
        User userWithRoles = userService.addUserRoles(cryptedUser);
        //--------------------------------------
        userService.saveUser(userWithRoles);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @GetMapping("/user/{id}")
//    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
//        User user = userService.getUser(id);
//        return new ResponseEntity<User>(user, HttpStatus.OK);
//    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") long id) {
        User user = userService.getUser(id);
        return user;
    }

    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);

    }

    @PatchMapping ("/admin/users/{id}")
    public ResponseEntity<HttpStatus>  updateUser(@RequestBody User user) {
//         Добаляем роли и шифруем пароль
        User cryptedUser = userService.encodeUserPassword(user);
        User userWithRoles = userService.addUserRoles(cryptedUser);
        //--------------------------------------

        userService.saveUser(userWithRoles);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
