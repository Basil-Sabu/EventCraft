package com.luminar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luminar.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    //Fetches a User record based on username.

    boolean existsByUsername(String username);
    //Checks whether a user already exists with the given username.	
}
