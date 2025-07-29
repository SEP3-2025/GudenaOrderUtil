package com.gudena.shipping;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class LocationDistanceCalculator {
    private static final String GOOGLE_API_KEY = "AIzaSyB9ldS8zuvlpA0AiVy2JtlY48bwP0Fev0k";

    public double getDistanceBetweenPostalCodes(String country1, String postalCode1,
                                                String country2, String postalCode2) throws Exception {
        double[] coords1 = getCoordinates(country1, postalCode1);
        double[] coords2 = getCoordinates(country2, postalCode2);

        return calculateDistance(coords1[0], coords1[1], coords2[0], coords2[1]);
    }

    public double[] getCoordinates(String country, String postalCode) throws Exception {
        String components = String.format("country:%s|postal_code:%s", country, postalCode);
        String encodedComponents = URLEncoder.encode(components, StandardCharsets.UTF_8);

        String url = String.format(
                "https://maps.googleapis.com/maps/api/geocode/json?components=%s&key=%s",
                encodedComponents, GOOGLE_API_KEY);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());

        if (json.getString("status").equals("OK")) {
            JSONArray results = json.getJSONArray("results");
            JSONObject location = results.getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");
            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");
            return new double[]{lat, lng};
        }
        throw new Exception("Unable to get coordinates for the specified location.");
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS_KM = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
