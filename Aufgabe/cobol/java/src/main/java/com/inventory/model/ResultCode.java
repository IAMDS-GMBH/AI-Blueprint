package com.inventory.model;

/**
 * Ergebnis-Status der Verarbeitung (aus COBOL: WS-RESULT-CODE).
 */
public enum ResultCode {

    ERFOLG("OK"),
    BESTAND_UNZUREICHEND("IS"),
    UNGUELTIGE_BUCHUNG("IT");

    private final String code;

    ResultCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
