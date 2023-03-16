package com.example.stocktrackereod.position;

public class Position {
    private String symbol = "";
    private int amount = 0;
    private double currentValue = 0;
    private double previousValue = 0;
    private double differential = 0;
    private final PositionService positionService = new PositionService();

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(double previousValue) {
        this.previousValue = previousValue;
    }

    public double getDifferential() {
        return differential;
    }

    public void setDifferential(double differential) {
        this.differential = differential;
    }

    public PositionService getPositionService() {
        return positionService;
    }

    public void updateValueAndDifferential(double price){
        double newCurrentValue = price * this.getAmount();
        if (newCurrentValue != this.previousValue){
            this.previousValue = this.currentValue;
            this.currentValue = newCurrentValue;
            setDifferential(positionService.calculateDifferential(this));
        }
    }

}
