package com.digiyak.identity;

import java.util.UUID;

/**
 * Public Spring Modulith facade exposed by the Identity module to other domain modules.
 *
 * <p><b>Why use a module API interface?</b>
 * Prevents cross-module coupling to {@code identity.internal} repositories or JPA entities.
 * Other modules inject {@code IdentityModuleApi} and receive only minimal DTO projections
 * ({@link UserDeliveryEligibility}), allowing Identity's internal database tables to evolve freely.
 */
public interface IdentityModuleApi {

    /** Checks whether a user is active and allowed to use postpaid delivery modes. */
    UserDeliveryEligibility getDeliveryEligibility(UUID userId);

    /** Increments a buyer's no-show counter (triggers postpaid lockout at threshold). */
    void recordNoShow(UUID userId);

    /** Appends an immutable entry to {@code identity.admin_audit_logs}. */
    void recordAdminAction(
            UUID actorUserId,
            String actorRole,
            UUID targetCampusId,
            String action,
            String targetEntityType,
            UUID targetEntityId,
            String metadataJson,
            String ipAddress
    );

    /** Read-only snapshot of user eligibility returned across module boundaries. */
    record UserDeliveryEligibility(
            UUID userId,
            UUID campusId,
            String fullName,
            String phone,
            boolean active,
            boolean verified,
            boolean allowPostpaid,
            int noShowCount
    ) {
    }
}
