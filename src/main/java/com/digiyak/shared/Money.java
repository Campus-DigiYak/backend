package com.digiyak.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Immutable Money value object stored strictly in integer minor units (pesewas for GHS).
 *
 * <p><b>Why integer pesewas instead of {@code double} or {@code float}?</b>
 * Binary floating-point numbers cannot represent base-10 decimals like {@code 0.10} accurately,
 * leading to rounding drift in ledgers. Storing {@code GHS 8.50} as {@code 850L} pesewas guarantees
 * exact arithmetic and ensures {@code SUM(DEBIT) == SUM(CREDIT)} to the last pesewa.
 */
public record Money(long pesewas, String currency) {

    public static final String DEFAULT_CURRENCY = "GHS";
    private static final int BPS_DENOMINATOR = 10_000; // 10,000 basis points = 100.00%

    /** Compact constructor validates currency invariants at creation time. */
    public Money {
        Objects.requireNonNull(currency, "currency must not be null");
        if (currency.length() != 3) {
            throw new IllegalArgumentException("currency must be a 3-letter ISO code: " + currency);
        }
    }

    public static Money ghs(long pesewas) {
        return new Money(pesewas, DEFAULT_CURRENCY);
    }

    public static Money zero() {
        return ghs(0L);
    }

    /** Uses {@link Math#addExact} so integer overflow fails loudly instead of wrapping silently. */
    public Money plus(Money other) {
        requireSameCurrency(other);
        return new Money(Math.addExact(this.pesewas, other.pesewas), this.currency);
    }

    /** Uses {@link Math#subtractExact} to guard against silent arithmetic underflow/overflow. */
    public Money minus(Money other) {
        requireSameCurrency(other);
        return new Money(Math.subtractExact(this.pesewas, other.pesewas), this.currency);
    }

    /**
     * Computes a basis-point share (e.g. {@code 1500 bps = 15.00%}) using {@link RoundingMode#HALF_UP}.
     * Basis points allow fractional percentages (e.g. 12.5% = 1250 bps) with integer inputs.
     */
    public Money percentageBps(int basisPoints) {
        if (basisPoints < 0 || basisPoints > BPS_DENOMINATOR) {
            throw new IllegalArgumentException("basisPoints must be between 0 and 10000: " + basisPoints);
        }
        long share = BigDecimal.valueOf(this.pesewas)
                .multiply(BigDecimal.valueOf(basisPoints))
                .divide(BigDecimal.valueOf(BPS_DENOMINATOR), 0, RoundingMode.HALF_UP)
                .longValueExact();
        return new Money(share, this.currency);
    }

    /**
     * Splits a delivery fee cleanly into {@code (runnerShare, platformCut)}.
     *
     * <p><b>Why subtract instead of computing both percentages independently?</b>
     * Computing {@code runnerShare = total - platformCut} guarantees that
     * {@code runnerShare + platformCut == total} with zero rounding discrepancy (no lost or created pesewa).
     */
    public TwoWaySplit splitTwoWays(int platformCutBps) {
        Money platformCut = percentageBps(platformCutBps);
        Money runnerShare = this.minus(platformCut);
        return new TwoWaySplit(runnerShare, platformCut);
    }

    public boolean isPositive() {
        return pesewas > 0;
    }

    public boolean isNegative() {
        return pesewas < 0;
    }

    /** Formats minor units into human-readable currency text (e.g. {@code 850 -> "₵8.50"}). */
    public String formatDisplay() {
        BigDecimal major = BigDecimal.valueOf(pesewas, 2);
        return ("GHS".equals(currency) ? "₵" : currency + " ") + major.toPlainString();
    }

    private void requireSameCurrency(Money other) {
        Objects.requireNonNull(other, "other money must not be null");
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch: " + this.currency + " vs " + other.currency);
        }
    }

    public record TwoWaySplit(Money runnerShare, Money platformCut) {
    }
}
