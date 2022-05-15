package com.example.socialforme.controller;

import com.example.socialforme.model.User;
import com.example.socialforme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class SubscriptionController {
    private final UserService userService;

    @Autowired
    public SubscriptionController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/subscribe/{id}")
    public String subscribe(@PathVariable Long id, Principal principal) {
        User user = userService.getById(id);
        User currentUser = userService.getUser(principal.getName());
        userService.subscribe(currentUser, user);
        return "redirect:/profile/" + id;
    }

    @GetMapping("/unsubscribe/{id}")
    public String unsubscribe(@PathVariable Long id, Principal principal) {
        User user = userService.getById(id);
        User currentUser = userService.getUser(principal.getName());
        userService.unsubscribe(currentUser, user);
        return "redirect:/profile/" + id;
    }

    @GetMapping("/{type}/{id}/list")
    public String userList(Model model, @PathVariable("id") Long id, @PathVariable("type") String type) {
        User user = userService.getById(id);
        model.addAttribute("type", type);
        model.addAttribute("userChannel", user);

        if ("subscriptions".equals(type)) {
            model.addAttribute("users", user.getSubscriptions());
        }
        else model.addAttribute("users", user.getSubscribers());
        return "profile/subscription";
    }

}
