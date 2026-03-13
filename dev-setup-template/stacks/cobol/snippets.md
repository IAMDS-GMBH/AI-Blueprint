# COBOL → Java Migration — Patterns

## COBOL Working-Storage → Java DTO (DO)
```cobol
       01 WS-CUSTOMER.
          05 WS-CUST-ID        PIC 9(10).
          05 WS-CUST-NAME      PIC X(50).
          05 WS-CUST-BALANCE   PIC 9(13)V99.
          05 WS-CUST-STATUS    PIC X(1).
             88 CUST-ACTIVE    VALUE 'A'.
             88 CUST-INACTIVE  VALUE 'I'.
```

```java
public record CustomerDTO(
    long customerId,         // PIC 9(10)
    String customerName,     // PIC X(50)
    BigDecimal balance,      // PIC 9(13)V99 → NEVER double
    CustomerStatus status    // 88-Level → Enum
) {}

public enum CustomerStatus {
    ACTIVE('A'), INACTIVE('I');

    private final char code;
    CustomerStatus(char code) { this.code = code; }
}
```

## COBOL PERFORM → Java Method (DO)
```cobol
       PERFORM CALCULATE-DISCOUNT
       PERFORM APPLY-TAX
       PERFORM WRITE-ORDER-RECORD

       CALCULATE-DISCOUNT.
           IF WS-ORDER-TOTAL > 1000
               COMPUTE WS-DISCOUNT = WS-ORDER-TOTAL * 0.1
           ELSE
               MOVE 0 TO WS-DISCOUNT
           END-IF.
```

```java
public OrderResult processOrder(Order order) {
    BigDecimal discount = calculateDiscount(order.getTotal());
    BigDecimal tax = applyTax(order.getTotal().subtract(discount));
    return writeOrderRecord(order, discount, tax);
}

private BigDecimal calculateDiscount(BigDecimal total) {
    if (total.compareTo(new BigDecimal("1000")) > 0) {
        return total.multiply(new BigDecimal("0.1"));
    }
    return BigDecimal.ZERO;
}
```

## OCCURS → Java List (DO)
```cobol
       01 WS-ORDER-LINES.
          05 WS-LINE-ITEM OCCURS 50 TIMES.
             10 WS-ITEM-CODE   PIC X(10).
             10 WS-QUANTITY    PIC 9(5).
             10 WS-UNIT-PRICE  PIC 9(7)V99.
```

```java
public record OrderLine(
    String itemCode,
    int quantity,
    BigDecimal unitPrice
) {}

// List statt fixed-size Array
List<OrderLine> orderLines = new ArrayList<>();
```

## Anti-Pattern: God Class (DON'T)
```java
// FALSCH — 1:1 COBOL-Programm als eine Klasse
public class CobolProgram {
    // 200 Felder aus WORKING-STORAGE
    private int wsField1;
    private String wsField2;
    // ... 198 weitere

    // 1 riesige Methode fuer PROCEDURE DIVISION
    public void execute() { /* 500 Zeilen */ }
}

// RICHTIG — Aufteilen in Domain-Klassen + Services
```
