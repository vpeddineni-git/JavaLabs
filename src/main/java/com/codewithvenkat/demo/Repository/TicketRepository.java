package com.codewithvenkat.demo.Repository;

import com.codewithvenkat.demo.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // JpaRepository provides save(), findById(), findAll(), delete() etc. by default
}
