package com.example.demo;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@Import(TestContainersConfiguration.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractSpringTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='USERS'");
        jdbcTemplate.execute("INSERT INTO users (FIRST_NAME, LAST_NAME) VALUES ('Alex', 'Ivanov')");
    }
}
