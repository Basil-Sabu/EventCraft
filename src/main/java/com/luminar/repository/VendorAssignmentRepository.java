package com.luminar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luminar.entity.Vendor;
import com.luminar.entity.VendorAssignment;

public interface VendorAssignmentRepository extends JpaRepository<VendorAssignment, Long> {

	List<VendorAssignment> findByEventEventId(Long eventId);

	boolean existsByEventEventIdAndVendorVendorId(Long eventId, Long vendorId);

	boolean existsByEventEventIdAndVendorVendorIdAndAssignmentIdNot(Long eventId, Long vendorId, Long assignmentId);
	
	List<VendorAssignment> findByEventClientUserId(Long userId);
}
