package mh.cyb.root.DpiBatchMeetBackend.controller;

import mh.cyb.root.DpiBatchMeetBackend.modules.user.domain.User;
import mh.cyb.root.DpiBatchMeetBackend.modules.profile.service.ProfileService;
import mh.cyb.root.DpiBatchMeetBackend.modules.user.service.UserService;
import mh.cyb.root.DpiBatchMeetBackend.modules.profile.controller.ProfileController;
import mh.cyb.root.DpiBatchMeetBackend.modules.profile.dto.ProfileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProfileControllerTest {

    @Mock
    private ProfileService profileService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProfileController profileController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMyProfile() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");

        User user = new User();
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        ProfileDto profileDto = new ProfileDto();
        when(profileService.getProfile(user)).thenReturn(profileDto);

        ResponseEntity<ProfileDto> response = profileController
                .getMyProfile(userDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(profileService, times(1)).getProfile(user);
    }
}
