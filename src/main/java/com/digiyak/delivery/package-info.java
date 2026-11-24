/**
 * Delivery Requests &amp; Payment Links module boundary.
 *
 * <p>Allowed to depend only on {@code shared}, {@code campus}, and {@code identity}.
 * Communicates downstream with {@code dispatch} and {@code finance} via domain events
 * ({@link com.digiyak.delivery.domain.DeliveryEvents}) instead of direct service calls,
 * preventing cyclic dependencies.
 */
@org.springframework.modulith.ApplicationModule(
        displayName = "Delivery Requests & Payment Links",
        allowedDependencies = {"shared", "campus", "identity"}
)
package com.digiyak.delivery;
