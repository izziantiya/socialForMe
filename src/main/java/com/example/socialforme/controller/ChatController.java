package com.example.socialforme.controller;

import com.example.socialforme.model.Dialog;
import com.example.socialforme.service.DialogService;
import com.example.socialforme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ChatController {
    private final UserService userService;
    private final DialogService dialogService;

    @Autowired
    public ChatController(UserService userService, DialogService dialogService) {
        this.userService = userService;
        this.dialogService = dialogService;
    }

    @GetMapping("/getSender")
    private String getSender(Principal principal) {
        return userService.getUser(principal.getName()).getId().toString();
    }

    @GetMapping("/getNameSender/{id}")
    private String getNameSender(@PathVariable("id") Long id) {
        return userService.getById(id).getUsername();
    }

}
