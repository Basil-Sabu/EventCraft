package com.luminar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luminar.entity.Client;
import com.luminar.entity.User;
import com.luminar.repository.ClientRepository;
import com.luminar.repository.UserRepository;

@Service
public class ClientServiceImpl implements ClientService{
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
    private UserRepository userRepository;

	
	@Override
	public boolean saveClientWithLogin(Client client, String username, String password) {

	    // 1️⃣ Block duplicate username
	    if (userRepository.findByUsername(username) != null) {
	        throw new RuntimeException("Username already exists");
	    }

	    // 2️⃣ Check business duplicate (NON-BLOCKING)
	    boolean duplicateExists =
	    	    clientRepository.existsByClientNameAndPhoneNumberAndAddress(
	    	        client.getClientName(),
	    	        client.getPhoneNumber(),
	    	        client.getAddress()
	    	    );

	    	if (duplicateExists) {
	    	    throw new RuntimeException(
	    	        "Client with same name, phone number, and address already exists"
	    	 );
	    }

	    // 3️⃣ Create user
	    User user = new User();
	    user.setUsername(username);
	    user.setPassword(password);
	    user.setRole("CLIENT");
	    user.setStatus("ACTIVE");

	    User savedUser = userRepository.save(user);

	    // 4️⃣ Save client
	    client.setUserId(savedUser.getId());
	    client.setStatus("ACTIVE");

	    clientRepository.save(client);

	    return false; 
	}


	@Override
	public List<Client> getAllClients() {
		return clientRepository.findByStatus("ACTIVE");
	}

	@Override
	public Client getClientById(Long id) {
		return clientRepository.findById(id).orElse(null);
		
	}

	@Override
	public void deleteClient(Long id) {
		Client client = clientRepository.findById(id).orElse(null);

	    if (client != null) {
	        client.setStatus("INACTIVE");
	        clientRepository.save(client);
	    }
		
	}

	@Override
	public void updateClientWithLogin(Client client, String username, String password) {

	    // 1️⃣ Update client
	    Client existing = clientRepository
	            .findById(client.getClientId())
	            .orElseThrow();

	    existing.setClientName(client.getClientName());
	    existing.setPhoneNumber(client.getPhoneNumber());
	    existing.setEmail(client.getEmail());
	    existing.setAddress(client.getAddress());
	    
	  

	    clientRepository.save(existing);

	    // 2️⃣ Update user using userId (SAFE)
	    User user = userRepository
	            .findById(existing.getUserId())
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // Block duplicate username (if changed)
	    User existingUser = userRepository.findByUsername(username);
	    if (existingUser != null && !existingUser.getId().equals(user.getId())) {
	        throw new RuntimeException("Username already exists");
	    }

	    user.setUsername(username);

	    // Update password only if entered
	    if (password != null && !password.isBlank()) {
	        user.setPassword(password);
	    }

	    userRepository.save(user);
	}



	

}
