package com.example.stocktrackereod.portfolio;

import com.example.stocktrackereod.position.Position;

import java.util.Set;

public class PortfolioService {
    public double calcCurrentPortfolioValue(Set<Position> positions) {
        return positions.stream().mapToDouble(Position::getCurrentValue).sum();
    }

    public double calcDifferential(Portfolio portfolio){
        return portfolio.getPreviousPortfolioValue() / portfolio.getCurrentPortfolioValue() *100;
    }
}
