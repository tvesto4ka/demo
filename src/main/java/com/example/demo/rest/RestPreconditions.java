package com.example.demo.rest;

import com.example.demo.dto.UserDto;
import com.example.demo.entities.User;
import com.example.demo.exceptions.UserNotFoundException;

import java.util.Optional;

public class RestPreconditions {
    public static UserDto checkFound(UserDto user) {
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    public static User checkFound(Optional<User> userOp) {
        if (userOp.isEmpty()) {
            throw new UserNotFoundException();
        }
        return userOp.get();
    }
}
