package com.luminar.service;

import com.luminar.entity.User;

public interface UserService {
	
	User login(String username, String password);

    User findByUsername(String username);

    User findById(Long id);

}
