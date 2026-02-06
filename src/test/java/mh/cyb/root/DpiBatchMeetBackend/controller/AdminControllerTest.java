package mh.cyb.root.DpiBatchMeetBackend.controller;

import mh.cyb.root.DpiBatchMeetBackend.domain.ApprovalRequest;
import mh.cyb.root.DpiBatchMeetBackend.service.ApprovalService;
import mh.cyb.root.DpiBatchMeetBackend.service.UserService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @Mock
    private ApprovalService approvalService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPendingApprovals() {
        when(approvalService.getPendingRequests()).thenReturn(Collections.emptyList());
        ResponseEntity<List<ApprovalRequest>> response = adminController.getPendingApprovals();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(approvalService, times(1)).getPendingRequests();
    }
}
