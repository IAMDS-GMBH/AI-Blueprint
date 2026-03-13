package com.inventory.exception;

/**
 * Exception bei ungueltiger Lagerbewegung (ersetzt COBOL Error-Code UNGUELTIGE_BUCHUNG).
 */
public class InvalidTransactionException extends RuntimeException {

    public InvalidTransactionException(String message) {
        super(message);
    }
}
