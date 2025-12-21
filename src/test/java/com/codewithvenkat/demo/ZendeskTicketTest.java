package com.codewithvenkat.demo;

import com.codewithvenkat.demo.Service.ZendeskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;
import java.util.Map;

@SpringBootTest
public class ZendeskTicketTest {

    @Autowired
    private ZendeskService zendeskService;

    @Test
    public void testCreateTicketFromJson() throws Exception {
        // Read the JSON file
        String jsonFilePath = "src/main/resources/static/ticketpvc.json";
        ObjectMapper mapper = new ObjectMapper();
        
        // Parse JSON
        Map<String, Object> ticketData = mapper.readValue(
            new File(jsonFilePath), 
            Map.class
        );

        // Create the ticket
        System.out.println("\n=== Creating Ticket ===");
        System.out.println("Ticket Data: " + ticketData);
        
        try {
            String response = zendeskService.createTicket(ticketData);
            System.out.println("\n✓ SUCCESS! Ticket Created");
            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.out.println("\n✗ Error creating ticket: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllTickets() {
        System.out.println("\n=== Getting All Tickets ===");
        try {
            String response = zendeskService.getAllTickets();
            System.out.println("✓ SUCCESS! Retrieved Tickets");
            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
