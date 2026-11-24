package com.digiyak.delivery;

import com.digiyak.delivery.domain.DeliveryEnums.DeliveryStatus;
import com.digiyak.delivery.domain.DeliveryEnums.PaymentMethod;

import java.util.UUID;

/**
 * Public Spring Modulith facade exposed by the Delivery module to Dispatch and Finance modules.
 *
 * <p><b>Why expose an interface at the module root?</b>
 * Spring Modulith forbids other modules from importing {@code delivery.internal.*}.
 * Dispatch and Finance inject this facade to query delivery summaries or drive valid state transitions
 * without bypassing {@code DeliveryRequest}'s domain invariants.
 */
public interface DeliveryModuleApi {

    DeliverySummary getSummary(UUID deliveryRequestId);

    /** Called by Finance once MoMo Prepaid or Pay-After-Delivery webhook confirms payment. */
    void markPaymentConfirmed(UUID deliveryRequestId, UUID actorUserId, String paymentReference);

    /** Called by Dispatch once a Runner atomically claims an open gig. */
    void markRunnerAssigned(UUID deliveryRequestId, UUID runnerUserId);

    /** Verifies the 4-digit Pickup Code and moves the delivery to {@code IN_TRANSIT}. */
    void markPickedUp(UUID deliveryRequestId, UUID runnerUserId, String pickupCode, String pickupPhotoUrl);

    /** Initiates Pay-After-Delivery MoMo prompt/link at handoff. */
    HandoffPaymentInfo requestHandoffPayment(UUID deliveryRequestId, UUID runnerUserId);

    /** Verifies the 4-digit Delivery PIN (and cash flag if applicable) to complete delivery. */
    void completeDelivery(
            UUID deliveryRequestId,
            UUID runnerUserId,
            String deliveryPin,
            boolean cashCollected,
            String dropoffPhotoUrl
    );

    /** Flags an unreachable recipient and triggers Identity's anti-ghosting no-show counter. */
    void reportBuyerNoShow(UUID deliveryRequestId, UUID runnerUserId, String reason);

    record DeliverySummary(
            UUID id,
            String trackingCode,
            UUID campusId,
            UUID requesterUserId,
            PaymentMethod paymentMethod,
            DeliveryStatus status,
            long deliveryFeePesewas,
            long runnerPayoutPesewas,
            long platformCutPesewas
    ) {
    }

    record HandoffPaymentInfo(
            UUID deliveryRequestId,
            String shortCode,
            long amountPesewas
    ) {
    }
}
