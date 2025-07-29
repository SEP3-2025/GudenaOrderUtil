package com.gudena.shipping;

public class Main {
    public static void main(String[] args) throws Exception {
        ShippingCostServer server = new ShippingCostServer(5001);
        server.start();
    }
}