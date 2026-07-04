package com.luminar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.luminar.entity.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    
    List<Vendor> findAllByOrderByVendorTypeAscVendorNameAsc();

  
    List<Vendor> findByVendorType(String vendorType);

   
    @Query("SELECT DISTINCT v.vendorType FROM Vendor v ORDER BY v.vendorType")
    List<String> findDistinctVendorTypes();

    
    boolean existsByVendorTypeAndVendorName(String vendorType, String vendorName);
}
