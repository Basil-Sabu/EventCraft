package com.luminar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luminar.entity.VendorAssignment;
import com.luminar.repository.VendorAssignmentRepository;

@Service
public class VendorAssignmentServiceImpl
        implements VendorAssignmentService {

    @Autowired
    private VendorAssignmentRepository assignmentRepository;

    @Override
    public void assignVendor(VendorAssignment assignment) {

        Long eventId = assignment.getEvent().getEventId();
        Long vendorId = assignment.getVendor().getVendorId();

        // ===============================
        // EDIT MODE
        // ===============================
        if (assignment.getAssignmentId() != null) {

            boolean exists =
                assignmentRepository
                    .existsByEventEventIdAndVendorVendorIdAndAssignmentIdNot(
                        eventId,
                        vendorId,
                        assignment.getAssignmentId()
                    );

            if (exists) {
                throw new RuntimeException(
                    "This vendor is already assigned to this event"
                );
            }

            VendorAssignment existing =
                assignmentRepository.findById(
                    assignment.getAssignmentId()
                ).orElseThrow(() ->
                    new RuntimeException("Assignment not found")
                );

            existing.setVendor(assignment.getVendor());
            existing.setAgreedCost(assignment.getAgreedCost());

            assignmentRepository.save(existing);
            return;
        }

        // ===============================
        // ADD MODE
        // ===============================
        boolean exists =
            assignmentRepository
                .existsByEventEventIdAndVendorVendorId(eventId, vendorId);

        if (exists) {
            throw new RuntimeException(
                "This vendor is already assigned to this event"
            );
        }

        assignmentRepository.save(assignment);
    }




    @Override
    public List<VendorAssignment> getAssignmentsByEvent(Long eventId) {
        return assignmentRepository.findByEventEventId(eventId);
    }

    @Override
    public VendorAssignment getAssignmentById(Long assignmentId) {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() ->
                    new RuntimeException("Assignment not found")
                );
    }

    @Override
    public void deleteAssignment(Long assignmentId) {
        assignmentRepository.deleteById(assignmentId);
    }
}
