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

@Service
public class ZendeskService {

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
}
