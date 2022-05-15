package com.example.socialforme.controller;

import com.example.socialforme.model.Dialog;
import com.example.socialforme.model.Message;
import com.example.socialforme.model.User;
import com.example.socialforme.service.DialogService;
import com.example.socialforme.service.MessageService;
import com.example.socialforme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;
    private final DialogService dialogService;
    private final MessageService messageService;

    @Autowired
    public MessageController(SimpMessagingTemplate simpMessagingTemplate, UserService userService, DialogService dialogService, MessageService messageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
        this.dialogService = dialogService;
        this.messageService = messageService;
    }

    @MessageMapping("/chat/{to}")
    public void sendMessage(@DestinationVariable Long to, @Payload Message message) {
        System.out.println("handling send message: " + message + " to: " + to);

        messageService.save(message);
        Dialog dialog = dialogService.getById(message.getDialog());
        dialog.getMessages().add(message);
        dialogService.save(dialog);


        User user = userService.getById(to);
        if (user != null) {
            simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
        }
    }
}
