package edu.java.controller;

import edu.java.controller.dto.GetStatusResponse;
import edu.java.controller.dto.SetStatusRequest;
import edu.java.models.User;
import edu.java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-bot")
public class ChatController {
    private final UserService userService;

    @Autowired
    public ChatController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{id}")
    public void registerChat(@PathVariable Long id) {
        userService.addUser(new User(id, null));
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable Long id) {
        userService.removeUser(id);
    }

    @PutMapping("/{id}/status")
    public void setStatus(@PathVariable Long id, @RequestBody SetStatusRequest request) {
        userService.updateUser(new User(id, request.status().getStatus()));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<GetStatusResponse> getStatus(@PathVariable Long id) {
        User user = userService.findUser(id);

        return ResponseEntity.ok(new GetStatusResponse(user.getStatus().toString()));
    }
}
