package com.luminar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.luminar.entity.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    // ===============================
    // LIST ALL VENDORS (OPTIONAL SORT)
    // ===============================
    List<Vendor> findAllByOrderByVendorTypeAscVendorNameAsc();

    // ===============================
    // FIND VENDORS BY TYPE
    // ===============================
    List<Vendor> findByVendorType(String vendorType);

    // ===============================
    // DISTINCT VENDOR TYPES (FOR DROPDOWN)
    // ===============================
    @Query("SELECT DISTINCT v.vendorType FROM Vendor v ORDER BY v.vendorType")
    List<String> findDistinctVendorTypes();

    // ===============================
    // DUPLICATE CHECK (OPTIONAL BUT GOOD)
    // ===============================
    boolean existsByVendorTypeAndVendorName(String vendorType, String vendorName);
}
