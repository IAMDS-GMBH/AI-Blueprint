package com.payroll;

import com.payroll.exception.PayrollValidationException;
import com.payroll.model.PayrollInput;
import com.payroll.model.PayrollResult;
import com.payroll.model.PayrollStatus;
import com.payroll.service.PayrollService;
import com.payroll.service.PayrollServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * TDD-Testfaelle fuer PayrollService — abgeleitet aus dem Pseudocode payroll-pseudocode.md.
 *
 * Diese Tests definieren das erwartete Verhalten VOR der Implementierung.
 * Workflow: Tests schreiben → rot → implementieren → gruen → refactoren.
 */
class PayrollServiceTddTest {

    private PayrollService service;

    @BeforeEach
    void setUp() {
        service = new PayrollServiceImpl();
    }

    // ========================================================================
    // 1. EINGABE-VALIDIERUNG (aus FUNKTION EINGABE-VALIDIEREN)
    // ========================================================================
    @Nested
    @DisplayName("Eingabe-Validierung")
    class EingabeValidierung {

        @Test
        @DisplayName("Bruttogehalt <= 0 → Exception")
        void bruttogehaltNullOderNegativ_wirftException() {
            PayrollInput input = new PayrollInput(
                    100001,
                    BigDecimal.ZERO,            // Bruttogehalt = 0
                    new BigDecimal("20.00"),
                    new BigDecimal("10.00"),
                    BigDecimal.ZERO
            );

            assertThatThrownBy(() -> service.calculate(input))
                    .isInstanceOf(PayrollValidationException.class);
        }

        @Test
        @DisplayName("Negatives Bruttogehalt → Exception")
        void negativesBruttogehalt_wirftException() {
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("-1000.00"),  // negativ
                    new BigDecimal("20.00"),
                    new BigDecimal("10.00"),
                    BigDecimal.ZERO
            );

            assertThatThrownBy(() -> service.calculate(input))
                    .isInstanceOf(PayrollValidationException.class);
        }

        @Test
        @DisplayName("Steuersatz > 50 → Exception")
        void steuersatzUeber50_wirftException() {
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("5000.00"),
                    new BigDecimal("50.01"),     // > 50
                    new BigDecimal("10.00"),
                    BigDecimal.ZERO
            );

            assertThatThrownBy(() -> service.calculate(input))
                    .isInstanceOf(PayrollValidationException.class);
        }

        @Test
        @DisplayName("Steuersatz = 50 → gueltig (Grenzwert)")
        void steuersatzGenau50_istGueltig() {
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("5000.00"),
                    new BigDecimal("50.00"),     // exakt 50 = erlaubt
                    new BigDecimal("10.00"),
                    BigDecimal.ZERO
            );

            PayrollResult result = service.calculate(input);
            assertThat(result.status()).isEqualTo(PayrollStatus.ERFOLG);
        }

        @Test
        @DisplayName("Sozialabgabensatz > 30 → Exception")
        void sozialabgabensatzUeber30_wirftException() {
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("5000.00"),
                    new BigDecimal("20.00"),
                    new BigDecimal("30.01"),     // > 30
                    BigDecimal.ZERO
            );

            assertThatThrownBy(() -> service.calculate(input))
                    .isInstanceOf(PayrollValidationException.class);
        }

        @Test
        @DisplayName("Sozialabgabensatz = 30 → gueltig (Grenzwert)")
        void sozialabgabensatzGenau30_istGueltig() {
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("5000.00"),
                    new BigDecimal("20.00"),
                    new BigDecimal("30.00"),     // exakt 30 = erlaubt
                    BigDecimal.ZERO
            );

            PayrollResult result = service.calculate(input);
            assertThat(result.status()).isEqualTo(PayrollStatus.ERFOLG);
        }
    }

    // ========================================================================
    // 2. STEUERBASIS-BERECHNUNG (aus FUNKTION STEUERBASIS-BERECHNEN)
    //    Steuerbasis = Bruttogehalt + Bonus
    // ========================================================================
    @Nested
    @DisplayName("Steuerbasis-Berechnung")
    class SteuerbasisBerechnung {

        @Test
        @DisplayName("Steuerbasis = Bruttogehalt + Bonus → Steuer wird auf Basis berechnet")
        void steuerbasisIstBruttoPlusBonus() {
            // Bruttogehalt=5000, Bonus=1000, Steuersatz=10%
            // Steuerbasis = 5000 + 1000 = 6000
            // Steuerbetrag = 6000 * 10 / 100 = 600
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("5000.00"),
                    new BigDecimal("10.00"),
                    new BigDecimal("0.00"),
                    new BigDecimal("1000.00")
            );

            PayrollResult result = service.calculate(input);
            // Steuer auf Steuerbasis (6000), nicht nur auf Bruttogehalt
            assertThat(result.taxPaid()).isEqualByComparingTo(new BigDecimal("600.00"));
        }

        @Test
        @DisplayName("Ohne Bonus → Steuerbasis = Bruttogehalt")
        void ohneBonus_steuerbasisGleichBrutto() {
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("4000.00"),
                    new BigDecimal("20.00"),
                    new BigDecimal("0.00"),
                    BigDecimal.ZERO             // kein Bonus
            );

            PayrollResult result = service.calculate(input);
            // Steuerbetrag = 4000 * 20 / 100 = 800
            assertThat(result.taxPaid()).isEqualByComparingTo(new BigDecimal("800.00"));
        }
    }

    // ========================================================================
    // 3. ABZUEGE-BERECHNUNG (aus FUNKTION ABZUEGE-BERECHNEN)
    //    Steuerbetrag = Steuerbasis * Steuersatz / 100
    //    Sozialabgaben = Bruttogehalt * Sozialabgabensatz / 100 (NICHT Steuerbasis!)
    // ========================================================================
    @Nested
    @DisplayName("Abzuege-Berechnung")
    class AbzuegeBerechnung {

        @Test
        @DisplayName("Sozialabgaben werden NUR auf Bruttogehalt berechnet, nicht auf Bonus")
        void sozialabgabenNurAufBrutto() {
            // Bruttogehalt=5000, Bonus=2000, Sozialabgabensatz=20%
            // Sozialabgaben = 5000 * 20 / 100 = 1000 (NICHT 7000 * 20%)
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("5000.00"),
                    new BigDecimal("0.00"),
                    new BigDecimal("20.00"),
                    new BigDecimal("2000.00")
            );

            PayrollResult result = service.calculate(input);
            assertThat(result.socialPaid()).isEqualByComparingTo(new BigDecimal("1000.00"));
        }

        @Test
        @DisplayName("Steuer wird auf Steuerbasis (Brutto+Bonus) berechnet")
        void steuerAufSteuerbasis() {
            // Bruttogehalt=3000, Bonus=500, Steuersatz=25%
            // Steuerbasis = 3500
            // Steuerbetrag = 3500 * 25 / 100 = 875
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("3000.00"),
                    new BigDecimal("25.00"),
                    new BigDecimal("0.00"),
                    new BigDecimal("500.00")
            );

            PayrollResult result = service.calculate(input);
            assertThat(result.taxPaid()).isEqualByComparingTo(new BigDecimal("875.00"));
        }
    }

    // ========================================================================
    // 4. NETTOGEHALT-BERECHNUNG (aus FUNKTION NETTOGEHALT-BERECHNEN)
    //    Nettogehalt = Steuerbasis - Gesamtabzuege
    // ========================================================================
    @Nested
    @DisplayName("Nettogehalt-Berechnung")
    class NettogehaltBerechnung {

        @Test
        @DisplayName("Standardfall: Brutto=5000, Steuer=20%, Sozial=10%, Bonus=0")
        void standardBerechnung() {
            // Steuerbasis = 5000 + 0 = 5000
            // Steuerbetrag = 5000 * 20 / 100 = 1000
            // Sozialabgaben = 5000 * 10 / 100 = 500
            // Gesamtabzuege = 1500
            // Nettogehalt = 5000 - 1500 = 3500
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("5000.00"),
                    new BigDecimal("20.00"),
                    new BigDecimal("10.00"),
                    BigDecimal.ZERO
            );

            PayrollResult result = service.calculate(input);

            assertThat(result.netSalary()).isEqualByComparingTo(new BigDecimal("3500.00"));
            assertThat(result.taxPaid()).isEqualByComparingTo(new BigDecimal("1000.00"));
            assertThat(result.socialPaid()).isEqualByComparingTo(new BigDecimal("500.00"));
            assertThat(result.status()).isEqualTo(PayrollStatus.ERFOLG);
        }

        @Test
        @DisplayName("Mit Bonus: Nettogehalt = Steuerbasis - Abzuege")
        void mitBonus_nettogehaltKorrekt() {
            // Bruttogehalt=4000, Bonus=1000, Steuersatz=10%, Sozialabgabensatz=15%
            // Steuerbasis = 4000 + 1000 = 5000
            // Steuerbetrag = 5000 * 10 / 100 = 500
            // Sozialabgaben = 4000 * 15 / 100 = 600
            // Gesamtabzuege = 1100
            // Nettogehalt = 5000 - 1100 = 3900
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("4000.00"),
                    new BigDecimal("10.00"),
                    new BigDecimal("15.00"),
                    new BigDecimal("1000.00")
            );

            PayrollResult result = service.calculate(input);

            assertThat(result.netSalary()).isEqualByComparingTo(new BigDecimal("3900.00"));
            assertThat(result.taxPaid()).isEqualByComparingTo(new BigDecimal("500.00"));
            assertThat(result.socialPaid()).isEqualByComparingTo(new BigDecimal("600.00"));
            assertThat(result.status()).isEqualTo(PayrollStatus.ERFOLG);
        }

        @Test
        @DisplayName("Maximale Abzuege: Steuer=50%, Sozial=30%")
        void maximaleAbzuege() {
            // Bruttogehalt=10000, Bonus=0, Steuersatz=50%, Sozialabgabensatz=30%
            // Steuerbasis = 10000
            // Steuerbetrag = 10000 * 50 / 100 = 5000
            // Sozialabgaben = 10000 * 30 / 100 = 3000
            // Gesamtabzuege = 8000
            // Nettogehalt = 10000 - 8000 = 2000
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("10000.00"),
                    new BigDecimal("50.00"),
                    new BigDecimal("30.00"),
                    BigDecimal.ZERO
            );

            PayrollResult result = service.calculate(input);

            assertThat(result.netSalary()).isEqualByComparingTo(new BigDecimal("2000.00"));
            assertThat(result.status()).isEqualTo(PayrollStatus.ERFOLG);
        }

        @Test
        @DisplayName("Nur Steuer, keine Sozialabgaben")
        void nurSteuer_keineSozialabgaben() {
            // Bruttogehalt=6000, Steuersatz=25%, Sozialabgabensatz=0%
            // Nettogehalt = 6000 - 1500 = 4500
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("6000.00"),
                    new BigDecimal("25.00"),
                    new BigDecimal("0.00"),
                    BigDecimal.ZERO
            );

            PayrollResult result = service.calculate(input);

            assertThat(result.netSalary()).isEqualByComparingTo(new BigDecimal("4500.00"));
            assertThat(result.socialPaid()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("Keine Abzuege: Steuer=0%, Sozial=0%")
        void keineAbzuege() {
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("3000.00"),
                    new BigDecimal("0.00"),
                    new BigDecimal("0.00"),
                    BigDecimal.ZERO
            );

            PayrollResult result = service.calculate(input);

            assertThat(result.netSalary()).isEqualByComparingTo(new BigDecimal("3000.00"));
        }
    }

    // ========================================================================
    // 5. STATUS-CODE (aus Hauptlogik)
    // ========================================================================
    @Nested
    @DisplayName("Status-Code")
    class StatusCode {

        @Test
        @DisplayName("Erfolgreiche Berechnung → Status ERFOLG")
        void erfolgreicheBerechnung_statusErfolg() {
            PayrollInput input = new PayrollInput(
                    100001,
                    new BigDecimal("5000.00"),
                    new BigDecimal("20.00"),
                    new BigDecimal("10.00"),
                    BigDecimal.ZERO
            );

            PayrollResult result = service.calculate(input);
            assertThat(result.status()).isEqualTo(PayrollStatus.ERFOLG);
            assertThat(result.status().getCode()).isEqualTo("OK");
        }
    }
}
