package com.payroll.service;

import com.payroll.model.PayrollInput;
import com.payroll.model.PayrollResult;

/**
 * Service-Interface fuer die Gehaltsberechnung.
 */
public interface PayrollService {

    /**
     * Berechnet das Nettogehalt aus den gegebenen Eingabedaten.
     *
     * @param input Eingabedaten (Bruttogehalt, Steuersatz, Sozialabgabensatz, Bonus)
     * @return Ergebnis mit Nettogehalt, gezahlter Steuer und Sozialabgaben
     * @throws com.payroll.exception.PayrollValidationException bei ungueltigen Eingabedaten
     */
    PayrollResult calculate(PayrollInput input);
}
