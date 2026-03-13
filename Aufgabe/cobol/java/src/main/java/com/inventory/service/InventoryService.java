package com.inventory.service;

import com.inventory.model.Article;
import com.inventory.model.InventoryResult;
import com.inventory.model.TransactionInput;

/**
 * Service-Interface fuer Lagerverwaltung (aus COBOL: PROGRAMM INVENTORY).
 */
public interface InventoryService {

    /**
     * Verarbeitet eine Lagerbewegung (Wareneingang oder Warenausgang).
     *
     * @param article Artikel-Datensatz mit aktuellem Bestand
     * @param input   Buchungs-Eingabe (Typ + Menge)
     * @return Ergebnis mit neuem Bestand und ggf. Nachbestellinfo
     * @throws com.inventory.exception.InvalidTransactionException bei ungueltiger Buchung
     * @throws com.inventory.exception.InsufficientStockException  bei unzureichendem Bestand
     */
    InventoryResult processTransaction(Article article, TransactionInput input);
}
