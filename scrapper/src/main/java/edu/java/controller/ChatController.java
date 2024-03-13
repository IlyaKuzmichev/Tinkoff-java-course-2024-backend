package edu.java.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class ChatController {
    @PostMapping("/{id}")
    public void registerChat(@PathVariable Long id) {
        // registration of new chat in repository
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable Long id) {
        // deletion of the chat from repository
    }
}
