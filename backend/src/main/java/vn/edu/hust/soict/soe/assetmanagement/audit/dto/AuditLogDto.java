package vn.edu.hust.soict.soe.assetmanagement.audit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * API response shape for audit log search (RP-03).
 * Excludes internal fields such as {@code userId} from the entity.
 */
@Getter
@Builder
@NoArgsConstructor 
@AllArgsConstructor
public class AuditLogDto {
    private UUID id;
    private String module;
    private String action;
    private String recordId;
    private String recordCode;
    private String performedBy;
    private String ipAddress;
    private String oldValue;
    private String newValue;
    private String description;
    private LocalDateTime performedAt;
}