/**
 * Dispatch &amp; Live Tracking module boundary.
 *
 * <p>Merged {@code assignment} and {@code realtime} sub-folders into the standard
 * {@code api / domain / internal} Spring Modulith structure.
 * Coordinates atomic runner gig claiming, Redis GEO live coordinates, and SSE fan-out.
 */
@org.springframework.modulith.ApplicationModule(
        displayName = "Dispatch & Live Tracking",
        allowedDependencies = {"shared", "campus", "delivery", "identity"}
)
package com.digiyak.dispatch;
