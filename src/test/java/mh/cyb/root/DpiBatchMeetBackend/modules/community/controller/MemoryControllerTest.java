package mh.cyb.root.DpiBatchMeetBackend.modules.community.controller;

import mh.cyb.root.DpiBatchMeetBackend.modules.community.dto.MemoryDto;
import mh.cyb.root.DpiBatchMeetBackend.modules.community.service.MemoryService;
import mh.cyb.root.DpiBatchMeetBackend.modules.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MemoryControllerTest {

    @Mock
    private MemoryService memoryService;
    @Mock
    private UserService userService;
    @InjectMocks
    private MemoryController memoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMemories_ShouldReturnOk() {
        when(memoryService.getAllMemories()).thenReturn(Collections.emptyList());
        ResponseEntity<List<MemoryDto>> response = memoryController.getAllMemories();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
