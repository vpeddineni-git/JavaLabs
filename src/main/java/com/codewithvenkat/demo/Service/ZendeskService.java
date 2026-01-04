package com.codewithvenkat.demo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.codewithvenkat.demo.Model.Ticket;
import com.codewithvenkat.demo.Repository.TicketRepository;
import java.time.LocalDateTime;

@Service
public class ZendeskService {

    @Autowired(required = false)
    private TicketRepository ticketRepository;

    @Value("${zendesk.url}")
    private String zendeskUrl;

    @Value("${zendesk.email}")
    private String zendeskEmail;

    @Value("${zendesk.token}")
    private String zendeskToken;

    private HttpHeaders authHeaders() {
        String username = zendeskEmail + "/token";
        String auth = Base64.getEncoder().encodeToString((username + ":" + zendeskToken).getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + auth);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public String getAllTickets() {
        String url = zendeskUrl + "/api/v2/tickets.json";
        RestTemplate rest = new RestTemplate();
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders());
        ResponseEntity<String> resp = rest.exchange(url, HttpMethod.GET, entity, String.class);
        return resp.getBody();
    }

    public String getTicketById(long ticketId) {
        String url = zendeskUrl + "/api/v2/tickets/" + ticketId + ".json";
        RestTemplate rest = new RestTemplate();
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders());
        ResponseEntity<String> resp = rest.exchange(url, HttpMethod.GET, entity, String.class);
        return resp.getBody();
    }

    public String getTicketStatus(long ticketId) {
        String body = getTicketById(ticketId);
        return body; // consumer can parse JSON; kept simple to avoid adding models
    }

    public String createTicket(Map<String, Object> payload) {
        try {
            String url = zendeskUrl + "/api/v2/tickets.json";
            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = authHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper mapper = new ObjectMapper();
            String body;
            if (payload != null && payload.containsKey("tickets")) {
                body = mapper.writeValueAsString(payload);
            } else {
                body = mapper.writeValueAsString(Collections.singletonMap("ticket", payload));
            }

            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> resp = rest.exchange(url, HttpMethod.POST, entity, String.class);
            return resp.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ticket: " + e.getMessage(), e);
        }
    }
     public Object saveTicketUsingRepository(Map<String, Object> payload) {
         if (ticketRepository == null) {
             throw new IllegalStateException("TicketRepository is not available");
         }

         Map<String, Object> ticketMap = payload;
         if (payload.containsKey("ticket") && payload.get("ticket") instanceof Map) {
             ticketMap = (Map<String, Object>) payload.get("ticket");
         }

         String subject = ticketMap.getOrDefault("subject", "").toString();
         String description = null;
         if (ticketMap.containsKey("comment") && ticketMap.get("comment") instanceof Map) {
             Object body = ((Map<?, ?>) ticketMap.get("comment")).get("body");
             if (body != null) description = String.valueOf(body);
         }
         String requesterEmail = null;
         if (ticketMap.containsKey("requester") && ticketMap.get("requester") instanceof Map) {
             Object email = ((Map<?, ?>) ticketMap.get("requester")).get("email");
             if (email != null) requesterEmail = String.valueOf(email);
         }
         if (requesterEmail == null && ticketMap.containsKey("requesterEmail")) {
             requesterEmail = String.valueOf(ticketMap.get("requesterEmail"));
         }

         String status = ticketMap.containsKey("status") ? String.valueOf(ticketMap.get("status")) : "new";

         Ticket ticket = new Ticket(subject, description, requesterEmail, status, LocalDateTime.now());
         return ticketRepository.save(ticket);
     }
}
