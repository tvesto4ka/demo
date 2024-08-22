package com.example.demo.services;

import com.example.demo.dto.UserDto;
import com.example.demo.entities.User;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repositories.UsersCrudRepository;
import com.example.demo.rest.RestPreconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UsersCrudRepository usersCrudRepository;

    private UserDto toDto(User user) {
            return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    private User toEntity(UserDto dto) {
        return new User(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getEmail());
    }

    @Transactional
    public UserDto createAndSaveUser(String firstName, String lastName, String email) {
        User user = new User(firstName, lastName, email);
        return toDto(usersCrudRepository.save(user));
    }

    public List<UserDto> findAll() {
        List<UserDto> list = new ArrayList<>();
        for(User user : usersCrudRepository.findAll()) {
            list.add(toDto(user));
        }
        return list;
    }

    public UserDto findById(Long id) {
        if (usersCrudRepository.findById(id).isPresent()) {
            return toDto(usersCrudRepository.findById(id).get());
        } else
            throw new UserNotFoundException();
    }

    public void deleteById(Long id) {
        usersCrudRepository.deleteById(id);
    }

    public Long create(UserDto user) {
        return usersCrudRepository.save(toEntity(user)).getId();
    }

    public void update(UserDto dto) {
        Optional<User> userOp = usersCrudRepository.findById(dto.getId());
        User user = RestPreconditions.checkFound(userOp);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        usersCrudRepository.save(user);
    }
}
