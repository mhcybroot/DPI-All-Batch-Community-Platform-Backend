package mh.cyb.root.DpiBatchMeetBackend.controller;

import mh.cyb.root.DpiBatchMeetBackend.modules.user.domain.User;
import mh.cyb.root.DpiBatchMeetBackend.security.JwtTokenProvider;
import mh.cyb.root.DpiBatchMeetBackend.modules.user.service.UserService;
import mh.cyb.root.DpiBatchMeetBackend.modules.auth.controller.AuthController;
import mh.cyb.root.DpiBatchMeetBackend.modules.auth.dto.RegisterRequest;
import mh.cyb.root.DpiBatchMeetBackend.modules.auth.dto.LoginRequest;
import mh.cyb.root.DpiBatchMeetBackend.modules.user.dto.UserDto;
import mh.cyb.root.DpiBatchMeetBackend.modules.admin.service.AuditService;
import jakarta.servlet.http.HttpServletRequest;
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

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        RegisterRequest request = new RegisterRequest(
                "test@example.com", "password", "Name");
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");

        when(userService.existsByEmail("test@example.com")).thenReturn(false);
        when(userService.registerUser(any(RegisterRequest.class)))
                .thenReturn(userDto);

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        ResponseEntity<?> response = authController.registerUser(request, httpRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userService, times(1)).registerUser(request);
        verify(auditService, times(1)).logAction(eq(null), eq("USER_REGISTRATION"), anyString(), anyString(),
                anyString());
    }

    @Test
    public void testLoginUser() {
        LoginRequest login = new LoginRequest();
        login.setEmail("test@example.com");
        login.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(any())).thenReturn("dummy-jwt-token");
        when(authentication.getName()).thenReturn("test@example.com");
        when(userService.findByEmail(anyString())).thenReturn(java.util.Optional.of(new User()));

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        ResponseEntity<?> response = authController.loginUser(login, httpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(auditService, times(1)).logAction(any(), eq("USER_LOGIN"), anyString(), anyString(), anyString());
    }
}
