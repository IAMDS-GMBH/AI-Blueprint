package com.inventory.model;

/**
 * Buchungs-Eingabe — Lagerbewegung (aus COBOL: WS-TRANS-*).
 */
public record TransactionInput(
        TransactionType transactionType,
        int quantity
) {
}
