package com.payroll.model;

public enum PayrollStatus {
    ERFOLG("OK"),
    FEHLER("ER");

    private final String code;

    PayrollStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
