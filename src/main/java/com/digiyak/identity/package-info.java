/**
 * Identity &amp; Auth module boundary.
 *
 * <p>Only {@code shared} may be imported by this module, keeping Identity at the bottom of the
 * dependency graph so other modules (Delivery, Dispatch, Finance) can depend on {@link IdentityModuleApi}
 * without creating cyclic dependencies.
 */
@org.springframework.modulith.ApplicationModule(
        displayName = "Identity & Auth",
        allowedDependencies = {"shared"}
)
package com.digiyak.identity;
