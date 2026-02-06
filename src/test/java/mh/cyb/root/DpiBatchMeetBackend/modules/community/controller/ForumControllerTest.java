package mh.cyb.root.DpiBatchMeetBackend.modules.community.controller;

import mh.cyb.root.DpiBatchMeetBackend.modules.community.dto.CreatePostRequest;
import mh.cyb.root.DpiBatchMeetBackend.modules.community.dto.ForumPostDto;
import mh.cyb.root.DpiBatchMeetBackend.modules.community.dto.ForumCategoryDto;
import mh.cyb.root.DpiBatchMeetBackend.modules.community.service.ForumService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong; // Added this import
import static org.mockito.Mockito.*;

class ForumControllerTest {

    @Mock
    private ForumService forumService;
    @Mock
    private UserService userService;
    @InjectMocks
    private ForumController forumController;
    @Mock
    private UserDetails userDetails;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
    }

    @Test
    void getAllCategories_ShouldReturnOk() {
        when(forumService.getAllCategories()).thenReturn(Collections.emptyList());
        ResponseEntity<List<ForumCategoryDto>> response = forumController.getAllCategories();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createPost_ShouldReturnCreated() {
        CreatePostRequest request = new CreatePostRequest();
        when(userDetails.getUsername()).thenReturn("user@test.com");
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(forumService.createPost(any(), anyLong())).thenReturn(new ForumPostDto());

        ResponseEntity<ForumPostDto> response = forumController.createPost(request, userDetails);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
