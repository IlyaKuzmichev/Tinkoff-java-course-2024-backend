package edu.java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-bot")
public class ChatController {
    @PostMapping("/{id}")
    public ResponseEntity<Void> registerChat(@PathVariable Long id) {
        // registration of new chat in repository
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        // deletion of the chat from repository
        return ResponseEntity.ok().build();
    }
}
