package com.pablojvm.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;

public class LoginHandler implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestMethod = exchange.getRequestMethod();

        String requestBody = getRequestBody(exchange);

        DataPostUser dataPostUser = objectMapper.readValue(
                requestBody,
                new TypeReference<DataPostUser>()
                {
                }
        );

        System.out.println(dataPostUser);

    }

    /**
     * Convert the body request from JSON format to string format.
     *
     * @param exchange A {@link HttpExchange} with the HTTP method data.
     * @return The body of the request in string format.
     * @throws IOException if there is an error reading the body
     */
    private String getRequestBody(HttpExchange exchange) throws IOException
    {
        InputStream requestBody = exchange.getRequestBody();
        StringBuilder stringBuilder = new StringBuilder();

        int i;
        while ((i = requestBody.read()) != -1)
        {
            stringBuilder.append((char) i);
        }

        return stringBuilder.toString();
    }
}
