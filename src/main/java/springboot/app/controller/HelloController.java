package springboot.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import springboot.app.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {

    private UserService userService;

    @Autowired
    public HelloController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String printWelcome(ModelMap model) {
        List<String> messages = new ArrayList<>();
        messages.add("Hello! People!");
        messages.add("I'm Spring Bootstrap application!");
        model.addAttribute("messages", messages);
        return "index";
    }

    @GetMapping("/default-users")
    public String defaultUsers() {
        userService.defaultUsers();
        return "redirect:/login";
    }

}