package mh.cyb.root.DpiBatchMeetBackend.service;

import mh.cyb.root.DpiBatchMeetBackend.domain.AuditLog;
import mh.cyb.root.DpiBatchMeetBackend.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuditServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogAction() {
        auditService.logAction(1L, "LOGIN", "session-1", "User logged in", "127.0.0.1");

        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }
}
