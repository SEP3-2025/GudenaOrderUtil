package com.gudena.payment;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;

public class PaymentServer {

    private final int port;
    private final ObjectMapper objectMapper;

    public PaymentServer(int port) {
        this.port = port;
        this.objectMapper = new ObjectMapper();
    }

    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Payment server running on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");
                new Thread(() -> handleClient(clientSocket)).start();
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (clientSocket;
             InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {

            byte[] buffer = new byte[2048];
            int bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) return;

            String jsonMessage = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            System.out.println("Received JSON: " + jsonMessage);

            // Deserialize JSON to CreditCardDto
            CreditCardDto creditCardDto = objectMapper.readValue(jsonMessage, CreditCardDto.class);

            String result = "DECLINED";

            if (creditCardDto != null &&
                creditCardDto.getCardNumber() != null &&
                creditCardDto.getHolderFullName() != null &&
                creditCardDto.getCvvCode() != null &&
                creditCardDto.getExpiryDate() != null) {

                String[] nameParts = creditCardDto.getHolderFullName().split(" ", 2);
                String firstName = nameParts.length > 0 ? nameParts[0] : "";
                String lastName = nameParts.length > 1 ? nameParts[1] : "";

                // Parse expiry date (MM/yy)
                String[] expiryParts = creditCardDto.getExpiryDate().split("/");
                int month = Integer.parseInt(expiryParts[0]);
                int year = 2000 + Integer.parseInt(expiryParts[1]);

                CardValidator validator = new CardValidator();
                boolean valid = validator.validateCard(
                        creditCardDto.getCardNumber(),
                        firstName,
                        lastName,
                        creditCardDto.getCvvCode(),
                        YearMonth.of(year, month)
                );

                result = valid ? "APPROVED" : "DECLINED";
            }

            outputStream.write(result.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            //System.out.println("Response sent: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
