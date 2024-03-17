package edu.java.controller;

import edu.java.controller.dto.AddLinkRequest;
import edu.java.controller.dto.LinkResponse;
import edu.java.controller.dto.ListLinksResponse;
import edu.java.controller.dto.RemoveLinkRequest;
import edu.java.models.Link;
import edu.java.service.jdbc.JdbcLinkService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final JdbcLinkService linkService;

    @Autowired
    public LinksController(JdbcLinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllLinks(@RequestHeader(name = "Tg-Chat-Id") Long id) {
        List<LinkResponse> links = linkService
            .findAllLinksForUser(id)
            .stream()
            .map((link) -> new LinkResponse(link.getId(), link.getUrl()))
            .toList();
        return ResponseEntity.ok(new ListLinksResponse(links, links.size()));
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(@RequestHeader(name = "Tg-Chat-Id") Long chatId,
        @RequestBody AddLinkRequest addLinkRequest) {
        Link link = linkService.addLink(chatId, addLinkRequest.link());
        return ResponseEntity.ok(new LinkResponse(link.getId(), link.getUrl()));
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLink(@RequestHeader(name = "Tg-Chat-Id") Long id,
        @RequestBody RemoveLinkRequest removeLinkRequest) {
        Link link = linkService.removeLinkByURL(id, removeLinkRequest.link());
        return ResponseEntity.ok(new LinkResponse(link.getId(), link.getUrl()));
    }
}
