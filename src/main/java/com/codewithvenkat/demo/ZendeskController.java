package com.codewithvenkat.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codewithvenkat.demo.Service.ZendeskService;
import java.util.Map;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/zendesk")
public class ZendeskController {

    @Autowired
    private ZendeskService zendeskService;

    @GetMapping("/tickets")
    public ResponseEntity<String> getAllTickets() {
        return ResponseEntity.ok(zendeskService.getAllTickets());
    }

    @GetMapping("/ticket/{id}")
    public ResponseEntity<String> getTicketById(@PathVariable long id) {
        return ResponseEntity.ok(zendeskService.getTicketById(id));
    }

    @GetMapping("/ticket/{id}/status")
    public ResponseEntity<String> getTicketStatus(@PathVariable long id) {
        return ResponseEntity.ok(zendeskService.getTicketStatus(id));
    }

    @PostMapping("/ticket")
    public ResponseEntity<String> createTicket(@RequestBody Map<String, Object> payload) {
        String resp = zendeskService.createTicket(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
