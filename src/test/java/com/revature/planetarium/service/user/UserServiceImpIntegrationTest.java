package com.revature.planetarium.service.user;

import com.revature.planetarium.entities.User;
import com.revature.planetarium.exceptions.UserFail;
import com.revature.planetarium.repository.user.UserDaoImp;
import com.revature.planetarium.utility.DatabaseConnector;
import com.revature.planetarium.Utility;

import org.junit.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserServiceImpIntegrationTest {

    private UserServiceImp userService;
    private UserDaoImp userDao;

    @Before
    public void setUp() {
        Utility.resetTestDatabase();
        userDao = new UserDaoImp();
        userService = new UserServiceImp(userDao);
    }

    @AfterClass
    public static void tearDown() {
        Utility.resetTestDatabase();
    }

// createUser tests

    @Test
    public void createUser_successful() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        String result = userService.createUser(newUser);

        assertEquals("Created user with username testuser and password password", result);

        Optional<User> userFromDb = userDao.findUserByUsername("testuser");
        assertTrue(userFromDb.isPresent());
        assertEquals("testuser", userFromDb.get().getUsername());
    }

    @Test(expected = UserFail.class)
    public void createUser_withNonUniqueUsername() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        userService.createUser(newUser);

        userService.createUser(newUser);
    }

    @Test(expected = UserFail.class)
    public void createUser_withUsernameTooLong() {
        User newUser = new User();
        newUser.setUsername("thisusernameiswaytoolongtobevalidandshouldfail");
        newUser.setPassword("password");

        userService.createUser(newUser);
    }

    @Test(expected = UserFail.class)
    public void createUser_withEmptyUsername() {
        User newUser = new User();
        newUser.setUsername("");
        newUser.setPassword("password");

        userService.createUser(newUser);
    }

    @Test(expected = UserFail.class)
    public void createUser_withEmptyPassword() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("");

        userService.createUser(newUser);
    }

    @Test
    public void createUser_withPasswordExactly30Characters() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("123456789012345678901234567890");

        String result = userService.createUser(newUser);

        assertEquals("Created user with username testuser and password 123456789012345678901234567890", result);

        Optional<User> userFromDb = userDao.findUserByUsername("testuser");
        assertTrue(userFromDb.isPresent());
        assertEquals("123456789012345678901234567890", userFromDb.get().getPassword());
    }

    @Test(expected = UserFail.class)
    public void createUser_withPasswordTooLong() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("thispasswordistoolongandshouldnotbeacceptedbythevalidation");

        userService.createUser(newUser);
    }

    @Test(expected = UserFail.class)
    public void createUser_withUsernameAndPasswordTooLong() {
        User newUser = new User();
        newUser.setUsername("thisusernameistoolongandshouldfail");
        newUser.setPassword("thispasswordistoolongandshouldnotbeaccepted");

        userService.createUser(newUser);
    }

    @Test
    public void createUser_withUsernameAndPasswordExactly30Characters() {
        User newUser = new User();
        newUser.setUsername("usernameexactlythirtycharacte");
        newUser.setPassword("passwordexactlythirtycharacte");

        String result = userService.createUser(newUser);

        assertEquals(
            "Created user with username usernameexactlythirtycharacte and password passwordexactlythirtycharacte",
            result);

        Optional<User> userFromDb = userDao.findUserByUsername("usernameexactlythirtycharacte");
        assertTrue(userFromDb.isPresent());
        assertEquals("usernameexactlythirtycharacte", userFromDb.get().getUsername());
        assertEquals("passwordexactlythirtycharacte", userFromDb.get().getPassword());
    }

// login tests

    @Test
    public void authenticate_successful() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        userService.createUser(newUser);

        User credentials = new User();
        credentials.setUsername("testuser");
        credentials.setPassword("password");

        User result = userService.authenticate(credentials);

        assertEquals("testuser", result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test(expected = UserFail.class)
    public void authenticate_withInvalidPassword() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        userService.createUser(newUser);

        User credentials = new User();
        credentials.setUsername("testuser");
        credentials.setPassword("wrongpassword");

        userService.authenticate(credentials);
    }

    @Test(expected = UserFail.class)
    public void authenticate_withInvalidUsername() {
        User credentials = new User();
        credentials.setUsername("wrongusername");
        credentials.setPassword("password");

        userService.authenticate(credentials);
    }

    @Test(expected = UserFail.class)
    public void authenticate_withEmptyUsername() {
        User credentials = new User();
        credentials.setUsername("");
        credentials.setPassword("password");

        userService.authenticate(credentials);
    }

    @Test(expected = UserFail.class)
    public void authenticate_withEmptyPassword() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        userService.createUser(newUser);

        User credentials = new User();
        credentials.setUsername("testuser");
        credentials.setPassword("");

        userService.authenticate(credentials);
    }
}
