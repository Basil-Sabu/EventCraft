package com.luminar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luminar.entity.Client;

public interface ClientRepository extends JpaRepository<Client,Long>{
	
	List<Client> findByStatus(String status);
	
	boolean existsByClientNameAndPhoneNumberAndAddress(
	        String clientName,
	        String phoneNumber,
	        String address
	);

}
