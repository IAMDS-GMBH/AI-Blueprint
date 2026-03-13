package com.payroll;

import com.payroll.exception.PayrollValidationException;
import com.payroll.model.PayrollInput;
import com.payroll.model.PayrollStatus;
import com.payroll.service.PayrollService;
import com.payroll.service.PayrollServiceImpl;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PayrollServiceImplTest {

    private PayrollService service;

    @BeforeEach
    void setUp() {
        service = new PayrollServiceImpl();
    }

    @Test
    void shouldCalculateNetSalaryForStandardInput() {
        var input = new PayrollInput(
                100001,
                BigDecimal.valueOf(5000),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(500)
        );

        var result = service.calculate(input);

        // Steuerbasis: 5000 + 500 = 5500
        // Steuer: 5500 * 20 / 100 = 1100
        // Sozial: 5000 * 10 / 100 = 500
        // Netto:  5500 - 1100 - 500 = 3900
        assertThat(result.status()).isEqualTo(PayrollStatus.ERFOLG);
        assertThat(result.taxPaid()).isEqualByComparingTo("1100.00");
        assertThat(result.socialPaid()).isEqualByComparingTo("500.00");
        assertThat(result.netSalary()).isEqualByComparingTo("3900.00");
    }

    @Test
    void shouldCalculateWithZeroBonus() {
        var input = new PayrollInput(
                100002,
                BigDecimal.valueOf(3000),
                BigDecimal.valueOf(25),
                BigDecimal.valueOf(15),
                BigDecimal.ZERO
        );

        var result = service.calculate(input);

        assertThat(result.netSalary()).isEqualByComparingTo("1800.00");
        assertThat(result.taxPaid()).isEqualByComparingTo("750.00");
        assertThat(result.socialPaid()).isEqualByComparingTo("450.00");
    }

    @Test
    void shouldCalculateWithMaxRates() {
        var input = new PayrollInput(
                100003,
                BigDecimal.valueOf(4000),
                BigDecimal.valueOf(50),
                BigDecimal.valueOf(30),
                BigDecimal.valueOf(1000)
        );

        var result = service.calculate(input);

        // Netto: 5000 - 2500 - 1200 = 1300
        assertThat(result.netSalary()).isEqualByComparingTo("1300.00");
    }

    @Test
    void shouldRoundTaxAmountCorrectly() {
        var input = new PayrollInput(
                100004,
                BigDecimal.valueOf(3333),
                BigDecimal.valueOf(19),
                BigDecimal.valueOf(10),
                BigDecimal.ZERO
        );

        var result = service.calculate(input);

        assertThat(result.taxPaid()).isEqualByComparingTo("633.27");
        assertThat(result.socialPaid()).isEqualByComparingTo("333.30");
    }

    @Test
    void shouldThrowWhenGrossSalaryIsZero() {
        var input = new PayrollInput(100005, BigDecimal.ZERO, BigDecimal.valueOf(20),
                BigDecimal.valueOf(10), BigDecimal.ZERO);

        assertThatThrownBy(() -> service.calculate(input))
                .isInstanceOf(PayrollValidationException.class)
                .hasMessageContaining("Bruttogehalt muss positiv sein");
    }

    @Test
    void shouldThrowWhenGrossSalaryIsNegative() {
        var input = new PayrollInput(100006, BigDecimal.valueOf(-1000), BigDecimal.valueOf(20),
                BigDecimal.valueOf(10), BigDecimal.ZERO);

        assertThatThrownBy(() -> service.calculate(input))
                .isInstanceOf(PayrollValidationException.class)
                .hasMessageContaining("Bruttogehalt muss positiv sein");
    }

    @Test
    void shouldThrowWhenTaxRateExceeds50() {
        var input = new PayrollInput(100007, BigDecimal.valueOf(5000), BigDecimal.valueOf(51),
                BigDecimal.valueOf(10), BigDecimal.ZERO);

        assertThatThrownBy(() -> service.calculate(input))
                .isInstanceOf(PayrollValidationException.class)
                .hasMessageContaining("Steuersatz darf maximal 50%");
    }

    @Test
    void shouldThrowWhenSocialRateExceeds30() {
        var input = new PayrollInput(100008, BigDecimal.valueOf(5000), BigDecimal.valueOf(20),
                BigDecimal.valueOf(31), BigDecimal.ZERO);

        assertThatThrownBy(() -> service.calculate(input))
                .isInstanceOf(PayrollValidationException.class)
                .hasMessageContaining("Sozialabgabensatz darf maximal 30%");
    }
}
