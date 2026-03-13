package com.inventory.model;

import java.math.BigDecimal;

/**
 * Artikel-Datensatz — entspricht einer DB-Zeile (aus COBOL: WS-ARTICLE-*).
 */
public record Article(
        int articleId,
        String articleName,
        int currentStock,
        int minStock,
        int reorderQuantity,
        BigDecimal unitPrice
) {

    /**
     * Erzeugt eine Kopie mit aktualisiertem Bestand.
     */
    public Article withCurrentStock(int newStock) {
        return new Article(articleId, articleName, newStock, minStock, reorderQuantity, unitPrice);
    }
}
