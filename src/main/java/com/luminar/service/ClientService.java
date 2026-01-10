package com.luminar.service;

import java.util.List;

import com.luminar.entity.Client;

public interface ClientService {
	
	boolean saveClientWithLogin(Client client, String username, String password);
	 
	 void updateClientWithLogin(Client client, String username, String password);
	
	List<Client> getAllClients();
	
	Client getClientById(Long id);
	
	void deleteClient(Long id);
	

	
	


}
