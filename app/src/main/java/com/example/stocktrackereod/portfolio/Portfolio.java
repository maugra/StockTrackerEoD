package com.example.stocktrackereod.portfolio;

import com.example.stocktrackereod.position.Position;

import java.util.HashSet;
import java.util.Set;

public class Portfolio {
    private Set<Position> positions = new HashSet<>();
    private double currentPortfolioValue;
    private double previousPortfolioValue;
    private double portfolioDifferential;
    private final PortfolioService portfolioService = new PortfolioService();

    public Set<Position> getPositions() {
        return positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }

    public double getCurrentPortfolioValue() {
        return currentPortfolioValue;
    }

    public void setCurrentPortfolioValue(double currentPortfolioValue) {
        this.currentPortfolioValue = currentPortfolioValue;
    }

    public double getPreviousPortfolioValue() {
        return previousPortfolioValue;
    }

    public void setPreviousPortfolioValue(double previousPortfolioValue) {
        this.previousPortfolioValue = previousPortfolioValue;
    }

    public double getPortfolioDifferential() {
        return portfolioDifferential;
    }

    public void setPortfolioDifferential(double portfolioDifferential) {
        this.portfolioDifferential = portfolioDifferential;
    }

    public PortfolioService getPortfolioService() {
        return portfolioService;
    }

    public void updateValueAndDifferential(){
        double newCurrentValue = portfolioService.calcCurrentPortfolioValue(this.positions);
        if (newCurrentValue != this.previousPortfolioValue){
            this.previousPortfolioValue = this.currentPortfolioValue;
            this.currentPortfolioValue = newCurrentValue;
            setPortfolioDifferential(portfolioService.calcDifferential(this));
        }
    }
}
