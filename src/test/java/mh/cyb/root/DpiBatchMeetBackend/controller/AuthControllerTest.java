package mh.cyb.root.DpiBatchMeetBackend.controller;

import mh.cyb.root.DpiBatchMeetBackend.domain.User;
import mh.cyb.root.DpiBatchMeetBackend.security.JwtTokenProvider;
import mh.cyb.root.DpiBatchMeetBackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userService.existsByEmail("test@example.com")).thenReturn(false);
        when(userService.registerUser(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = authController.registerUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    public void testLoginUser() {
        User login = new User();
        login.setEmail("test@example.com");
        login.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(any())).thenReturn("dummy-jwt-token");
        when(authentication.getName()).thenReturn("test@example.com");

        ResponseEntity<?> response = authController.loginUser(login);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
