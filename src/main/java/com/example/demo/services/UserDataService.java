package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.repositories.UsersCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDataService {
    @Autowired
    private UsersCrudRepository usersCrudRepository;

    @Transactional
    public User createAndSaveUser(String firstName, String lastName, String email) {
        User user = new User(firstName, lastName, email);
        return usersCrudRepository.save(user);
    }
}
