# PostgreSQL — Patterns & Anti-Patterns

## Table mit Identity + Constraints (DO)
```sql
-- V001__create_customers_table.sql
CREATE TABLE customers (
    customer_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    full_name   VARCHAR(200) NOT NULL,
    status      VARCHAR(20) DEFAULT 'active' NOT NULL CHECK (status IN ('active','inactive','blocked')),
    metadata    JSONB DEFAULT '{}',
    created_at  TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

-- Rollback: DROP TABLE customers;
```

## Foreign Key mit Index (DO)
```sql
-- V002__create_orders_table.sql
CREATE TABLE orders (
    order_id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    customer_id BIGINT NOT NULL REFERENCES customers(customer_id),
    total       NUMERIC(19,4) NOT NULL,
    created_at  TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_orders_customer_id ON orders(customer_id);

-- Rollback: DROP TABLE orders;
```

## JSONB Query (DO)
```sql
-- JSONB indizieren
CREATE INDEX idx_customers_metadata ON customers USING GIN (metadata);

-- JSONB abfragen
SELECT customer_id, email
FROM customers
WHERE metadata @> '{"tier": "premium"}';
```

## Pagination (DO)
```sql
SELECT customer_id, email, full_name
FROM customers
WHERE status = 'active'
ORDER BY created_at DESC
LIMIT :pageSize OFFSET :offset;
```
