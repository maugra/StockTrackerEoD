package com.example.stocktrackereod;

public class PositionService {
    private final String BASE_URL = "https://api.polygon.io/v2/aggs/ticker/";
    private final String API_KEY = "2guV5i8O4ZqgNchftZ1WvhIbAqJMPRLf";


    public double calculateDifferential(Position position) {
        return position.getPreviousValue() / position.getCurrentValue() * 100;
    }

}
