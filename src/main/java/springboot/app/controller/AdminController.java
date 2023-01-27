package springboot.app.controller;

import org.springframework.security.core.GrantedAuthority;
import springboot.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springboot.app.service.UserService;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String printUsers(ModelMap model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("us", userService.getUserByUsername(principal.getName()));
        model.addAttribute("userUpdate", new User());
        return "users";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute("userUpdate") User user) {
        // Добаляем роли и шифруем пароль
        User cryptedUser = userService.encodeUserPassword(user);
        User userWithRoles = userService.addUserRoles(cryptedUser);
        //--------------------------------------
        userService.saveUser(userWithRoles);
        return "redirect:/admin";
    }

    @PatchMapping ("/update/{id}")
    public String updateUser(@PathVariable("id") long id,
                             @ModelAttribute("userUpdate") User user) {
//         Добаляем роли и шифруем пароль
        User cryptedUser = userService.encodeUserPassword(user);
        User userWithRoles = userService.addUserRoles(cryptedUser);
        //--------------------------------------

        userService.saveUser(userWithRoles);
        return "redirect:/admin";
    }

    @DeleteMapping ("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
