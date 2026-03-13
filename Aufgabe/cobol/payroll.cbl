      *================================================================
      * PAYROLL.CBL - Gehaltsberechnung
      * Zweck: Berechnet Nettogehalt aus Bruttogehalt
      *        abzueglich Steuer und Sozialabgaben
      *================================================================
       IDENTIFICATION DIVISION.
       PROGRAM-ID. PAYROLL.
       AUTHOR. SCHULUNGSBEISPIEL.

       DATA DIVISION.
       WORKING-STORAGE SECTION.

      *----------------------------------------------------------------
      * Eingabedaten (Input)
      *----------------------------------------------------------------
       01 WS-INPUT.
          05 WS-EMPLOYEE-ID     PIC 9(6).
          05 WS-GROSS-SALARY    PIC 9(7)V99.
          05 WS-TAX-RATE        PIC 9(2)V99.
          05 WS-SOCIAL-RATE     PIC 9(2)V99.
          05 WS-BONUS           PIC 9(5)V99.

      *----------------------------------------------------------------
      * Berechnungsvariablen (intern)
      *----------------------------------------------------------------
       01 WS-CALC.
          05 WS-TAXABLE-BASE    PIC 9(7)V99.
          05 WS-TAX-AMOUNT      PIC 9(7)V99.
          05 WS-SOCIAL-AMOUNT   PIC 9(7)V99.
          05 WS-TOTAL-DEDUCT    PIC 9(7)V99.

      *----------------------------------------------------------------
      * Ausgabedaten (Output)
      *----------------------------------------------------------------
       01 WS-OUTPUT.
          05 WS-NET-SALARY      PIC 9(7)V99.
          05 WS-TAX-PAID        PIC 9(7)V99.
          05 WS-SOCIAL-PAID     PIC 9(7)V99.
          05 WS-STATUS-CODE     PIC X(2).
             88 WS-SUCCESS      VALUE 'OK'.
             88 WS-ERROR        VALUE 'ER'.

      *================================================================
       PROCEDURE DIVISION.

       MAIN-LOGIC.
           PERFORM VALIDATE-INPUT
           IF WS-SUCCESS
               PERFORM CALCULATE-TAXABLE-BASE
               PERFORM CALCULATE-DEDUCTIONS
               PERFORM CALCULATE-NET-SALARY
               MOVE 'OK' TO WS-STATUS-CODE
           END-IF
           STOP RUN.

      *----------------------------------------------------------------
       VALIDATE-INPUT.
           IF WS-GROSS-SALARY <= ZERO
               MOVE 'ER' TO WS-STATUS-CODE
           ELSE IF WS-TAX-RATE > 50
               MOVE 'ER' TO WS-STATUS-CODE
           ELSE IF WS-SOCIAL-RATE > 30
               MOVE 'ER' TO WS-STATUS-CODE
           ELSE
               MOVE 'OK' TO WS-STATUS-CODE
           END-IF.

      *----------------------------------------------------------------
       CALCULATE-TAXABLE-BASE.
           COMPUTE WS-TAXABLE-BASE =
               WS-GROSS-SALARY + WS-BONUS.

      *----------------------------------------------------------------
       CALCULATE-DEDUCTIONS.
           COMPUTE WS-TAX-AMOUNT ROUNDED =
               WS-TAXABLE-BASE * WS-TAX-RATE / 100.
           COMPUTE WS-SOCIAL-AMOUNT ROUNDED =
               WS-GROSS-SALARY * WS-SOCIAL-RATE / 100.
           COMPUTE WS-TOTAL-DEDUCT =
               WS-TAX-AMOUNT + WS-SOCIAL-AMOUNT.

      *----------------------------------------------------------------
       CALCULATE-NET-SALARY.
           COMPUTE WS-NET-SALARY =
               WS-TAXABLE-BASE - WS-TOTAL-DEDUCT.
           MOVE WS-TAX-AMOUNT    TO WS-TAX-PAID.
           MOVE WS-SOCIAL-AMOUNT TO WS-SOCIAL-PAID.
