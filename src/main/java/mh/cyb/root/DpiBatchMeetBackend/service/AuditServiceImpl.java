package mh.cyb.root.DpiBatchMeetBackend.service;

import mh.cyb.root.DpiBatchMeetBackend.domain.AuditLog;
import mh.cyb.root.DpiBatchMeetBackend.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void logAction(Long actorId, String action, String targetId, String details, String ipAddress) {
        AuditLog log = AuditLog.builder()
                .actorId(actorId)
                .action(action)
                .targetId(targetId)
                .details(details)
                .ipAddress(ipAddress)
                .build();
        auditLogRepository.save(log);
    }

    @Override
    public List<AuditLog> getLogsByActor(Long actorId) {
        return auditLogRepository.findByActorId(actorId);
    }
}
