package mh.cyb.root.DpiBatchMeetBackend.controller;

import mh.cyb.root.DpiBatchMeetBackend.domain.ApprovalRequest;
import mh.cyb.root.DpiBatchMeetBackend.domain.User;
import mh.cyb.root.DpiBatchMeetBackend.service.ApprovalService;
import mh.cyb.root.DpiBatchMeetBackend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdminController {

    private final ApprovalService approvalService;
    private final UserService userService;

    public AdminController(ApprovalService approvalService, UserService userService) {
        this.approvalService = approvalService;
        this.userService = userService;
    }

    @GetMapping("/approvals")
    public ResponseEntity<List<ApprovalRequest>> getPendingApprovals() {
        return ResponseEntity.ok(approvalService.getPendingRequests());
    }

    @PostMapping("/approvals/{id}/approve")
    public ResponseEntity<?> approveUser(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        User reviewer = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        return ResponseEntity.ok(approvalService.approveRequest(id, reviewer));
    }

    @PostMapping("/approvals/{id}/reject")
    public ResponseEntity<?> rejectUser(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody(required = false) String reason) {
        User reviewer = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        return ResponseEntity.ok(approvalService.rejectRequest(id, reviewer, reason));
    }
}
