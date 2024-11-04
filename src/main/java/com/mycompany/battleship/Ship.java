/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Dario
 */
public class Ship {

    public enum ShipDirection {
        VERTICAL, HORIZONTAL

    }

    private Position headPosition;
    private int length;
    private int life;

    //numero di pezzi in vita
    private int hitPieces;
    private ShipDirection direction;

    public Ship(Position headPosition, int length, ShipDirection direction) {
        this.headPosition = headPosition;
        this.length = length;
        this.direction = direction;
        this.life = length;
    }

    public int getHitPieces() {
        return hitPieces;
    }

    public int getLife() {
        return life;
    }

    public void setHitPieces(int hitPieces) {
        this.hitPieces = hitPieces;
    }

    public Position getHeadPosition() {
        return headPosition;
    }

    public void setHeadPosition(Position headPosition) {
        this.headPosition = headPosition;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Position> getVerticalPositions() {

        List<Position> positions = new ArrayList<>();
        for (int j = 0; j < length; j++) {
            Position coverPosition = new Position(headPosition.getX(), headPosition.getY() + j);
            positions.add(coverPosition);
        }
        return positions;
    }

    public List<Position> getHorizontalPositions() {

        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Position coverPosition = new Position(headPosition.getX() + i, headPosition.getY());
            positions.add(coverPosition);

        }
        return positions;
    }

    public List<Position> getPositions() {
        /**
         * Restituisce le posizioni coperte dalla nave
         */
        List<Position> positions;
        if (getDirection() == ShipDirection.HORIZONTAL) {
            positions = getHorizontalPositions();
        } else {
            positions = getVerticalPositions();
        }
        return positions;

    }

    public boolean collision(Position position) {
        /**
         * Restituisce true se una posizione si sovrappone a quella della nave
         * corrente
         */
        List<Position> positions = getPositions();
        return positions.contains(position);

    }

    public boolean collision(Ship ship) {
        /**
         * Restituisce true se la nave corrente si sovrappone alla ship
         */
        List<Position> positions = ship.getPositions();
        for (Position pos : positions) {
            if (collision(pos)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ship other = (Ship) obj;
        if (this.length != other.length) {
            return false;
        }
        return Objects.equals(this.headPosition, other.headPosition);
    }

    public ShipDirection getDirection() {
        return direction;
    }

    public void setDirection(ShipDirection direction) {
        this.direction = direction;
    }

    public void decreaseLife() {
        if (life > 0) {
            life = life - 1;
        }
    }

    public boolean isSunk() {
        return life == 0;
    }

    /*Manca il meccanismo di verifica dello spostamento in tutte le direzioni*/
    public void moveBottom() {
        int x = headPosition.getX();

        int newY = headPosition.getY() + 1;
        Position newPos = new Position(x, newY);
        if (BattleField.isValidPosition(newPos)) {
            headPosition.setY(newY);
        }

    }

    public void moveTop() {
        int x = headPosition.getX();
        int newY = headPosition.getY() - 1;
        Position newPos = new Position(x, newY);
        if (BattleField.isValidPosition(newPos)) {
            headPosition.setY(newY);
        }

    }

    public void moveLeft() {
        int newX = headPosition.getX() - 1;
        int y = headPosition.getY();
        Position newPos = new Position(newX, y);
        if (BattleField.isValidPosition(newPos)) {
            headPosition.setX(newX);
        }

    }

    public void moveRight() {
        int newX = headPosition.getX() + 1;
        int y = headPosition.getY();
        Position newPos = new Position(newX, y);
        if (BattleField.isValidPosition(newPos)) {
            headPosition.setX(newX);
        }
        //headPosition.setX(newX);
    }

    public void changeDirection() {
        if (direction == Ship.ShipDirection.HORIZONTAL) {
            direction = ShipDirection.VERTICAL;
        } else {
            direction = ShipDirection.HORIZONTAL;
        }
    }

    @Override
    public String toString() {
        return "Ship{" + "headPosition=" + headPosition + ", length=" + length + '}';
    }

}
