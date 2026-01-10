package com.luminar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luminar.entity.User;
import com.luminar.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(String username, String password) {

        User user = userRepository.findByUsername(username);

        if (user != null 
                && user.getPassword().equals(password) 
                && "ACTIVE".equals(user.getStatus())) {
            return user;
        }

        return null;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

    
}
