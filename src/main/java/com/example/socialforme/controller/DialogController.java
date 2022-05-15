package com.example.socialforme.controller;

import com.example.socialforme.model.Dialog;
import com.example.socialforme.model.User;
import com.example.socialforme.service.DialogService;
import com.example.socialforme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dialogs")
public class DialogController {
    private final DialogService dialogService;
    private final UserService userService;

    @Autowired
    public DialogController(DialogService dialogService, UserService userService) {
        this.dialogService = dialogService;
        this.userService = userService;
    }

    @RequestMapping
    public String getDialogs(Principal principal, Model model) {
        User user = userService.getUser(principal.getName());
        List<Dialog> dialogList = user.getDialogList();
        Map<Dialog, List<User>> map = new LinkedHashMap<>();
        dialogList.forEach(dialog -> dialog.getUserList().remove(user));
        dialogList.forEach(dialog -> map.put(dialog, dialog.getUserList()));
        model.addAttribute("map", map);
        return "chat/index";
    }

    @RequestMapping("/{id}")
    public String getChat(@PathVariable("id") Long id, Model model, Principal principal) {
        Dialog dialog = dialogService.getById(id);
        List<User> userList = new ArrayList<>();
        dialog.getUserList().forEach(user -> {
            if (!user.getUsername().equals(principal.getName())) {
                userList.add(user);
            }
        });
        model.addAttribute("messages", dialog.getMessages());
        model.addAttribute("recipient", userList);
        model.addAttribute("recName", userList.get(0).getUsername());
        model.addAttribute("dialog", dialog);
        model.addAttribute("princip", userService.getUser(principal.getName()));
        return "chat/chat";
    }

    @RequestMapping("/createWithUser/{id}")
    public String createDialog(@PathVariable("id") Long id, Principal principal) {
        User channelUser = userService.getUser(principal.getName());
        User recipientUser = userService.getById(id);

        List<Dialog> dialogList = channelUser.getDialogList();
        for (Dialog d : dialogList) {
            List<User> userList = d.getUserList();
            for (User u : userList) {
                if (u.getUsername().equals(recipientUser.getUsername())) {
                    return "redirect:/dialogs/" + d.getId();
                }
            }
        }

        Dialog dialog = new Dialog();
        dialog.setName("dialog");
        dialog.getUserList().add(channelUser);
        dialog.getUserList().add(recipientUser);
        dialogService.save(dialog);

        channelUser.getDialogList().add(dialog);
        recipientUser.getDialogList().add(dialog);
        userService.save(channelUser);
        userService.save(recipientUser);
        return "redirect:/dialogs/" + dialog.getId();
    }
}
