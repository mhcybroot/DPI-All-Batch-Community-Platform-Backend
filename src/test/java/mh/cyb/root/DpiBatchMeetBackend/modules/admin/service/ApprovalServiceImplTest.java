package mh.cyb.root.DpiBatchMeetBackend.modules.admin.service;

import mh.cyb.root.DpiBatchMeetBackend.modules.admin.domain.ApprovalRequest;
import mh.cyb.root.DpiBatchMeetBackend.modules.admin.domain.ApprovalRequest.ApprovalStatus;
import mh.cyb.root.DpiBatchMeetBackend.modules.user.domain.User;
import mh.cyb.root.DpiBatchMeetBackend.modules.admin.repository.ApprovalRequestRepository;
import mh.cyb.root.DpiBatchMeetBackend.modules.user.repository.UserRepository;
import mh.cyb.root.DpiBatchMeetBackend.modules.admin.mapper.ApprovalRequestMapper;
import mh.cyb.root.DpiBatchMeetBackend.modules.admin.dto.ApprovalRequestDto;
import mh.cyb.root.DpiBatchMeetBackend.modules.admin.service.AuditService;
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

    @Mock
    private ApprovalRequestMapper approvalRequestMapper;

    @Mock
    private AuditService auditService;

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

        ApprovalRequest savedRequest = new ApprovalRequest();
        savedRequest.setId(10L);
        savedRequest.setUser(user);
        savedRequest.setStatus(ApprovalStatus.PENDING);

        ApprovalRequestDto dto = new ApprovalRequestDto();
        dto.setId(10L);
        // ... set other fields

        when(approvalRequestRepository.save(any(ApprovalRequest.class))).thenReturn(savedRequest);
        when(approvalRequestMapper.toDto(any(ApprovalRequest.class))).thenReturn(dto);

        ApprovalRequestDto result = approvalService.createRequest(user);

        assertNotNull(result);
        assertEquals(10L, result.getId());
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

        ApprovalRequestDto dto = new ApprovalRequestDto();
        dto.setStatus(ApprovalStatus.APPROVED);

        when(approvalRequestMapper.toDto(any(ApprovalRequest.class))).thenReturn(dto);

        ApprovalRequestDto approved = approvalService.approveRequest(10L, reviewer);

        assertEquals(ApprovalStatus.APPROVED, approved.getStatus());
        // assertEquals(reviewer, approved.getReviewedBy());
        assertTrue(request.getUser().isEnabled()); // User should be enabled!
        verify(userRepository, times(1)).save(user); // Verify user update
    }
}
