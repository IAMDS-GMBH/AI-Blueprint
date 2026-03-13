      *================================================================
      * INVENTORY.CBL - Lagerverwaltung
      * Zweck: Verwaltet Lagerbestand, prueft Mindestbestand
      *        und loest Nachbestellung aus wenn noetig
      *================================================================
       IDENTIFICATION DIVISION.
       PROGRAM-ID. INVENTORY.
       AUTHOR. SCHULUNGSBEISPIEL.

       DATA DIVISION.
       WORKING-STORAGE SECTION.

      *----------------------------------------------------------------
      * Artikel-Datensatz (entspricht einer DB-Zeile / Entity)
      *----------------------------------------------------------------
       01 WS-ARTICLE.
          05 WS-ARTICLE-ID      PIC 9(8).
          05 WS-ARTICLE-NAME    PIC X(50).
          05 WS-CURRENT-STOCK   PIC 9(6).
          05 WS-MIN-STOCK       PIC 9(6).
          05 WS-REORDER-QTY     PIC 9(6).
          05 WS-UNIT-PRICE      PIC 9(5)V99.

      *----------------------------------------------------------------
      * Buchungs-Eingabe
      *----------------------------------------------------------------
       01 WS-TRANSACTION.
          05 WS-TRANS-TYPE      PIC X(1).
             88 WS-INBOUND      VALUE 'I'.
             88 WS-OUTBOUND     VALUE 'O'.
          05 WS-TRANS-QTY       PIC 9(6).

      *----------------------------------------------------------------
      * Ergebnis
      *----------------------------------------------------------------
       01 WS-RESULT.
          05 WS-NEW-STOCK       PIC 9(6).
          05 WS-ORDER-NEEDED    PIC X(1).
             88 WS-ORDER-YES    VALUE 'Y'.
             88 WS-ORDER-NO     VALUE 'N'.
          05 WS-ORDER-QTY       PIC 9(6).
          05 WS-RESULT-CODE     PIC X(2).
             88 WS-SUCCESS      VALUE 'OK'.
             88 WS-INSUFF-STOCK VALUE 'IS'.
             88 WS-INVALID-TRANS VALUE 'IT'.

      *================================================================
       PROCEDURE DIVISION.

       MAIN-LOGIC.
           PERFORM VALIDATE-TRANSACTION
           IF WS-SUCCESS
               EVALUATE TRUE
                   WHEN WS-INBOUND
                       PERFORM PROCESS-INBOUND
                   WHEN WS-OUTBOUND
                       PERFORM PROCESS-OUTBOUND
               END-EVALUATE
               PERFORM CHECK-REORDER
           END-IF
           STOP RUN.

      *----------------------------------------------------------------
       VALIDATE-TRANSACTION.
           IF WS-TRANS-QTY <= ZERO
               MOVE 'IT' TO WS-RESULT-CODE
           ELSE IF NOT (WS-INBOUND OR WS-OUTBOUND)
               MOVE 'IT' TO WS-RESULT-CODE
           ELSE
               MOVE 'OK' TO WS-RESULT-CODE
           END-IF.

      *----------------------------------------------------------------
       PROCESS-INBOUND.
           COMPUTE WS-NEW-STOCK =
               WS-CURRENT-STOCK + WS-TRANS-QTY
           MOVE WS-NEW-STOCK TO WS-CURRENT-STOCK
           MOVE 'OK' TO WS-RESULT-CODE.

      *----------------------------------------------------------------
       PROCESS-OUTBOUND.
           IF WS-TRANS-QTY > WS-CURRENT-STOCK
               MOVE 'IS' TO WS-RESULT-CODE
           ELSE
               COMPUTE WS-NEW-STOCK =
                   WS-CURRENT-STOCK - WS-TRANS-QTY
               MOVE WS-NEW-STOCK TO WS-CURRENT-STOCK
               MOVE 'OK' TO WS-RESULT-CODE
           END-IF.

      *----------------------------------------------------------------
       CHECK-REORDER.
           IF WS-CURRENT-STOCK < WS-MIN-STOCK
               MOVE 'Y' TO WS-ORDER-NEEDED
               COMPUTE WS-ORDER-QTY =
                   WS-REORDER-QTY - WS-CURRENT-STOCK
               IF WS-ORDER-QTY < WS-REORDER-QTY
                   MOVE WS-REORDER-QTY TO WS-ORDER-QTY
               END-IF
           ELSE
               MOVE 'N' TO WS-ORDER-NEEDED
               MOVE ZERO TO WS-ORDER-QTY
           END-IF.
