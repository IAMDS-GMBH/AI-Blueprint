package com.inventory.model;

/**
 * Verarbeitungsresultat einer Lagerbewegung (aus COBOL: WS-NEW-STOCK, WS-ORDER-NEEDED, etc.).
 */
public record InventoryResult(
        int newStock,
        boolean reorderNeeded,
        int orderQuantity,
        ResultCode resultCode
) {
}
