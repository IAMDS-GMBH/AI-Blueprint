package com.chatbot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolDefinition {

    private ToolDefinition() {
        // Utility class
    }

    public static List<Map<String, Object>> getToolDefinitions() {
        List<Map<String, Object>> tools = new ArrayList<>();

        tools.add(buildTool(
                "list-tables",
                "Listet alle verfuegbaren Tabellen in der Datenbank auf",
                Map.of("type", "object", "properties", Map.of())
        ));

        tools.add(buildTool(
                "describe-table",
                "Zeigt die Spalten und Datentypen einer bestimmten Tabelle",
                buildParameters(
                        Map.of("tableName", stringParam("Name der Tabelle")),
                        List.of("tableName")
                )
        ));

        tools.add(buildTool(
                "count-rows",
                "Zaehlt die Zeilen einer Tabelle, optional mit WHERE-Bedingung",
                buildParameters(
                        Map.of(
                                "tableName", stringParam("Name der Tabelle"),
                                "whereClause", stringParam("Optionale WHERE-Bedingung (ohne WHERE-Keyword)")
                        ),
                        List.of("tableName")
                )
        ));

        Map<String, Object> queryProperties = new HashMap<>();
        queryProperties.put("tableName", stringParam("Name der Tabelle"));
        queryProperties.put("columns", Map.of(
                "type", "array",
                "items", Map.of("type", "string"),
                "description", "Liste der Spalten (leer = alle)"
        ));
        queryProperties.put("whereClause", stringParam("Optionale WHERE-Bedingung (ohne WHERE-Keyword)"));
        queryProperties.put("limit", Map.of(
                "type", "integer",
                "description", "Maximale Anzahl Zeilen (Standard: 50)",
                "default", 50
        ));
        tools.add(buildTool(
                "query-table",
                "Fuehrt eine SELECT-Abfrage auf einer Tabelle aus",
                buildParameters(queryProperties, List.of("tableName"))
        ));

        return tools;
    }

    private static Map<String, Object> buildTool(String name, String description, Map<String, Object> parameters) {
        Map<String, Object> function = new HashMap<>();
        function.put("name", name);
        function.put("description", description);
        function.put("parameters", parameters);

        Map<String, Object> tool = new HashMap<>();
        tool.put("type", "function");
        tool.put("function", function);
        return tool;
    }

    private static Map<String, Object> stringParam(String description) {
        Map<String, Object> param = new HashMap<>();
        param.put("type", "string");
        param.put("description", description);
        return param;
    }

    private static Map<String, Object> buildParameters(Map<String, Object> properties, List<String> required) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", "object");
        params.put("properties", properties);
        params.put("required", required);
        return params;
    }
}
