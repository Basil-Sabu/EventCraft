package com.luminar.service;

import java.util.List;

import com.luminar.entity.Event;
import com.luminar.entity.Guest;

public interface GuestService {

	// ADD / UPDATE
	void saveGuest(Guest guest);

	// GET ONE
	Guest getGuestById(Long guestId);

	// LIST BY EVENT
	List<Guest> getGuestsByEvent(Long eventId);

	// DELETE
	void deleteGuest(Long guestId);

	Integer getTotalGuestCountByEvent(Long eventId);

	// ✅ NEW — List events by logged-in user (client)
	List<Guest> getGuestGroupsForClientEvent(Long eventId, Long userId);

}
