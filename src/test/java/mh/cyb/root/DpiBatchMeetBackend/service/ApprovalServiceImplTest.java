package mh.cyb.root.DpiBatchMeetBackend.service;

import mh.cyb.root.DpiBatchMeetBackend.domain.ApprovalRequest;
import mh.cyb.root.DpiBatchMeetBackend.domain.ApprovalRequest.ApprovalStatus;
import mh.cyb.root.DpiBatchMeetBackend.domain.User;
import mh.cyb.root.DpiBatchMeetBackend.repository.ApprovalRequestRepository;
import mh.cyb.root.DpiBatchMeetBackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ApprovalServiceImplTest {

    @Mock
    private ApprovalRequestRepository approvalRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApprovalServiceImpl approvalService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRequest() {
        User user = new User();
        user.setId(1L);

        when(approvalRequestRepository.save(any(ApprovalRequest.class))).thenAnswer(invocation -> {
            ApprovalRequest req = invocation.getArgument(0);
            req.setId(10L);
            return req;
        });

        ApprovalRequest request = approvalService.createRequest(user);

        assertNotNull(request);
        assertEquals(ApprovalStatus.PENDING, request.getStatus());
        assertEquals(user, request.getUser());
        verify(approvalRequestRepository, times(1)).save(any(ApprovalRequest.class));
    }

    @Test
    public void testApproveRequest() {
        User reviewer = new User();
        reviewer.setId(2L);

        ApprovalRequest request = new ApprovalRequest();
        request.setId(10L);
        request.setStatus(ApprovalStatus.PENDING);
        User user = new User();
        request.setUser(user);

        when(approvalRequestRepository.findById(10L)).thenReturn(Optional.of(request));
        when(approvalRequestRepository.save(any(ApprovalRequest.class))).thenAnswer(i -> i.getArgument(0));

        ApprovalRequest approved = approvalService.approveRequest(10L, reviewer);

        assertEquals(ApprovalStatus.APPROVED, approved.getStatus());
        assertEquals(reviewer, approved.getReviewedBy());
        assertTrue(approved.getUser().isEnabled()); // User should be enabled!
        verify(userRepository, times(1)).save(user); // Verify user update
    }
}
