package com.luminar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.luminar.entity.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    // ===============================
    // LIST GUEST GROUPS BY EVENT
    // ===============================
    List<Guest> findByEventEventId(Long eventId);

    // ===============================
    // DUPLICATE CHECK (ADD)
    // ===============================
    boolean existsByEventEventIdAndGroupNameIgnoreCase(
            Long eventId,
            String groupName
    );

    // ===============================
    // DUPLICATE CHECK (UPDATE)
    // ===============================
    boolean existsByEventEventIdAndGroupNameIgnoreCaseAndGroupIdNot(
            Long eventId,
            String groupName,
            Long groupId
    );
    
    //COALESCE is used to replace NULL with a default value.
    //If SUM(paid_amount) is NULL, return 0
    @Query("SELECT COALESCE(SUM(g.guestCount), 0) FROM Guest g WHERE g.event.eventId = :eventId")
    Integer getTotalGuestCountByEvent(Long eventId);

}
