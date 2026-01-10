package com.luminar.service;

import java.util.List;

import com.luminar.entity.Vendor;

public interface VendorService {

    // ===============================
    // ADD / UPDATE VENDOR
    // ===============================
    void saveVendor(Vendor vendor);

    // ===============================
    // GET ALL VENDORS (TABLE)
    // ===============================
    List<Vendor> getAllVendors();

    // ===============================
    // GET VENDOR BY ID
    // ===============================
    Vendor getVendorById(Long vendorId);

    // ===============================
    // DELETE VENDOR
    // ===============================
    void deleteVendor(Long vendorId);

    // ===============================
    // DISTINCT VENDOR TYPES
    // ===============================
    List<String> getVendorTypes();

    // ===============================
    // VENDORS BY TYPE (ASSIGNMENT)
    // ===============================
    List<Vendor> getVendorsByType(String vendorType);
}
