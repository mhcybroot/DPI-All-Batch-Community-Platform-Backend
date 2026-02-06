package mh.cyb.root.DpiBatchMeetBackend.service;

import mh.cyb.root.DpiBatchMeetBackend.domain.ApprovalRequest;
import mh.cyb.root.DpiBatchMeetBackend.domain.User;
import mh.cyb.root.DpiBatchMeetBackend.repository.ApprovalRequestRepository;
import mh.cyb.root.DpiBatchMeetBackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRequestRepository approvalRequestRepository;
    private final UserRepository userRepository;

    public ApprovalServiceImpl(ApprovalRequestRepository approvalRequestRepository, UserRepository userRepository) {
        this.approvalRequestRepository = approvalRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ApprovalRequest createRequest(User user) {
        ApprovalRequest request = new ApprovalRequest();
        request.setUser(user);
        request.setStatus(ApprovalRequest.ApprovalStatus.PENDING);
        return approvalRequestRepository.save(request);
    }

    @Override
    public List<ApprovalRequest> getPendingRequests() {
        return approvalRequestRepository.findByStatus(ApprovalRequest.ApprovalStatus.PENDING);
    }

    @Override
    public ApprovalRequest approveRequest(Long requestId, User reviewer) {
        ApprovalRequest request = approvalRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found")); // In real app, use custom exception

        request.setStatus(ApprovalRequest.ApprovalStatus.APPROVED);
        request.setReviewedBy(reviewer);

        // Enable user
        User user = request.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        return approvalRequestRepository.save(request);
    }

    @Override
    public ApprovalRequest rejectRequest(Long requestId, User reviewer, String reason) {
        ApprovalRequest request = approvalRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(ApprovalRequest.ApprovalStatus.REJECTED);
        request.setReviewedBy(reviewer);
        request.setRejectionReason(reason);

        // User remains disabled
        return approvalRequestRepository.save(request);
    }
}
