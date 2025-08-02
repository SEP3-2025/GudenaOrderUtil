package com.gudena.shipping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EUCountryCodeMap {

    private static final Map<String, String> EU_COUNTRY_CODES;

    static {
        Map<String, String> map = new HashMap<>();
        map.put("Austria", "AT");
        map.put("Belgium", "BE");
        map.put("Bulgaria", "BG");
        map.put("Croatia", "HR");
        map.put("Cyprus", "CY");
        map.put("Czech Republic", "CZ");
        map.put("Denmark", "DK");
        map.put("Estonia", "EE");
        map.put("Finland", "FI");
        map.put("France", "FR");
        map.put("Germany", "DE");
        map.put("Greece", "GR");
        map.put("Hungary", "HU");
        map.put("Ireland", "IE");
        map.put("Italy", "IT");
        map.put("Latvia", "LV");
        map.put("Lithuania", "LT");
        map.put("Luxembourg", "LU");
        map.put("Malta", "MT");
        map.put("Netherlands", "NL");
        map.put("Poland", "PL");
        map.put("Portugal", "PT");
        map.put("Romania", "RO");
        map.put("Slovakia", "SK");
        map.put("Slovenia", "SI");
        map.put("Spain", "ES");
        map.put("Sweden", "SE");
        EU_COUNTRY_CODES = Collections.unmodifiableMap(map);
    }

    private EUCountryCodeMap() {
    }

    public static String getCode(String countryName) {
        return EU_COUNTRY_CODES.get(countryName);
    }

    public static boolean containsCountry(String countryName) {
        return EU_COUNTRY_CODES.containsKey(countryName);
    }

    public static Map<String, String> getAll() {
        return EU_COUNTRY_CODES;
    }
}
