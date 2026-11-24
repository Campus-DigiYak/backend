/**
 * Finance, Double-Entry Ledger &amp; MoMo Gateway module boundary.
 *
 * <p>Merged {@code gateway} and {@code ledger} sub-folders into the standard
 * {@code api / domain / internal} Spring Modulith structure.
 * Guarantees balanced double-entry accounting ({@code SUM(DEBIT) == SUM(CREDIT)}),
 * 85%/15% two-way splits, runner physical cash float limits, and pluggable MoMo gateways.
 */
@org.springframework.modulith.ApplicationModule(
        displayName = "Finance, Double-Entry Ledger & MoMo Gateway",
        allowedDependencies = {"shared", "campus", "delivery", "identity"}
)
package com.digiyak.finance;
