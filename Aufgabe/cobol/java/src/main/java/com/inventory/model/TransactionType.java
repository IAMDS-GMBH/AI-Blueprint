package com.inventory.model;

/**
 * Art der Lagerbewegung (aus COBOL: WS-TRANS-TYPE).
 */
public enum TransactionType {

    WARENEINGANG("I"),
    WARENAUSGANG("O");

    private final String code;

    TransactionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
