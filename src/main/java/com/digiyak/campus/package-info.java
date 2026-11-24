/**
 * Campus &amp; Geospatial module boundary.
 * Owns PostGIS boundary polygons ({@code ST_Contains}), campus zones, landmarks, and zone-to-zone pricing.
 */
@org.springframework.modulith.ApplicationModule(
        displayName = "Campus & Geospatial",
        allowedDependencies = {"shared"}
)
package com.digiyak.campus;
