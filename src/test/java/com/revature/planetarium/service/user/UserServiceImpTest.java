package com.revature.planetarium.service.user;

import com.revature.planetarium.Utility;
import com.revature.planetarium.entities.User;
import com.revature.planetarium.exceptions.UserFail;
import com.revature.planetarium.repository.user.UserDao;
import com.revature.planetarium.utility.DatabaseConnector;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceImpTest {

    private UserServiceImp userService;
    private UserDao mockUserDao;
    private Connection conn;

    @BeforeClass
    public static void setUpTestDb() throws Exception {
        Utility.resetTestDatabase();
    }

    @Before
    public void setUp() throws Exception {
        mockUserDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImp(mockUserDao);

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
    public void testCreateUser_success() {
        User newUser = new User();
        newUser.setUsername("validUsername");
        newUser.setPassword("validPassword");

        when(mockUserDao.findUserByUsername(newUser.getUsername())).thenReturn(Optional.empty());
        when(mockUserDao.createUser(newUser)).thenReturn(Optional.of(newUser));

        String result = userService.createUser(newUser);

        assertEquals("Created user with username validUsername and password validPassword", result);
        verify(mockUserDao).findUserByUsername(newUser.getUsername());
        verify(mockUserDao).createUser(newUser);
    }

    @Test
    public void testCreateUser_usernameExists() {
        User existingUser = new User();
        existingUser.setUsername("validUsername");
        existingUser.setPassword("validPassword");

        when(mockUserDao.findUserByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));

        UserFail exception = assertThrows(UserFail.class, () -> userService.createUser(existingUser));
        assertEquals("Username is already in use", exception.getMessage());

        verify(mockUserDao).findUserByUsername(existingUser.getUsername());
        verify(mockUserDao, never()).createUser(existingUser);
    }

    @Test
    public void testCreateUserWithNonUniqueUsername() {
        User user1 = new User();
        user1.setUsername("testuser");
        user1.setPassword("password");

        when(mockUserDao.findUserByUsername("testuser")).thenReturn(Optional.of(user1));

        UserFail exception = assertThrows(UserFail.class, () -> userService.createUser(user1));
        assertTrue(exception.getMessage().contains("Username is already in use"));

        verify(mockUserDao).findUserByUsername("testuser");
        verify(mockUserDao, never()).createUser(user1);
    }

    @Test
    public void testCreateUserWithUsernameTooLong() {
        User newUser = new User();
        newUser.setUsername("thisusernameiswaytoolongtobevalidandshouldfail");
        newUser.setPassword("password");

        when(mockUserDao.createUser(newUser)).thenThrow(new UserFail("Username is too long"));

        UserFail exception = assertThrows(UserFail.class, () -> userService.createUser(newUser));
        assertTrue(exception.getMessage().contains("Username is too long"));
    }

    @Test
    public void testCreateUserWithEmptyUsername() {
        User newUser = new User();
        newUser.setUsername("");
        newUser.setPassword("password");

        when(mockUserDao.createUser(newUser)).thenThrow(new UserFail("Username cannot be empty"));

        UserFail exception = assertThrows(UserFail.class, () -> userService.createUser(newUser));
        assertTrue(exception.getMessage().contains("Username cannot be empty"));
    }

    @Test
    public void testCreateUserWithEmptyPassword() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("");

        when(mockUserDao.createUser(newUser)).thenThrow(new UserFail("Password cannot be empty"));

        UserFail exception = assertThrows(UserFail.class, () -> userService.createUser(newUser));
        assertTrue(exception.getMessage().contains("Password cannot be empty"));
    }

    @Test
    public void testAuthenticate_success() {
        User credentials = new User();
        credentials.setUsername("validUsername");
        credentials.setPassword("validPassword");

        when(mockUserDao.findUserByUsername(credentials.getUsername())).thenReturn(Optional.of(credentials));

        User result = userService.authenticate(credentials);

        assertEquals(credentials, result);
        verify(mockUserDao).findUserByUsername(credentials.getUsername());
    }

    @Test
    public void testAuthenticate_invalidCredentials() {
        User credentials = new User();
        credentials.setUsername("validUsername");
        credentials.setPassword("validPassword");

        when(mockUserDao.findUserByUsername(credentials.getUsername())).thenReturn(Optional.of(credentials));

        User invalidUser = new User();
        invalidUser.setUsername("validUsername");
        invalidUser.setPassword("invalidPassword");

        UserFail exception = assertThrows(UserFail.class, () -> userService.authenticate(invalidUser));
        assertEquals("Username and/or password do not match", exception.getMessage());

        verify(mockUserDao).findUserByUsername(credentials.getUsername());
    }

    @Test
    public void testAuthenticateWithEmptyUsername() {
        User credentials = new User();
        credentials.setUsername("");
        credentials.setPassword("validPassword");

        when(mockUserDao.findUserByUsername(credentials.getUsername())).thenReturn(Optional.empty());

        UserFail exception = assertThrows(UserFail.class, () -> userService.authenticate(credentials));
        assertTrue(exception.getMessage().contains("Username cannot be empty"));
    }

    @Test
    public void testAuthenticateWithEmptyPassword() {
        User credentials = new User();
        credentials.setUsername("validUsername");
        credentials.setPassword("");

        when(mockUserDao.findUserByUsername(credentials.getUsername())).thenReturn(Optional.of(credentials));

        UserFail exception = assertThrows(UserFail.class, () -> userService.authenticate(credentials));
        assertTrue(exception.getMessage().contains("Password cannot be empty"));
    }
}
