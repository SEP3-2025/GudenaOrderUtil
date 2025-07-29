package com.gudena.payment;

public class Main {
    public static void main(String[] args) throws Exception {
        PaymentServer server = new PaymentServer(5002);
        server.start();
    }
}
