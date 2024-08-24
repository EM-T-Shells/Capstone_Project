package com.revature.planetarium.repository.user;

import com.revature.planetarium.entities.User;
import com.revature.planetarium.exceptions.UserFail;
import com.revature.planetarium.service.user.UserServiceImp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceImpTest {

    private UserServiceImp userService;
    private UserDao userDao;

    @Before
    public void setUp() {
        userDao = Mockito.mock(UserDao.class);
        userService = new UserServiceImp(userDao);
    }

    @Test
    public void createUser_successful() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        when(userDao.findUserByUsername("testuser")).thenReturn(Optional.empty());
        when(userDao.createUser(newUser)).thenReturn(Optional.of(newUser));

        String result = userService.createUser(newUser);

        assertEquals("Created user with username testuser and password password", result);
        verify(userDao).createUser(newUser);
    }

    @Test(expected = UserFail.class)
    public void createUser_withNonUniqueUsername() {
        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setPassword("password");

        when(userDao.findUserByUsername("testuser")).thenReturn(Optional.of(newUser));

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

        when(userDao.findUserByUsername("testuser")).thenReturn(Optional.empty());
        when(userDao.createUser(newUser)).thenReturn(Optional.of(newUser));

        String result = userService.createUser(newUser);

        assertEquals("Created user with username testuser and password 123456789012345678901234567890", result);
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

        when(userDao.findUserByUsername("usernameexactlythirtycharacte")).thenReturn(Optional.empty());
        when(userDao.createUser(newUser)).thenReturn(Optional.of(newUser));

        String result = userService.createUser(newUser);

        assertEquals("Created user with username usernameexactlythirtycharacte and password passwordexactlythirtycharacte", result);
    }

    @Test
    public void authenticate_successful() {
        User credentials = new User();
        credentials.setUsername("testuser");
        credentials.setPassword("password");

        User foundUser = new User();
        foundUser.setUsername("testuser");
        foundUser.setPassword("password");

        when(userDao.findUserByUsername("testuser")).thenReturn(Optional.of(foundUser));

        User result = userService.authenticate(credentials);

        assertEquals("testuser", result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test(expected = UserFail.class)
    public void authenticate_withInvalidPassword() {
        User credentials = new User();
        credentials.setUsername("testuser");
        credentials.setPassword("wrongpassword");

        User foundUser = new User();
        foundUser.setUsername("testuser");
        foundUser.setPassword("password");

        when(userDao.findUserByUsername("testuser")).thenReturn(Optional.of(foundUser));

        userService.authenticate(credentials);
    }

    @Test(expected = UserFail.class)
    public void authenticate_withInvalidUsername() {
        User credentials = new User();
        credentials.setUsername("wrongusername");
        credentials.setPassword("password");

        when(userDao.findUserByUsername("wrongusername")).thenReturn(Optional.empty());

        userService.authenticate(credentials);
    }

    @Test(expected = UserFail.class)
    public void authenticate_withEmptyUsername() {
        User credentials = new User();
        credentials.setUsername("");
        credentials.setPassword("password");

        when(userDao.findUserByUsername("")).thenReturn(Optional.empty());

        userService.authenticate(credentials);
    }

    @Test(expected = UserFail.class)
    public void authenticate_withEmptyPassword() {
        User credentials = new User();
        credentials.setUsername("testuser");
        credentials.setPassword("");

        User foundUser = new User();
        foundUser.setUsername("testuser");
        foundUser.setPassword("password");

        when(userDao.findUserByUsername("testuser")).thenReturn(Optional.of(foundUser));

        userService.authenticate(credentials);
    }
}
