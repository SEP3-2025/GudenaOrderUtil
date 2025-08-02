package com.gudena.shipping;

import com.fasterxml.jackson.databind.ObjectMapper;
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
            System.out.println("Shipping server is running on port " + port + "...");
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

            String json = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            System.out.println("Received JSON: " + json);

            ObjectMapper mapper = new ObjectMapper();
            LocationsShippingsDto dto = mapper.readValue(json, LocationsShippingsDto.class);


            String originCountryCode = EUCountryCodeMap.containsCountry(dto.getOriginCountry())
                    ? EUCountryCodeMap.getCode(dto.getOriginCountry())
                    : dto.getOriginCountry();

            String destinationCountryCode = EUCountryCodeMap.containsCountry(dto.getDestinationCountry())
                    ? EUCountryCodeMap.getCode(dto.getDestinationCountry())
                    : dto.getDestinationCountry();

            LocationDistanceCalculator calculator = new LocationDistanceCalculator();
            double distance = calculator.getDistanceBetweenPostalCodes(
                    originCountryCode, dto.getOriginPostalCode(),
                    destinationCountryCode, dto.getDestinationPostalCode());

            double shippingCost = distance < 750 ? BASIC_COST : BASIC_COST * 1.25;


            outputStream.write(String.valueOf(shippingCost).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            System.out.println("Response sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
