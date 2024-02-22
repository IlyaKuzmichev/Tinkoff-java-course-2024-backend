package edu.java.controller;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/links")
public class LinksController {
    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllLinks(@RequestHeader(name = "Tg-Chat-Id") Long id) {
        // Getting all links by chat id
        ListLinksResponse links = new ListLinksResponse(List.of(new LinkResponse(id, "aboba.com")), 1);
        return ResponseEntity.ok(links);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(@RequestHeader(name = "Tg-Chat-Id") Long id,
        @RequestBody AddLinkRequest addLinkRequest) {
        // Add link by chat id
        LinkResponse linkResponse = new LinkResponse(id, addLinkRequest.link());
        return ResponseEntity.ok(linkResponse);
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLink(@RequestHeader(name = "Tg-Chat-Id") Long id,
        @RequestBody RemoveLinkRequest removeLinkRequest) {
        // Remove link by chat id
        LinkResponse linkResponse = new LinkResponse(id, removeLinkRequest.link());
        return ResponseEntity.ok(linkResponse);
    }
}
