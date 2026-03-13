package com.payroll.model;

import java.math.BigDecimal;

/**
 * Eingabedaten fuer die Gehaltsberechnung.
 *
 * @param employeeId   Eindeutige Mitarbeiternummer (6 Stellen)
 * @param grossSalary  Monatliches Bruttogehalt
 * @param taxRate      Steuersatz in Prozent
 * @param socialRate   Sozialabgabensatz in Prozent
 * @param bonus        Zusaetzlicher Bonus
 */
public record PayrollInput(
        int employeeId,
        BigDecimal grossSalary,
        BigDecimal taxRate,
        BigDecimal socialRate,
        BigDecimal bonus
) {
}
