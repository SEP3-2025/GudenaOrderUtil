package com.gudena.payment;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.YearMonth;

public class PaymentServer {

    private final int port;

    public PaymentServer(int port) {
        this.port = port;
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

            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            if (bytesRead == -1) return;

            String message = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            System.out.println("Received: " + message);

            String[] parts = message.split("\\|");
            String result = "INVALID";

            if (parts.length == 6) {
                String cardNumber = parts[0];
                String firstName = parts[1];
                String lastName = parts[2];
                String cvv = parts[3];
                int month = Integer.parseInt(parts[4]);
                int year = Integer.parseInt(parts[5]);

                CardValidator validator = new CardValidator();
                boolean valid = validator.validateCard(cardNumber, firstName, lastName, cvv, YearMonth.of(year, month));
                result = valid ? "VALID" : "INVALID";
            }

            outputStream.write(result.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            System.out.println("Response sent: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
