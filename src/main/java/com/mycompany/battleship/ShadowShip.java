/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Dario
 */
public class ShadowShip {

    private List<Position> positions;
    private Ship.ShipDirection direction;

    public ShadowShip(List<Position> positions) throws Exception {

        this.positions = positions;
        direction = findDirection();
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Ship.ShipDirection findDirection() throws Exception {
        if (positions.size() == 1) {
            return Ship.ShipDirection.HORIZONTAL;
        }

        if (isHorizontal()) {
            return Ship.ShipDirection.HORIZONTAL;
        }

        if (isVertical()) {
            return Ship.ShipDirection.VERTICAL;
        }
        throw new Exception("Direzione non riconosciuta");
    }

    public boolean isHorizontal() {

        for (int i = 0; i < positions.size() - 1; i++) {
            if (!positions.get(i).isHorizontalAligned(positions.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    public boolean isVertical() {
        for (int i = 0; i < positions.size() - 1; i++) {
            if (!positions.get(i).isVerticalAligned(positions.get(i + 1))) {
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
