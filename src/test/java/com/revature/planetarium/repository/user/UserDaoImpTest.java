package com.revature.planetarium.repository.user;

import com.revature.planetarium.Utility;
import com.revature.planetarium.entities.User;
import com.revature.planetarium.utility.DatabaseConnector;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserDaoImpTest {

    private UserDaoImp userDao;
    private Connection conn;

    @BeforeClass
    public static void setUpTestDb() throws Exception {
        Utility.resetTestDatabase();
    }

    @Before
    public void setUp() throws Exception {
        String url = System.getenv("PLANETARIUM");
        System.out.println("Connecting to database at URL: " + url);
        userDao = new UserDaoImp();
        conn = DatabaseConnector.getConnection();

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM users");
        }
    }

    @After
    public void tearDown() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @Test
    public void createUser() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");
        Optional<User> result = userDao.createUser(newUser);
        assertTrue(result.isPresent());
        assertNotNull(result.get().getId());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    public void findUserByUsername() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");
        userDao.createUser(newUser);
        Optional<User> result = userDao.findUserByUsername("testuser");
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }
}
