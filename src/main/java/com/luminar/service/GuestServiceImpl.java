package com.luminar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luminar.entity.Guest;
import com.luminar.repository.GuestRepository;

@Service
public class GuestServiceImpl implements GuestService {

	@Autowired
	private GuestRepository guestRepository;

	// ===============================
	// ADD / UPDATE WITH DUPLICATE CHECK
	// ===============================
	@Override
	public void saveGuest(Guest guest) {

		Long eventId = guest.getEvent().getEventId();

		// ADD case
		if (guest.getGroupId() == null) {

			boolean exists = guestRepository.existsByEventEventIdAndGroupNameIgnoreCase(eventId, guest.getGroupName());

			if (exists) {
				throw new RuntimeException("Guest group with the same name already exists for this event");
			}
		}
		// UPDATE case
		else {

			boolean exists = guestRepository.existsByEventEventIdAndGroupNameIgnoreCaseAndGroupIdNot(eventId,
					guest.getGroupName(), guest.getGroupId());

			if (exists) {
				throw new RuntimeException("Another guest group with the same name already exists for this event");
			}
		}

		guestRepository.save(guest);
	}

	// ===============================
	// GET SINGLE GUEST GROUP
	// ===============================
	@Override
	public Guest getGuestById(Long guestId) {
		return guestRepository.findById(guestId).orElseThrow(() -> new RuntimeException("Guest group not found"));
	}

	// ===============================
	// LIST GUEST GROUPS BY EVENT
	// ===============================
	@Override
	public List<Guest> getGuestsByEvent(Long eventId) {
		return guestRepository.findByEventEventId(eventId);
	}

	// ===============================
	// DELETE
	// ===============================
	@Override
	public void deleteGuest(Long guestId) {
		guestRepository.deleteById(guestId);
	}

	@Override
	public Integer getTotalGuestCountByEvent(Long eventId) {
		return guestRepository.getTotalGuestCountByEvent(eventId);
	}

	@Override
	public List<Guest> getGuestGroupsForClientEvent(Long eventId, Long userId) {

		return guestRepository.findByEventEventIdAndEventClientUserId(eventId, userId);
	}

}
