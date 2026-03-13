package com.inventory;

import com.inventory.exception.InsufficientStockException;
import com.inventory.exception.InvalidTransactionException;
import com.inventory.model.*;
import com.inventory.service.InventoryService;
import com.inventory.service.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * TDD-Testfaelle fuer InventoryService — abgeleitet aus dem Pseudocode inventory-pseudocode.md.
 *
 * Diese Tests definieren das erwartete Verhalten VOR der Implementierung.
 * Workflow: Tests schreiben → rot → implementieren → gruen → refactoren.
 */
class InventoryServiceTddTest {

    private InventoryService service;

    private Article createArticle(int currentStock, int minStock, int reorderQty) {
        return new Article(10000001, "Testprodukt", currentStock, minStock, reorderQty, new BigDecimal("9.99"));
    }

    @BeforeEach
    void setUp() {
        service = new InventoryServiceImpl();
    }

    // ========================================================================
    // 1. BUCHUNG-VALIDIERUNG (aus FUNKTION BuchungValidieren)
    // ========================================================================
    @Nested
    @DisplayName("Buchung-Validierung")
    class BuchungValidierung {

        @Test
        @DisplayName("Buchungsmenge <= 0 → InvalidTransactionException")
        void buchungsmengeNull_wirftException() {
            Article article = createArticle(100, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENEINGANG, 0);

            assertThatThrownBy(() -> service.processTransaction(article, input))
                    .isInstanceOf(InvalidTransactionException.class);
        }

        @Test
        @DisplayName("Negative Buchungsmenge → InvalidTransactionException")
        void negativeBuchungsmenge_wirftException() {
            Article article = createArticle(100, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENEINGANG, -5);

            assertThatThrownBy(() -> service.processTransaction(article, input))
                    .isInstanceOf(InvalidTransactionException.class);
        }

        @Test
        @DisplayName("Null als TransactionType → InvalidTransactionException")
        void nullTransactionType_wirftException() {
            Article article = createArticle(100, 10, 50);
            TransactionInput input = new TransactionInput(null, 10);

            assertThatThrownBy(() -> service.processTransaction(article, input))
                    .isInstanceOf(InvalidTransactionException.class);
        }
    }

    // ========================================================================
    // 2. WARENEINGANG (aus FUNKTION WareneingangVerarbeiten)
    //    NeuerBestand = AktuellerBestand + Buchungsmenge
    // ========================================================================
    @Nested
    @DisplayName("Wareneingang")
    class Wareneingang {

        @Test
        @DisplayName("Standardfall: Bestand wird um Buchungsmenge erhoeht")
        void bestandWirdErhoeht() {
            Article article = createArticle(100, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENEINGANG, 25);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.newStock()).isEqualTo(125);
            assertThat(result.resultCode()).isEqualTo(ResultCode.ERFOLG);
        }

        @Test
        @DisplayName("Wareneingang bei Bestand 0 → Bestand = Buchungsmenge")
        void bestandBeiNull_wirdAufBuchungsmengeGesetzt() {
            Article article = createArticle(0, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENEINGANG, 30);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.newStock()).isEqualTo(30);
            assertThat(result.resultCode()).isEqualTo(ResultCode.ERFOLG);
        }

        @Test
        @DisplayName("Grosser Wareneingang ergibt korrekten Bestand")
        void grosserWareneingang() {
            Article article = createArticle(500, 100, 200);
            TransactionInput input = new TransactionInput(TransactionType.WARENEINGANG, 999);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.newStock()).isEqualTo(1499);
        }
    }

    // ========================================================================
    // 3. WARENAUSGANG (aus FUNKTION WarenausgangVerarbeiten)
    //    WENN Buchungsmenge > AktuellerBestand → BESTAND_UNZUREICHEND
    //    SONST NeuerBestand = AktuellerBestand - Buchungsmenge
    // ========================================================================
    @Nested
    @DisplayName("Warenausgang")
    class Warenausgang {

        @Test
        @DisplayName("Standardfall: Bestand wird um Buchungsmenge reduziert")
        void bestandWirdReduziert() {
            Article article = createArticle(100, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENAUSGANG, 30);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.newStock()).isEqualTo(70);
            assertThat(result.resultCode()).isEqualTo(ResultCode.ERFOLG);
        }

        @Test
        @DisplayName("Exakter Bestand wird entnommen → Bestand = 0")
        void gesamterBestandEntnommen() {
            Article article = createArticle(50, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENAUSGANG, 50);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.newStock()).isEqualTo(0);
            assertThat(result.resultCode()).isEqualTo(ResultCode.ERFOLG);
        }

        @Test
        @DisplayName("Buchungsmenge > Bestand → InsufficientStockException")
        void zuWenigBestand_wirftException() {
            Article article = createArticle(10, 5, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENAUSGANG, 15);

            assertThatThrownBy(() -> service.processTransaction(article, input))
                    .isInstanceOf(InsufficientStockException.class);
        }

        @Test
        @DisplayName("Bestand = 0, Warenausgang → InsufficientStockException")
        void bestandNull_wirftException() {
            Article article = createArticle(0, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENAUSGANG, 1);

            assertThatThrownBy(() -> service.processTransaction(article, input))
                    .isInstanceOf(InsufficientStockException.class);
        }
    }

    // ========================================================================
    // 4. NACHBESTELLUNG (aus FUNKTION NachbestellungPruefen)
    //    WENN AktuellerBestand < Mindestbestand → Nachbestellung noetig
    //    Bestellmenge = max(Nachbestellmenge, Nachbestellmenge - AktuellerBestand)
    // ========================================================================
    @Nested
    @DisplayName("Nachbestellung")
    class Nachbestellung {

        @Test
        @DisplayName("Bestand unter Mindestbestand → Nachbestellung noetig")
        void bestandUnterMinimum_nachbestellungNoetig() {
            // Bestand=100, Ausgang=95 → neuer Bestand=5, Mindestbestand=10
            Article article = createArticle(100, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENAUSGANG, 95);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.newStock()).isEqualTo(5);
            assertThat(result.reorderNeeded()).isTrue();
        }

        @Test
        @DisplayName("Bestand ueber Mindestbestand → keine Nachbestellung")
        void bestandUeberMinimum_keineNachbestellung() {
            Article article = createArticle(100, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENAUSGANG, 10);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.newStock()).isEqualTo(90);
            assertThat(result.reorderNeeded()).isFalse();
            assertThat(result.orderQuantity()).isEqualTo(0);
        }

        @Test
        @DisplayName("Bestand = Mindestbestand → keine Nachbestellung (nicht < sondern =)")
        void bestandGleichMinimum_keineNachbestellung() {
            // Bestand=20, Ausgang=10 → neuer Bestand=10, Mindestbestand=10
            Article article = createArticle(20, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENAUSGANG, 10);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.newStock()).isEqualTo(10);
            assertThat(result.reorderNeeded()).isFalse();
        }

        @Test
        @DisplayName("Bestellmenge ist mindestens die Standard-Nachbestellmenge")
        void bestellmengeMinDestensNachbestellmenge() {
            // Pseudocode: Bestellmenge = Nachbestellmenge - AktuellerBestand
            //             WENN Bestellmenge < Nachbestellmenge → Bestellmenge = Nachbestellmenge
            // Da (Nachbestellmenge - AktuellerBestand) immer < Nachbestellmenge wenn Bestand > 0,
            // wird Bestellmenge = Nachbestellmenge gesetzt
            Article article = createArticle(15, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENAUSGANG, 10);

            InventoryResult result = service.processTransaction(article, input);

            // neuer Bestand = 5, unter Mindestbestand 10
            assertThat(result.reorderNeeded()).isTrue();
            assertThat(result.orderQuantity()).isEqualTo(50); // mindestens Nachbestellmenge
        }

        @Test
        @DisplayName("Bestand = 0 nach Ausgang → Bestellmenge = Nachbestellmenge")
        void bestandNull_bestellmengeIstNachbestellmenge() {
            Article article = createArticle(5, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENAUSGANG, 5);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.newStock()).isEqualTo(0);
            assertThat(result.reorderNeeded()).isTrue();
            // Nachbestellmenge - 0 = 50, nicht < 50, also Bestellmenge = 50
            assertThat(result.orderQuantity()).isEqualTo(50);
        }

        @Test
        @DisplayName("Wareneingang der Bestand ueber Minimum hebt → keine Nachbestellung")
        void wareneingangUeberMinimum_keineNachbestellung() {
            Article article = createArticle(5, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENEINGANG, 100);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.newStock()).isEqualTo(105);
            assertThat(result.reorderNeeded()).isFalse();
            assertThat(result.orderQuantity()).isEqualTo(0);
        }

        @Test
        @DisplayName("Wareneingang bei Bestand unter Minimum → Nachbestellung noetig wenn weiterhin unter Minimum")
        void wareneingangAbertNochUnterMinimum_nachbestellungNoetig() {
            Article article = createArticle(2, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENEINGANG, 3);

            InventoryResult result = service.processTransaction(article, input);

            // neuer Bestand = 5, immer noch < 10
            assertThat(result.newStock()).isEqualTo(5);
            assertThat(result.reorderNeeded()).isTrue();
        }
    }

    // ========================================================================
    // 5. ERGEBNIS-CODE (aus Hauptlogik)
    // ========================================================================
    @Nested
    @DisplayName("Ergebnis-Code")
    class ErgebnisCode {

        @Test
        @DisplayName("Erfolgreicher Wareneingang → ResultCode.ERFOLG")
        void erfolgreich_codeErfolg() {
            Article article = createArticle(100, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENEINGANG, 10);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.resultCode()).isEqualTo(ResultCode.ERFOLG);
            assertThat(result.resultCode().getCode()).isEqualTo("OK");
        }

        @Test
        @DisplayName("Erfolgreicher Warenausgang → ResultCode.ERFOLG")
        void erfolgreichAusgang_codeErfolg() {
            Article article = createArticle(100, 10, 50);
            TransactionInput input = new TransactionInput(TransactionType.WARENAUSGANG, 10);

            InventoryResult result = service.processTransaction(article, input);

            assertThat(result.resultCode()).isEqualTo(ResultCode.ERFOLG);
        }
    }
}
