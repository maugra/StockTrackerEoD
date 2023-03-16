package com.example.stocktrackereod;

public class Position {
    private String symbol;
    private int amount;
    private double currentValue;
    private double previousValue;
    private double differential;
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

    public void updateValueAndDifferential(){
        double newCurrentValue = positionService.calculateCurrentValue(this);
        if (newCurrentValue != this.previousValue){
            this.previousValue = this.currentValue;
            this.currentValue = newCurrentValue;
            setDifferential(positionService.calculateDifferential(this));
        }
    }

}
