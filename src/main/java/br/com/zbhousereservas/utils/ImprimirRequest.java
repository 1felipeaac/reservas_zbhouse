package br.com.zbhousereservas.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public abstract class ImprimirRequest {
    public static Map<String, String> execute(HttpServletRequest request) {
        Map<String, String> headersMap = new HashMap<>();

        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request URI: " + request.getRequestURI());

        // Imprimindo os cabeçalhos da requisição e adicionando ao mapa
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            System.out.println("Request Headers:");
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                System.out.println(headerName + ": " + headerValue);
                headersMap.put(headerName, headerValue);
            }
        }
        return headersMap;
    }
}
