package vn.edu.hust.soict.soe.assetmanagement.lookup.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Minimal DTO for dropdown options: id, code, and display name.
 * Returned by all {@code /api/lookups/*} endpoints.
 */
@Getter
@Builder
public class LookupItemDto {
    private String id;
    private String code;
    private String name;
}
