package com.digiyak.shared;

import java.util.Set;
import java.util.UUID;

/**
 * Immutable security principal injected into controllers via {@code @AuthenticationPrincipal}
 * after {@code JwtAuthenticationFilter} verifies the incoming Bearer JWT.
 *
 * <p><b>Multi-Campus Tenant Isolation:</b>
 * {@code campusId} identifies which university campus a student, runner, vendor, or {@code CAMPUS_ADMIN}
 * belongs to. Global {@code SUPER_ADMIN} accounts have {@code campusId == null} so they can govern
 * all campuses across Ghana.
 */
public record AuthenticatedUser(
        UUID userId,
        UUID campusId,
        String email,
        Set<String> roles
) {

    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }

    public boolean isSuperAdmin() {
        return hasRole("SUPER_ADMIN");
    }

    public boolean isCampusAdmin() {
        return hasRole("CAMPUS_ADMIN");
    }

    public boolean isAnyAdmin() {
        return isSuperAdmin() || isCampusAdmin();
    }

    public boolean isRunner() {
        return hasRole("RUNNER");
    }

    /**
     * Enforces multi-campus tenant isolation:
     * <ul>
     *   <li>{@code SUPER_ADMIN} can manage any campus.</li>
     *   <li>{@code CAMPUS_ADMIN} can only manage records belonging to their own {@code campusId}.</li>
     * </ul>
     */
    public boolean canManageCampus(UUID targetCampusId) {
        if (isSuperAdmin()) {
            return true;
        }
        return isCampusAdmin() && campusId != null && campusId.equals(targetCampusId);
    }
}
