package com.example.stocktrackereod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class PositionService {
    private final String BASE_URL = "https://api.polygon.io/v2/aggs/ticker/";
    private final String API_KEY = "2guV5i8O4ZqgNchftZ1WvhIbAqJMPRLf";


    public double calculateCurrentValue(Position position) {
        try {
            return getPrice(position.getSymbol()) * position.getAmount();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double calculateDifferential(Position position) {
        return position.getPreviousValue() / position.getCurrentValue() * 100;
    }

    private double getPrice(String symbol) throws IOException {
        URL url = new URL(constructURL(symbol));
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String response = new BufferedReader(new InputStreamReader(in))
                    .lines().collect(Collectors.joining("\n"));
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getJSONArray("results").getJSONObject(0).getDouble("c");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            urlConnection.disconnect();
        }
    }

    private String constructURL(String symbol) {


        return BASE_URL + symbol + "/?adjusted=true&apiKey=" + API_KEY;
    }

}
