package com.luminar.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luminar.entity.Event;

public interface EventRepository extends JpaRepository<Event,Long>{
	
	 List<Event> findByClientClientId(Long clientId);
	 
	 boolean existsByClientClientIdAndEventNameAndEventDate( //duplicate check for add
		        Long clientId,
		        String eventName,
		        LocalDate eventDate
		);
	 
	 boolean existsByClientClientIdAndEventNameAndEventDateAndEventIdNot( //duplicate check for update
		        Long clientId,
		        String eventName,
		        LocalDate eventDate,
		        Long eventId
		);
	 //for client side view
	 List<Event> findByClientUserId(Long userId);


	 
	 //Spring Data JPA automatically creates this SQL:
	 
	 //SELECT COUNT(*)FROM events WHERE client_id = ? AND event_name = ? AND event_date = ?



}
