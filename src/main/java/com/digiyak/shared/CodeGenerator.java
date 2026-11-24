package com.digiyak.shared;

import java.security.SecureRandom;

/**
 * Cryptographically strong generator for human-friendly Digiyak codes &amp; handoff PINs.
 *
 * <p><b>Why {@link SecureRandom} instead of {@code java.util.Random}?</b>
 * Pickup codes and delivery PINs authorize physical package handoff and financial settlement;
 * {@code SecureRandom} ensures an attacker cannot predict the next PIN from previous ones.
 */
public final class CodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    // Excludes visually ambiguous characters (0/O and 1/I) so students can read WhatsApp links easily.
    private static final char[] ALPHANUMERIC = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();

    private CodeGenerator() {
    }

    /** 4-digit numeric PIN for Pickup Code or Delivery Handoff PIN (0000-9999). */
    public static String fourDigitPin() {
        return String.format("%04d", RANDOM.nextInt(10_000));
    }

    /** Human-readable delivery tracking code, e.g. {@code DYK-48291}. */
    public static String trackingCode() {
        return "DYK-" + String.format("%05d", RANDOM.nextInt(100_000));
    }

    /** URL-safe 8-character short code for 1-tap WhatsApp payment links. */
    public static String shortPayLinkCode() {
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(ALPHANUMERIC[RANDOM.nextInt(ALPHANUMERIC.length)]);
        }
        return sb.toString();
    }
}
