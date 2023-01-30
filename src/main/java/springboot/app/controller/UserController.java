package springboot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springboot.app.model.User;
import springboot.app.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String printUser(ModelMap model, Principal principal) {
            User user = userService.getUserByUsername(principal.getName());
            model.addAttribute("this_user", user);
        return "user";
    }

}
