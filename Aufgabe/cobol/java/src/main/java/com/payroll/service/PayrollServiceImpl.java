package com.payroll.service;

import com.payroll.exception.PayrollValidationException;
import com.payroll.model.PayrollInput;
import com.payroll.model.PayrollResult;
import com.payroll.model.PayrollStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementierung der Gehaltsberechnung.
 * Uebersetzt aus COBOL-Programm payroll.cbl.
 */
@Service
public class PayrollServiceImpl implements PayrollService {

    private static final Logger log = LoggerFactory.getLogger(PayrollServiceImpl.class);

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal MAX_TAX_RATE = BigDecimal.valueOf(50);
    private static final BigDecimal MAX_SOCIAL_RATE = BigDecimal.valueOf(30);
    private static final int SCALE = 2;

    @Override
    public PayrollResult calculate(PayrollInput input) {
        validate(input);

        BigDecimal taxableBase = calculateTaxableBase(input);
        BigDecimal taxAmount = calculateTaxAmount(taxableBase, input.taxRate());
        BigDecimal socialAmount = calculateSocialAmount(input.grossSalary(), input.socialRate());
        BigDecimal totalDeductions = taxAmount.add(socialAmount);
        BigDecimal netSalary = taxableBase.subtract(totalDeductions);

        log.info("Gehaltsberechnung fuer Mitarbeiter {} abgeschlossen: Netto={}",
                input.employeeId(), netSalary);

        return new PayrollResult(netSalary, taxAmount, socialAmount, PayrollStatus.ERFOLG);
    }

    private void validate(PayrollInput input) {
        if (input.grossSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PayrollValidationException("Bruttogehalt muss positiv sein");
        }
        if (input.taxRate().compareTo(MAX_TAX_RATE) > 0) {
            throw new PayrollValidationException("Steuersatz darf maximal 50% betragen");
        }
        if (input.socialRate().compareTo(MAX_SOCIAL_RATE) > 0) {
            throw new PayrollValidationException("Sozialabgabensatz darf maximal 30% betragen");
        }
    }

    private BigDecimal calculateTaxableBase(PayrollInput input) {
        return input.grossSalary().add(input.bonus());
    }

    private BigDecimal calculateTaxAmount(BigDecimal taxableBase, BigDecimal taxRate) {
        return taxableBase
                .multiply(taxRate)
                .divide(HUNDRED, SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateSocialAmount(BigDecimal grossSalary, BigDecimal socialRate) {
        return grossSalary
                .multiply(socialRate)
                .divide(HUNDRED, SCALE, RoundingMode.HALF_UP);
    }
}
