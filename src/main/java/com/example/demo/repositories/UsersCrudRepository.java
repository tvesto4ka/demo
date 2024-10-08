package com.example.demo.repositories;

import com.example.demo.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersCrudRepository extends CrudRepository<User, Long> {

    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);

    List<User> findByFirstNameStartsWith(String a);
}
