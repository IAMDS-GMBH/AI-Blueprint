package com.inventory.service;

import com.inventory.exception.InsufficientStockException;
import com.inventory.exception.InvalidTransactionException;
import com.inventory.model.Article;
import com.inventory.model.InventoryResult;
import com.inventory.model.ResultCode;
import com.inventory.model.TransactionInput;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementierung der Lagerverwaltung (aus COBOL: inventory.cbl).
 */
@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    @Override
    public InventoryResult processTransaction(Article article, TransactionInput input) {
        validateTransaction(input);

        int newStock = switch (input.transactionType()) {
            case WARENEINGANG -> processInbound(article, input);
            case WARENAUSGANG -> processOutbound(article, input);
        };

        return checkReorder(article, newStock);
    }

    private void validateTransaction(TransactionInput input) {
        if (input.quantity() <= 0) {
            throw new InvalidTransactionException(
                    "Buchungsmenge muss positiv sein, war: " + input.quantity());
        }
        if (input.transactionType() == null) {
            throw new InvalidTransactionException("Buchungstyp darf nicht null sein");
        }
    }

    private int processInbound(Article article, TransactionInput input) {
        int newStock = article.currentStock() + input.quantity();
        log.info("Wareneingang: Artikel {} — Bestand {} + {} = {}",
                article.articleId(), article.currentStock(), input.quantity(), newStock);
        return newStock;
    }

    private int processOutbound(Article article, TransactionInput input) {
        if (input.quantity() > article.currentStock()) {
            throw new InsufficientStockException(
                    "Bestand unzureichend: verfuegbar=%d, angefordert=%d"
                            .formatted(article.currentStock(), input.quantity()));
        }
        int newStock = article.currentStock() - input.quantity();
        log.info("Warenausgang: Artikel {} — Bestand {} - {} = {}",
                article.articleId(), article.currentStock(), input.quantity(), newStock);
        return newStock;
    }

    private InventoryResult checkReorder(Article article, int newStock) {
        if (newStock < article.minStock()) {
            int orderQuantity = article.reorderQuantity() - newStock;
            if (orderQuantity < article.reorderQuantity()) {
                orderQuantity = article.reorderQuantity();
            }
            log.info("Nachbestellung noetig: Artikel {} — Bestand {} < Minimum {}, Bestellmenge {}",
                    article.articleId(), newStock, article.minStock(), orderQuantity);
            return new InventoryResult(newStock, true, orderQuantity, ResultCode.ERFOLG);
        }
        return new InventoryResult(newStock, false, 0, ResultCode.ERFOLG);
    }
}
