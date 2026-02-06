package mh.cyb.root.DpiBatchMeetBackend.modules.event.controller;

import mh.cyb.root.DpiBatchMeetBackend.modules.event.dto.RegisterRequest;
import mh.cyb.root.DpiBatchMeetBackend.modules.event.dto.RegistrationDto;
import mh.cyb.root.DpiBatchMeetBackend.modules.event.dto.RejectRegistrationRequest;
import mh.cyb.root.DpiBatchMeetBackend.modules.event.service.RegistrationService;
import mh.cyb.root.DpiBatchMeetBackend.modules.user.domain.User;
import mh.cyb.root.DpiBatchMeetBackend.modules.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;
    @Mock
    private UserService userService;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private RegistrationController registrationController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
    }

    @Test
    void register_ShouldReturnCreated() {
        RegisterRequest request = new RegisterRequest();
        when(userDetails.getUsername()).thenReturn("user@test.com");
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(registrationService.register(anyLong(), any(), any())).thenReturn(RegistrationDto.builder().build());

        ResponseEntity<RegistrationDto> response = registrationController.register(1L, request, userDetails);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void approve_ShouldReturnOk() {
        when(userDetails.getUsername()).thenReturn("admin@test.com");
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user)); // Using same user object as mock
                                                                                  // admin
        when(registrationService.approveRegistration(anyLong(), any())).thenReturn(RegistrationDto.builder().build());

        ResponseEntity<RegistrationDto> response = registrationController.approveRegistration(1L, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void reject_ShouldReturnOk() {
        RejectRegistrationRequest request = new RejectRegistrationRequest();
        request.setReason("Incomplete");
        when(userDetails.getUsername()).thenReturn("admin@test.com");
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(registrationService.rejectRegistration(anyLong(), any(), anyString()))
                .thenReturn(RegistrationDto.builder().build());

        ResponseEntity<RegistrationDto> response = registrationController.rejectRegistration(1L, request, userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void cancel_ShouldReturnNoContent() {
        when(userDetails.getUsername()).thenReturn("user@test.com");
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        doNothing().when(registrationService).cancelRegistration(anyLong(), any());

        ResponseEntity<Void> response = registrationController.cancelRegistration(1L, userDetails);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
