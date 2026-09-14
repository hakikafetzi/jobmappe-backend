package dev.kafetzi.jobmappe.tracking.jobposting.projection;

import java.util.UUID;

public interface CountByCompanyProjection {
    UUID getCompanyId();
    Long getCount();
}
