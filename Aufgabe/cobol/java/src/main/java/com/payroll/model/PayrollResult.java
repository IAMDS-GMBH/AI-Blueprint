package com.payroll.model;

import java.math.BigDecimal;

/**
 * Ergebnis der Gehaltsberechnung.
 *
 * @param netSalary  Berechnetes Nettogehalt
 * @param taxPaid    Tatsaechlich abgefuehrte Steuer
 * @param socialPaid Tatsaechlich abgefuehrte Sozialabgaben
 * @param status     Ergebnisstatus der Berechnung
 */
public record PayrollResult(
        BigDecimal netSalary,
        BigDecimal taxPaid,
        BigDecimal socialPaid,
        PayrollStatus status
) {
}
