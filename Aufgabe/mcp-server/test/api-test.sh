#!/bin/bash
# API-Tests fuer MCP HTTP-Wrapper
BASE_URL="http://localhost:3001"
API_KEY="schulung-secret-key-2026"
PASS=0
FAIL=0

test_case() {
  local name="$1" expected="$2" actual="$3"
  if echo "$actual" | grep -q "$expected"; then
    echo "  PASS: $name"
    PASS=$((PASS + 1))
  else
    echo "  FAIL: $name (expected: $expected)"
    echo "        got: $actual"
    FAIL=$((FAIL + 1))
  fi
}

AUTH="Authorization: Bearer $API_KEY"

echo "================================"
echo "MCP HTTP-Wrapper API Tests"
echo "================================"
echo ""

# --- Functional Tests ---
echo "--- Functional Tests ---"
echo ""

# 1. Health Check
RESULT=$(curl -s "$BASE_URL/api/health")
test_case "GET /api/health" "ok" "$RESULT"

# 2. List Tools
RESULT=$(curl -s -H "$AUTH" "$BASE_URL/api/tools")
test_case "GET /api/tools enthaelt list-tables" "list-tables" "$RESULT"

# 3. List Tables
RESULT=$(curl -s -X POST -H "$AUTH" -H "Content-Type: application/json" "$BASE_URL/api/tools/list-tables")
test_case "POST /api/tools/list-tables enthaelt kunden" "kunden" "$RESULT"

# 4. Describe Table
RESULT=$(curl -s -X POST -H "$AUTH" -H "Content-Type: application/json" \
  -d '{"arguments":{"tableName":"kunden"}}' \
  "$BASE_URL/api/tools/describe-table")
test_case "POST /api/tools/describe-table (kunden)" "column" "$RESULT"

# 5. Get Schema
RESULT=$(curl -s -X POST -H "$AUTH" -H "Content-Type: application/json" "$BASE_URL/api/tools/get-schema")
test_case "POST /api/tools/get-schema enthaelt kunden" "kunden" "$RESULT"

# 6. Count Rows
RESULT=$(curl -s -X POST -H "$AUTH" -H "Content-Type: application/json" \
  -d '{"arguments":{"tableName":"kunden"}}' \
  "$BASE_URL/api/tools/count-rows")
test_case "POST /api/tools/count-rows (kunden) = 20" "20" "$RESULT"

# 7. Query Table
RESULT=$(curl -s -X POST -H "$AUTH" -H "Content-Type: application/json" \
  -d '{"arguments":{"tableName":"kunden","limit":3}}' \
  "$BASE_URL/api/tools/query-table")
test_case "POST /api/tools/query-table (kunden, limit 3)" "result" "$RESULT"

# 8. Query Users
RESULT=$(curl -s -X POST -H "$AUTH" -H "Content-Type: application/json" \
  -d '{"arguments":{"limit":5}}' \
  "$BASE_URL/api/tools/query-users")
test_case "POST /api/tools/query-users (limit 5)" "result" "$RESULT"

# 9. Get User by Email
RESULT=$(curl -s -X POST -H "$AUTH" -H "Content-Type: application/json" \
  -d '{"arguments":{"email":"test@test.de"}}' \
  "$BASE_URL/api/tools/get-user-by-email")
test_case "POST /api/tools/get-user-by-email" "result" "$RESULT"

echo ""
echo "--- Security Tests ---"
echo ""

# 10. SQL Injection
RESULT=$(curl -s -X POST -H "$AUTH" -H "Content-Type: application/json" \
  -d '{"arguments":{"tableName":"kunden","whereClause":"1=1; DROP TABLE kunden"}}' \
  "$BASE_URL/api/tools/count-rows")
test_case "SQL-Injection blocked" "error" "$RESULT"

# 11. Ohne API-Key
RESULT=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/api/tools")
test_case "Ohne API-Key ergibt 401" "401" "$RESULT"

# 12. Falscher API-Key
RESULT=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer wrong-key" "$BASE_URL/api/tools")
test_case "Falscher API-Key ergibt 401" "401" "$RESULT"

# 13. Unbekanntes Tool
RESULT=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "$AUTH" -H "Content-Type: application/json" \
  "$BASE_URL/api/tools/nonexistent")
test_case "Unbekanntes Tool ergibt 404" "404" "$RESULT"

echo ""
echo "================================"
echo "Ergebnis: $PASS bestanden, $FAIL fehlgeschlagen von $((PASS + FAIL)) Tests"
echo "================================"
