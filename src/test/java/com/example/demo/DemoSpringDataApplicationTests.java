package com.example.demo;

import com.example.demo.entities.User;
import com.example.demo.repositories.UsersCrudRepository;
import com.example.demo.services.UserDataService;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class DemoSpringDataApplicationTests extends AbstractSpringTest {
    @Autowired
    private UsersCrudRepository usersCrudRepository;

    @Autowired
    private UserDataService userDataService;

    @Test
    public void testFindByFirstNameAndLastName() {
        Optional<User> usersOptional = usersCrudRepository.findByFirstNameAndLastName("Alex", "Ivanov");
    }

    @Test
    public void testFindByFirstNameStartsWithOrderByFirstNamePage() {
        List<User> list = usersCrudRepository.
                findByFirstNameStartsWith("A", PageRequest.of(1,3, Sort.by("firstName")));
        list.forEach(u -> System.out.println(u.getFirstName() + " " +u.getLastName()));
    }

    @Test
    public void testCreateAndSaveUser() {
        List <User> list = Lists.newArrayList(usersCrudRepository.findAll());
        System.out.println(list.size());
        Assertions.assertEquals(0,list.size());
        User user = userDataService.createAndSaveUser("Toma", "Tisova", "");
        System.out.println(user.getId() + ", " + user.getFirstName());
        list = Lists.newArrayList(usersCrudRepository.findAll());
        Assertions.assertEquals(1,list.size());
    }

}
