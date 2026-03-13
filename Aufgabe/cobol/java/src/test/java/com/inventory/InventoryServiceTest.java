package com.inventory;

import com.inventory.exception.InsufficientStockException;
import com.inventory.exception.InvalidTransactionException;
import com.inventory.model.Article;
import com.inventory.model.InventoryResult;
import com.inventory.model.ResultCode;
import com.inventory.model.TransactionInput;
import com.inventory.model.TransactionType;
import com.inventory.service.InventoryService;
import com.inventory.service.InventoryServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InventoryServiceTest {

    private static final Article SAMPLE_ARTICLE = new Article(
            10001000,
            "Schraubensatz M8",
            100,
            20,
            50,
            BigDecimal.valueOf(12.50)
    );

    private InventoryService service;

    @BeforeEach
    void setUp() {
        service = new InventoryServiceImpl();
    }

    // --- Happy Path: Wareneingang ---

    @Test
    void shouldIncreaseStockOnInbound() {
        var input = new TransactionInput(TransactionType.WARENEINGANG, 30);

        var result = service.processTransaction(SAMPLE_ARTICLE, input);

        assertThat(result.resultCode()).isEqualTo(ResultCode.ERFOLG);
        assertThat(result.newStock()).isEqualTo(130);
        assertThat(result.reorderNeeded()).isFalse();
        assertThat(result.orderQuantity()).isZero();
    }

    // --- Happy Path: Warenausgang ---

    @Test
    void shouldDecreaseStockOnOutbound() {
        var input = new TransactionInput(TransactionType.WARENAUSGANG, 30);

        var result = service.processTransaction(SAMPLE_ARTICLE, input);

        assertThat(result.resultCode()).isEqualTo(ResultCode.ERFOLG);
        assertThat(result.newStock()).isEqualTo(70);
        assertThat(result.reorderNeeded()).isFalse();
    }

    // --- Nachbestellung ---

    @Test
    void shouldTriggerReorderWhenStockBelowMinimum() {
        var article = SAMPLE_ARTICLE.withCurrentStock(25);
        var input = new TransactionInput(TransactionType.WARENAUSGANG, 10);

        var result = service.processTransaction(article, input);

        assertThat(result.resultCode()).isEqualTo(ResultCode.ERFOLG);
        assertThat(result.newStock()).isEqualTo(15);
        assertThat(result.reorderNeeded()).isTrue();
        assertThat(result.orderQuantity()).isEqualTo(50);
    }

    @Test
    void shouldNotTriggerReorderWhenStockEqualsMinimum() {
        var article = SAMPLE_ARTICLE.withCurrentStock(50);
        var input = new TransactionInput(TransactionType.WARENAUSGANG, 30);

        var result = service.processTransaction(article, input);

        assertThat(result.reorderNeeded()).isFalse();
        assertThat(result.orderQuantity()).isZero();
    }

    @Test
    void shouldTriggerReorderOnInboundWhenStockStillBelowMinimum() {
        var article = SAMPLE_ARTICLE.withCurrentStock(5);
        var input = new TransactionInput(TransactionType.WARENEINGANG, 10);

        var result = service.processTransaction(article, input);

        assertThat(result.newStock()).isEqualTo(15);
        assertThat(result.reorderNeeded()).isTrue();
        assertThat(result.orderQuantity()).isEqualTo(50);
    }

    // --- Validierungsfehler ---

    @Test
    void shouldThrowWhenQuantityIsZero() {
        var input = new TransactionInput(TransactionType.WARENEINGANG, 0);

        assertThatThrownBy(() -> service.processTransaction(SAMPLE_ARTICLE, input))
                .isInstanceOf(InvalidTransactionException.class)
                .hasMessageContaining("positiv");
    }

    @Test
    void shouldThrowWhenQuantityIsNegative() {
        var input = new TransactionInput(TransactionType.WARENAUSGANG, -5);

        assertThatThrownBy(() -> service.processTransaction(SAMPLE_ARTICLE, input))
                .isInstanceOf(InvalidTransactionException.class);
    }

    @Test
    void shouldThrowWhenTransactionTypeIsNull() {
        var input = new TransactionInput(null, 10);

        assertThatThrownBy(() -> service.processTransaction(SAMPLE_ARTICLE, input))
                .isInstanceOf(InvalidTransactionException.class)
                .hasMessageContaining("null");
    }

    // --- Bestand unzureichend ---

    @Test
    void shouldThrowWhenOutboundExceedsStock() {
        var input = new TransactionInput(TransactionType.WARENAUSGANG, 150);

        assertThatThrownBy(() -> service.processTransaction(SAMPLE_ARTICLE, input))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("unzureichend");
    }

    // --- Grenzwerte ---

    @Test
    void shouldAllowOutboundOfExactStock() {
        var input = new TransactionInput(TransactionType.WARENAUSGANG, 100);

        var result = service.processTransaction(SAMPLE_ARTICLE, input);

        assertThat(result.newStock()).isZero();
        assertThat(result.reorderNeeded()).isTrue();
        assertThat(result.orderQuantity()).isEqualTo(50);
    }

    @Test
    void shouldHandleSingleUnitInbound() {
        var input = new TransactionInput(TransactionType.WARENEINGANG, 1);

        var result = service.processTransaction(SAMPLE_ARTICLE, input);

        assertThat(result.newStock()).isEqualTo(101);
    }
}
