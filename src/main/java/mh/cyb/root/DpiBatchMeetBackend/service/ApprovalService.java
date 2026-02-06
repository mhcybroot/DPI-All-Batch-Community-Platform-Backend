package mh.cyb.root.DpiBatchMeetBackend.service;

import mh.cyb.root.DpiBatchMeetBackend.domain.ApprovalRequest;
import mh.cyb.root.DpiBatchMeetBackend.domain.User;
import java.util.List;

public interface ApprovalService {
    ApprovalRequest createRequest(User user);

    List<ApprovalRequest> getPendingRequests();

    ApprovalRequest approveRequest(Long requestId, User reviewer);

    ApprovalRequest rejectRequest(Long requestId, User reviewer, String reason);
}
