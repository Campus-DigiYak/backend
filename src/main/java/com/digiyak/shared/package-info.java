/**
 * Shared Kernel module ({@code Type.OPEN}).
 *
 * <p>Marking this module {@code OPEN} allows every business module in Digiyak to import
 * cross-cutting primitives (such as {@link com.digiyak.shared.Money}, {@link com.digiyak.shared.ApiException},
 * and {@link com.digiyak.shared.AuthenticatedUser}) without creating circular dependencies.
 */
@org.springframework.modulith.ApplicationModule(
        displayName = "Shared Kernel",
        type = org.springframework.modulith.ApplicationModule.Type.OPEN
)
package com.digiyak.shared;
