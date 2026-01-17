package com.luminar.service;

import java.util.List;

import com.luminar.entity.Vendor;
import com.luminar.entity.VendorAssignment;

public interface VendorAssignmentService {

    void assignVendor(VendorAssignment assignment);

    List<VendorAssignment> getAssignmentsByEvent(Long eventId);

    VendorAssignment getAssignmentById(Long assignmentId);

    void deleteAssignment(Long assignmentId);
    
    List<VendorAssignment> getVendorsByUserId(Long userId);
}
