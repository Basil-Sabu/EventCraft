package com.luminar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luminar.entity.Vendor;
import com.luminar.repository.VendorRepository;

@Service
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    // ===============================
    // ADD / UPDATE VENDOR
    // ===============================
    @Override
    public void saveVendor(Vendor vendor) {

        // Duplicate check ONLY for new vendor
        if (vendor.getVendorId() == null) {

            boolean exists =
                vendorRepository.existsByVendorTypeAndVendorName(
                    vendor.getVendorType(),
                    vendor.getVendorName()
                );

            if (exists) {
                throw new RuntimeException(
                    "Vendor already exists with same type and name"
                );
            }
        }

        vendorRepository.save(vendor);
    }

    // ===============================
    // GET ALL VENDORS
    // ===============================
    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepository
                .findAllByOrderByVendorTypeAscVendorNameAsc();
    }

    // ===============================
    // GET VENDOR BY ID
    // ===============================
    @Override
    public Vendor getVendorById(Long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() ->
                    new RuntimeException("Vendor not found")
                );
    }

    // ===============================
    // DELETE VENDOR
    // ===============================
    @Override
    public void deleteVendor(Long vendorId) {
        vendorRepository.deleteById(vendorId);
    }

    // ===============================
    // DISTINCT VENDOR TYPES
    // ===============================
    @Override
    public List<String> getVendorTypes() {
        return vendorRepository.findDistinctVendorTypes();
    }

    // ===============================
    // VENDORS BY TYPE
    // ===============================
    @Override
    public List<Vendor> getVendorsByType(String vendorType) {
        return vendorRepository.findByVendorType(vendorType);
    }
}
