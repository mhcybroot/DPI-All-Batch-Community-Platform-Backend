package mh.cyb.root.DpiBatchMeetBackend.service;

import mh.cyb.root.DpiBatchMeetBackend.domain.AuditLog;
import java.util.List;

public interface AuditService {
    void logAction(Long actorId, String action, String targetId, String details, String ipAddress);

    List<AuditLog> getLogsByActor(Long actorId);
}
