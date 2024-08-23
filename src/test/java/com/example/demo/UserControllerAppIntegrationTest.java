package com.example.demo;

import com.example.demo.dto.UserDto;
import com.example.demo.repositories.UsersCrudRepository;
import com.example.demo.rest.UserController;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerAppIntegrationTest extends AbstractSpringTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController controller;

    @Autowired
    private UsersCrudRepository repo;

    @Test
//    @WithMockUser
    public void whenGetAllUsers() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [{"id":1,"firstName":"Alex","lastName":"Ivanov","email":null}]
                        """, true));
    }

    @Test
//    @WithMockUser
    public void testGetUserById() throws Exception {
        this.mockMvc.perform(get("/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"id":1,"firstName":"Alex","lastName":"Ivanov","email":null}
                        """, true));
    }

    @Test
//    @WithMockUser
    public void testUpdateUserById() throws Exception {
        UserDto dto = new UserDto("Tim", "Ivanov");
        this.mockMvc.perform(put("/users/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"id":1,"firstName":"Tim","lastName":"Ivanov","email":null}
                        """, true));
    }

    @Test
//    @WithMockUser
    public void testCreateUser() throws Exception {
        UserDto dto = new UserDto("Denis", "Tisov", "");
        this.mockMvc.perform(post("/users/?", dto))
                .andDo(print())
                .andExpect(status().isCreated());
//                .andExpect(content().json("""
//                        {"id":1,"firstName":"Alex","lastName":"Ivanov","email":null}
//                        """, true));
    }

    @Test
//    @WithMockUser
    public void testDeleteUserById() throws Exception {
        long repoSize = repo.count();
        this.mockMvc.perform(delete("/users/1") )
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(repoSize - 1, repo.count());
    }
}
