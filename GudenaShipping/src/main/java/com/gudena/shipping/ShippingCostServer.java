package com.gudena.shipping;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ShippingCostServer {
    private final int port;
    private final static double BASIC_COST = 5;

    public ShippingCostServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Shipping server is running on 5001 port...");
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
            if (bytesRead == -1) {
                return; // client closed connection
            }

            String message = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            System.out.println("Received: " + message);

            String[] parts = message.split("\\|");
            if (parts.length == 4) {
                String country1 = parts[0];
                String postalCode1 = parts[1];
                String country2 = parts[2];
                String postalCode2 = parts[3];

                LocationDistanceCalculator calculator = new LocationDistanceCalculator();
                try {
                    double distance = calculator.getDistanceBetweenPostalCodes(country1, postalCode1, country2, postalCode2);
                    double shippingCost = distance < 750 ? BASIC_COST : BASIC_COST * 1.25;
                    byte[] responseBytes = String.valueOf(shippingCost).getBytes(StandardCharsets.UTF_8);
                    outputStream.write(responseBytes);
                    outputStream.flush();

                    System.out.println("Response sent.");
                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
