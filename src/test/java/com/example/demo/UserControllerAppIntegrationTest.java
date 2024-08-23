package com.example.demo;

import com.example.demo.dto.UserDto;
import com.example.demo.entities.User;
import com.example.demo.repositories.UsersCrudRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerAppIntegrationTest extends AbstractSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersCrudRepository repo;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate.execute("INSERT INTO users (FIRST_NAME, LAST_NAME) VALUES ('Alex', 'Ivanov')");
    }

    @Test
    @WithMockUser
    public void whenGetAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"id":1,"firstName":"Alex","lastName":"Ivanov","email":null}]
                        """, true));
    }

    @Test
    @WithMockUser
    public void testGetUserById() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"id":1,"firstName":"Alex","lastName":"Ivanov","email":null}
                        """, true));
    }

    @Test
    @WithMockUser
    public void testUpdateUserById() throws Exception {
        UserDto dto = new UserDto(1L, "Tim", "Ivanov", "");
        mockMvc.perform(put("/users/{id}", 1)
                        .content(new ObjectMapper().writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
        if (repo.findById(1L).isPresent()) {
            Assertions.assertEquals("Tim", repo.findById(1L).get().getFirstName());
            Assertions.assertEquals("Ivanov", repo.findById(1L).get().getLastName());
        } else {
            throw new RuntimeException();
        }
    }

    @Test
    @WithMockUser
    public void testCreateUser() throws Exception {
        UserDto dto = new UserDto("Denis", "Tisov");
        mockMvc.perform(post("/users").content(new ObjectMapper().writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated());
        Assertions.assertEquals(2, repo.count());
        Optional<User> user = repo.findByFirstNameAndLastName("Denis", "Tisov");
        if (user.isPresent()) {
            Assertions.assertEquals("Denis", user.get().getFirstName());
            Assertions.assertEquals("Tisov", user.get().getLastName());
        } else {
            throw new RuntimeException();
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUserById() throws Exception {
        mockMvc.perform(delete("/users/1")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(0, repo.count());
    }

    @Test
    @WithAnonymousUser
    public void whenGetAllUsersForAnonymous() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isUnauthorized());
    }
}
