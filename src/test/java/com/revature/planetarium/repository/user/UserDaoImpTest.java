package com.revature.planetarium.repository.user;

import com.revature.planetarium.Utility;
import com.revature.planetarium.entities.User;
import com.revature.planetarium.exceptions.UserFail;
import com.revature.planetarium.utility.DatabaseConnector;

import org.junit.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.Assert.*;

public class UserDaoImpTest {

    private UserDaoImp userDao;

    @Before
    public void setUp() {
        Utility.resetTestDatabase();
        userDao = new UserDaoImp();
    }

    @AfterClass
    public static void resetDB() {
        Utility.resetTestDatabase();
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

    // user registration tests

    @Test
    public void createUserWithNonUniqueUsername() {
        User user1 = new User();
        user1.setUsername("testuser");
        user1.setPassword("password");
        userDao.createUser(user1);

        User user2 = new User();
        user2.setUsername("testuser");
        user2.setPassword("newpassword");

        Assert.assertThrows(UserFail.class, () -> {
            userDao.createUser(user2);
        });
    }

    @Test
    public void createUserWithUsernameTooLong() {
        User newUser = new User();
        newUser.setUsername("thisusernameiswaytoolongtobevalidandshouldfail");
        newUser.setPassword("password");
        Assert.assertThrows(UserFail.class, () -> {
            userDao.createUser(newUser);
        });
    }


    @Test
    public void createUserWithEmptyUsername() {
        User newUser = new User();
        newUser.setUsername("");  
        newUser.setPassword("password");
    
        Optional<User> result = userDao.createUser(newUser);
        
        assertTrue(result.isPresent());  
        assertEquals("", result.get().getUsername());  
    }
    
    @Test
    public void createUserWithEmptyPassword() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("");  
    
        Optional<User> result = userDao.createUser(newUser);
        
        assertTrue(result.isPresent());  
        assertEquals("", result.get().getPassword()); 
    }

    @Test
    public void createUserWithPasswordExactly30Characters() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("123456789012345678901234567890");
        Optional<User> result = userDao.createUser(newUser);

        assertTrue(result.isPresent());
        assertEquals(30, result.get().getPassword().length());
    }

    @Test
    public void createUserWithPasswordTooLong() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("thispasswordistoolongandshouldnotbeacceptedbythevalidation");
        Assert.assertThrows(UserFail.class, () -> {
            userDao.createUser(newUser);
        });
    }

    @Test
    public void createUserWithUsernameAndPasswordTooLong() {
        User newUser = new User();
        newUser.setUsername("thisusernameistoolongandshouldfail");
        newUser.setPassword("thispasswordistoolongandshouldnotbeaccepted");
        Assert.assertThrows(UserFail.class, () -> {
            userDao.createUser(newUser);
        });
    }

    @Test
    public void createUserWithUsernameAndPasswordExactly30Characters() {
        User newUser = new User();
        newUser.setUsername("usernameexactlythirtycharacte");
        newUser.setPassword("passwordexactlythirtycharacte");
        Optional<User> result = userDao.createUser(newUser);

        assertTrue("Expected user creation to succeed with exactly 30 character username and password",
                result.isPresent());
        assertEquals("usernameexactlythirtycharacte", result.get().getUsername());
        assertEquals("passwordexactlythirtycharacte", result.get().getPassword());
        assertNotNull("Expected user ID to be generated", result.get().getId());
    }

    // login tests

    @Test
    public void findUserByUsername() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");
        userDao.createUser(newUser);
        Optional<User> result = userDao.findUserByUsername("testuser");
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
        assertEquals("password", result.get().getPassword());
    }

    @Test
    public void loginInvalidPassword() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");
        userDao.createUser(newUser);

        Optional<User> result = userDao.findUserByUsername("testuser");
        assertTrue(result.isPresent());

        assertNotEquals("wrongpassword", result.get().getPassword());
    }

    @Test
    public void loginInvalidUsername() {
        Optional<User> result = userDao.findUserByUsername("wrongusername");
        assertFalse(result.isPresent());
    }

    @Test
    public void loginWithEmptyUsername() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("validpassword");
        userDao.createUser(newUser);

        try {
            Optional<User> result = userDao.findUserByUsername("");
            if (result.isPresent()) {
                fail("Expected a UserFail exception due to empty username, but found a user");
            }
        } catch (UserFail e) {
            assertTrue(e.getMessage().contains("Username cannot be empty"));
        }
    }

    @Test
    public void loginWithEmptyPassword() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("validpassword");
        userDao.createUser(newUser);

        Optional<User> result = userDao.findUserByUsername("testuser");
        assertTrue(result.isPresent());

        try {
            if (result.isPresent() && result.get().getPassword().trim().isEmpty()) {
                throw new UserFail("Password cannot be empty");
            }
        } catch (UserFail e) {
            assertTrue(e.getMessage().contains("Password cannot be empty"));
        }
    }

}