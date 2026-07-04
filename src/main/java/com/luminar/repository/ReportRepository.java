package com.luminar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.luminar.entity.Client;

@Repository
public interface ReportRepository extends JpaRepository<Client, Long> {

	@Query("""
			    SELECT
			        c.clientName,
			        COALESCE(SUM(e.packageAmount), 0),
			        COALESCE(SUM(va.agreedCost), 0)
			    FROM Client c
			    LEFT JOIN Event e
			        ON e.client = c
			    LEFT JOIN VendorAssignment va
			        ON va.event = e
			    WHERE c.status = 'ACTIVE'
			    GROUP BY c.clientId, c.clientName
			""")
	List<Object[]> fetchClientProfitData();
}
