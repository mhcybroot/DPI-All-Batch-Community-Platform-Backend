package mh.cyb.root.DpiBatchMeetBackend.service;

import mh.cyb.root.DpiBatchMeetBackend.domain.Role;
import mh.cyb.root.DpiBatchMeetBackend.domain.User;
import mh.cyb.root.DpiBatchMeetBackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApprovalService approvalService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        User user = User.builder()
                .email("new@example.com")
                .password("plainPassword")
                .fullName("New User")
                .build();

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        User registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser.getId());
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertFalse(registeredUser.isEnabled()); // Should be disabled by default
        assertTrue(registeredUser.getRoles().contains(Role.MEMBER));

        verify(userRepository, times(1)).save(any(User.class));
        verify(approvalService, times(1)).createRequest(any(User.class));
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("found@example.com");
        when(userRepository.findByEmail("found@example.com")).thenReturn(Optional.of(user));

        Optional<User> found = userService.findByEmail("found@example.com");
        assertTrue(found.isPresent());
        assertEquals("found@example.com", found.get().getEmail());
    }
}
