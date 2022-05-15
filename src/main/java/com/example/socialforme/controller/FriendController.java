package com.example.socialforme.controller;

import com.example.socialforme.model.User;
import com.example.socialforme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class FriendController {
    private final UserService userService;

    @Autowired
    public FriendController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping
    public String getNewFriends(Model model, Principal principal) {
        List<User> userList = userService.getAll();
        userList.removeIf(user -> user.getUsername().equals(principal.getName()));
        model.addAttribute("users", userList);
        return "index";
    }

    @RequestMapping("/profile/{id}")
    public String getProfileOtherUser(Model model, @PathVariable("id") Long id, Principal principal) {
        User user = userService.getById(id);
        User currentUser = userService.getUser(principal.getName());
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSub", isSub(currentUser, user));
        model.addAttribute("user", user);
        model.addAttribute("flag", false);
        return "profile/profile";
    }

    public boolean isSub(User currentUser, User user) {
        for (User u : user.getSubscribers()) {
            if (u.getUsername().equals(currentUser.getUsername()))
                return true;
        }
        return false;
    }
}
