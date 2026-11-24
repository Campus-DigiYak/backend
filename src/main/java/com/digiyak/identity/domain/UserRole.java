package com.digiyak.identity.domain;

/**
 * Application-wide RBAC roles stored in {@code identity.user_roles}.
 * A single student account can hold multiple roles simultaneously (e.g. CUSTOMER + RUNNER).
 */
public enum UserRole {
    CUSTOMER,
    VENDOR_PARTNER,
    RUNNER,
    CAMPUS_ADMIN,
    SUPER_ADMIN
}
