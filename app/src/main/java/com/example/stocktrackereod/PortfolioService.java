package com.example.stocktrackereod;

import java.util.Set;

public class PortfolioService {
    public double calcCurrentPortfolioValue(Set<Position> positions) {
        return positions.stream().mapToDouble(Position::getCurrentValue).sum();
    }

    public double calcDifferential(Portfolio portfolio){
        return portfolio.getPreviousPortfolioValue() / portfolio.getCurrentPortfolioValue() *100;
    }
}
