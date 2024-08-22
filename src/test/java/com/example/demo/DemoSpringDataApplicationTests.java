package com.example.demo;

import com.example.demo.dto.UserDto;
import com.example.demo.entities.User;
import com.example.demo.repositories.UsersCrudRepository;
import com.example.demo.services.UserService;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class DemoSpringDataApplicationTests extends AbstractSpringTest {
    @Autowired
    private UsersCrudRepository usersCrudRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testFindByFirstNameAndLastName() {
        Optional<User> usersOptional = usersCrudRepository.findByFirstNameAndLastName("Alex", "Ivanov");
        Assertions.assertTrue(usersOptional.isPresent());
    }

    @Test
    public void testFindByFirstNameStartsWithOrderByFirstNamePage() {
        List<User> list = usersCrudRepository.
                findByFirstNameStartsWith("A");
        list.forEach(u -> System.out.println(u.getFirstName() + " " +u.getLastName()));
        Assertions.assertNotNull(list.get(0));
    }

    @Test
    public void testCreateAndSaveUser() {
        List <User> list = Lists.newArrayList(usersCrudRepository.findAll());
        int listSizeBeforeCreation = list.size();
        UserDto user = userService.createAndSaveUser("Toma", "Tisova", "");
        System.out.println(user.getId() + ", " + user.getFirstName());
        list = Lists.newArrayList(usersCrudRepository.findAll());
        Assertions.assertEquals(listSizeBeforeCreation + 1, list.size());
    }

    @Test
    public void testUpdate() {
        UserDto dto = new UserDto(1L, "Denis", "Ivanov", "");
        userService.update(dto);
        UserDto user = userService.findById(1L);
        Assertions.assertEquals("Denis", user.getFirstName());
    }
}
