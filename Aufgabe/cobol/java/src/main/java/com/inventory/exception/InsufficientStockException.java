package com.inventory.exception;

/**
 * Exception bei unzureichendem Lagerbestand (ersetzt COBOL Error-Code BESTAND_UNZUREICHEND).
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String message) {
        super(message);
    }
}
