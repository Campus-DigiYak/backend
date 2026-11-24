package com.digiyak.delivery.domain;

import com.digiyak.delivery.domain.DeliveryEnums.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Spring Modulith domain events persisted via the Transactional Outbox ({@code modulith.event_publication}).
 *
 * <p><b>Why use the Transactional Outbox pattern?</b>
 * Publishing an event inside the same {@code @Transactional} database commit as saving
 * {@link DeliveryRequest} guarantees that Dispatch and Finance will reliably receive the event
 * even if the application crashes right after committing (preventing dual-write inconsistencies).
 */
public final class DeliveryEvents {

    private DeliveryEvents() {
    }

    /** Consumed by Dispatch to create an open gig and push it over SSE to online campus runners. */
    public record DeliveryReadyForDispatchEvent(
            UUID deliveryRequestId,
            String trackingCode,
            UUID campusId,
            PaymentMethod paymentMethod,
            String pickupLabel,
            BigDecimal pickupLat,
            BigDecimal pickupLng,
            String dropoffLabel,
            BigDecimal dropoffLat,
            BigDecimal dropoffLng,
            long runnerPayoutPesewas,
            Instant occurredAt
    ) {
    }

    /** Consumed by Finance/Gateway to trigger a MoMo prompt when runner arrives for Pay-After-Delivery. */
    public record DeliveryHandoffPaymentRequestedEvent(
            UUID deliveryRequestId,
            String trackingCode,
            String shortPayLinkCode,
            long amountPesewas,
            String recipientPhone,
            Instant occurredAt
    ) {
    }

    /** Consumed by Finance Ledger to execute the 85%/15% two-way split or debit cash commission. */
    public record DeliveryCompletedEvent(
            UUID deliveryRequestId,
            String trackingCode,
            UUID campusId,
            UUID runnerUserId,
            PaymentMethod paymentMethod,
            long deliveryFeePesewas,
            long runnerPayoutPesewas,
            long platformCutPesewas,
            boolean cashCollected,
            Instant occurredAt
    ) {
    }

    /** Consumed by Dispatch (cancel assignment) and Finance (issue refund if already prepaid). */
    public record DeliveryCancelledEvent(
            UUID deliveryRequestId,
            String trackingCode,
            UUID campusId,
            UUID requesterUserId,
            PaymentMethod paymentMethod,
            boolean requiresRefund,
            String reason,
            Instant occurredAt
    ) {
    }
}
