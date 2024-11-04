/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Dario
 */
public class ShadowShip {

    private List<Position> positions;
    private Ship.ShipDirection direction;

    public ShadowShip(List<Position> positions) {

        this.positions = positions;
        direction = findDirection();
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Ship.ShipDirection findDirection() {
        if (positions.size() == 1) {
            return Ship.ShipDirection.HORIZONTAL;
        }

        if (isHorizontal()) {
            return Ship.ShipDirection.HORIZONTAL;
        }

        if (isVertical()) {
            return Ship.ShipDirection.VERTICAL;
        }
        throw new RuntimeException("Direzione non riconosciuta" + toString());
    }

    public boolean isHorizontal() {
        List<Position> sortedPositions = new ArrayList<>(positions);

        // Ordina le posizioni in base alla coordinata x
        Collections.sort(sortedPositions, new Comparator<Position>() {
            @Override
            public int compare(Position p1, Position p2) {
                return Integer.compare(p1.getX(), p2.getX());
            }
        });

        for (int i = 0; i < sortedPositions.size() - 1; i++) {
            if (!sortedPositions.get(i).isHorizontalAligned(sortedPositions.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    public boolean isVertical() {
        List<Position> sortedPositions = new ArrayList<>(positions);
        Collections.sort(sortedPositions, new Comparator<Position>() {
            @Override
            public int compare(Position p1, Position p2) {
                return Integer.compare(p1.getY(), p2.getY());
            }
        });
        for (int i = 0; i < sortedPositions.size() - 1; i++) {
            if (!sortedPositions.get(i).isVerticalAligned(sortedPositions.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    public int getLenght() {
        return positions.size();
    }

    @Override
    public String toString() {
        return "ShadowShip{" + "positions=" + positions + ", direction=" + direction + '}';
    }

    public Position getVerticalHead() {

        /**
         * Restituisce la posizione di testa della nave supponendo che sia
         * allineata verticalmente
         */
        int minY = positions.get(0).getY();
        int x = positions.get(0).getX();

        for (Position pos : positions) {
            if (pos.getY() < minY) {
                minY = pos.getY();
                x = pos.getX();
            }

        }

        return new Position(x, minY);
    }

    public Position getHorizontalHead() {
        int minX = positions.get(0).getX();
        int y = positions.get(0).getY();

        for (Position pos : positions) {
            if (pos.getX() < minX) {
                y = pos.getY();
                minX = pos.getX();
            }

        }
        return new Position(minX, y);

    }

    public boolean match(Ship ship) {

        List<Position> shipPositions = ship.getPositions();

        if (shipPositions.size() != getLenght()) {
            return false;
        } else {
            int count = 0;
            for (Position pos : positions) {
                for (Position shiPos : shipPositions) {

                    if (shiPos.equals(pos)) {
                        count += 1;
                        break;
                    }
                }
            }
            return count == shipPositions.size();
        }

    }

    public Position getVerticalTail() {
        int maxY = positions.get(0).getY();
        int x = positions.get(0).getX();

        for (Position pos : positions) {
            if (pos.getY() > maxY) {
                maxY = pos.getY();
                x = pos.getX();
            }
        }

        return new Position(x, maxY);

    }

    public Position getHorizontalTail() {
        int maxX = positions.get(0).getX();
        int y = positions.get(0).getY();

        for (Position pos : positions) {
            if (pos.getX() > maxX) {
                y = pos.getY();
                maxX = pos.getX();
            }

        }
        return new Position(maxX, y);
    }

    public Position getHead() {
        if (isHorizontal()) {
            return getHorizontalHead();
        } else {
            return getVerticalHead();
        }
    }

    public Position getTail() {
        if (isHorizontal()) {
            return getHorizontalTail();
        } else {
            return getVerticalTail();
        }
    }

}
