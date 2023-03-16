package com.example.stocktrackereod;

public class PositionService {
    private final String BASE_URL = "";
    private final String API_KEY = "2guV5i8O4ZqgNchftZ1WvhIbAqJMPRLf";

    public double calculateCurrentValue(Position position){
      return getPrice(position.getSymbol()) * position.getAmount();
    }

    public double calculateDifferential(Position position) {
        return position.getPreviousValue() / position.getCurrentValue() * 100;
    }

    private double getPrice(String symbol){
        return 1.0;
    }

}
