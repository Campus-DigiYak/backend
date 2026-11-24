package com.digiyak.dispatch.domain;

/**
 * Planned JPA entity for {@code dispatch.delivery_assignments} using optimistic locking ({@code @Version})
 * so only one runner can win a concurrent claim (losers receive {@code 409 Conflict}).
 */
public class DeliveryAssignment {
}
