package com.betvictor.test.messaging.actionmonitor.service;

import com.betvictor.test.messaging.actionmonitor.dao.UserDao;
import com.betvictor.test.messaging.actionmonitor.model.User;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class UserServiceTest {

    @Mock
    private UserDao dao;
    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(dao);
    }

    @Test
    void findUserByUsername_NotFound() {
        String username = RandomString.make();
        when(dao.findUserByUsername(username)).thenReturn(Optional.empty());
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> service.findUserByUsername(username));
        assertThat(responseStatusException.getStatus(), is(HttpStatus.NOT_FOUND));
        verify(dao, times(1)).findUserByUsername(username);
    }

    @Test
    void findUserByUsername() {
        String username = RandomString.make();
        User user = new User();
        user.setUsername(username);
        when(dao.findUserByUsername(username)).thenReturn(Optional.of(user));

        assertThat(service.findUserByUsername(username), is(user));
        verify(dao, times(1)).findUserByUsername(username);
    }
}