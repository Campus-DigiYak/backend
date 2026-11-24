package com.digiyak.delivery.internal;

import com.digiyak.delivery.api.DeliveryDtos.PaymentLinkResponse;
import com.digiyak.delivery.domain.DeliveryEnums.PayLinkStatus;
import com.digiyak.delivery.domain.DeliveryRequest;
import com.digiyak.delivery.domain.PaymentLink;
import com.digiyak.shared.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/** Internal service managing lifecycle and guest checkout lookups for {@link PaymentLink}s. */
@Service
@Transactional
public class PaymentLinkService {

    private final PaymentLinkRepository paymentLinkRepository;
    private final DeliveryRequestRepository deliveryRequestRepository;

    public PaymentLinkService(
            PaymentLinkRepository paymentLinkRepository,
            DeliveryRequestRepository deliveryRequestRepository
    ) {
        this.paymentLinkRepository = paymentLinkRepository;
        this.deliveryRequestRepository = deliveryRequestRepository;
    }

    /** Returns an existing payment link for a delivery or generates a fresh 30-minute link. */
    public PaymentLink getOrCreateForDelivery(DeliveryRequest request) {
        return paymentLinkRepository.findByDeliveryRequestId(request.getId())
                .orElseGet(() -> paymentLinkRepository.save(
                        PaymentLink.create(
                                request.getId(),
                                request.getRecipientPhone(),
                                request.getDeliveryFeePesewas()
                        )
                ));
    }

    /** Fetches public summary for {@code /api/v1/pay/{shortCode}}, enforcing link expiration. */
    @Transactional(readOnly = true)
    public PaymentLinkResponse getGuestCheckoutDetails(String shortCode) {
        PaymentLink link = paymentLinkRepository.findByShortCode(shortCode)
                .orElseThrow(() -> ApiException.notFound("PAYMENT_LINK_NOT_FOUND", "Payment link not found"));

        if (link.getStatus() == PayLinkStatus.ACTIVE && link.isExpired(Instant.now())) {
            throw ApiException.unprocessable("PAYMENT_LINK_EXPIRED", "This payment link has expired");
        }

        DeliveryRequest delivery = deliveryRequestRepository.findById(link.getDeliveryRequestId())
                .orElseThrow(() -> ApiException.notFound("DELIVERY_NOT_FOUND", "Associated delivery not found"));

        return PaymentLinkResponse.from(link, delivery);
    }

    public void markPaidIfPresent(UUID deliveryRequestId) {
        paymentLinkRepository.findByDeliveryRequestId(deliveryRequestId)
                .ifPresent(link -> {
                    link.markPaid();
                    paymentLinkRepository.save(link);
                });
    }

    public void cancelIfPresent(UUID deliveryRequestId) {
        paymentLinkRepository.findByDeliveryRequestId(deliveryRequestId)
                .ifPresent(link -> {
                    link.cancel();
                    paymentLinkRepository.save(link);
                });
    }
}
