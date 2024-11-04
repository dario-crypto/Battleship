/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship;

import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.math3.util.Pair;

/**
 *
 * @author Dario
 */
public final class BattleField {

    private final static int FIELD_SIZE = 10;

    // mosse realizzate
    private List<Position> moves = new ArrayList<>();
    // posizioni colpite
    private List<Position> hits = new ArrayList<>();

    private List<Ship> ships = new ArrayList<>();

    public BattleField() {
        defaultInit();
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    public List<Position> getMoves() {
        return moves;
    }

    public void setMoves(List<Position> moves) {
        this.moves = moves;
    }

    public List<Position> getHits() {
        return hits;
    }

    public void setHits(List<Position> hits) {
        this.hits = hits;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public static boolean isValidPosition(Position position) {
        /**
         * Controlla che la nave non supera i confini definiti dal campo di
         * battaglia
         */
        boolean checkX = (position.getX() >= 0 && position.getX() < FIELD_SIZE);
        boolean checkY = (position.getY() >= 0 && position.getY() < FIELD_SIZE);
        return checkX && checkY;
    }

    public Ship hit(Position position) throws IllegalArgumentException {
        /**
         * Restituisce true se una nave posizionata sulla board è stata colpita,
         * atrimenti restituisce false
         */

        if (!isValidPosition(position)) {
            throw new IllegalArgumentException("Posizione: " + position.toString() + " non valida");
        }

        moves.add(position);

        for (Ship ship : ships) {

            if (ship.collision(position)) {
                ship.decreaseLife();
                hits.add(position);
                //return true;

                return ship;

            }
        }
        return null;
    }

    public Ship getShipByPosition(Position position) {
        for (Ship ship : ships) {
            if (ship.collision(position)) {
                return ship;
            }
        }
        return null;
    }

    public List<Position> getAllMoves() {
        List<Position> allMoves = new ArrayList<>();
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                allMoves.add(new Position(i, j));

            }
        }
        return allMoves;
    }

    public int getCountSunkShips() {
        /**
         * Restituisce il numero di navi affondate
         */
        int count = 0;
        for (Ship ship : ships) {
            if (ship.getLife() == 0) {
                count += 1;
            }
        }
        return count;
    }

    public int getCountShips() {
        /**
         * Restituisce il numero complessivo di navi
         */
        return ships.size();
    }

    public void reset() {
        ships.clear();
        moves.clear();
        hits.clear();
    }

    public List<Position> getHitMoves() {
        return hits;
    }

    public int getFieldSize() {
        return FIELD_SIZE;
    }

    public static void main(String args[]) {

        BattleField bf = new BattleField();
        Ship ship = new Ship(new Position(0, 0), 3, Ship.ShipDirection.HORIZONTAL);
        bf.addShip(ship);
        System.out.println(bf.hit(new Position(0, 3)));

    }

    public boolean shipCollision() {
        /**
         * Restituisce true se una delle navi nella board collide con almeno una
         * delle altre navi
         */
        for (int i = 0; i < ships.size() - 1; i++) {
            if (ships.get(i).collision(ships.get(i + 1))) {
                return true;
            }

        }
        return false;
    }

    public List<Ship> getShipsByLength(int length) {
        return ships.stream().filter(s -> s.getLength() == length).collect(Collectors.toList());
    }

    public int getCountShipsByLenght(int length) {
        return (int) ships.stream().filter(s -> s.getLength() == length).count();

    }

    public int getAliveShipsCountByLength(int lenght) {
        return (int) getShipsByLength(lenght).stream().filter(s -> !s.isSunk()).count();
    }

    public void defaultInit() {

        Ship ship1 = new Ship(new Position(0, 0), 3, Ship.ShipDirection.HORIZONTAL);
        Ship ship2 = new Ship(new Position(2, 7), 3, Ship.ShipDirection.VERTICAL);
        Ship ship3 = new Ship(new Position(5, 7), 2, Ship.ShipDirection.HORIZONTAL);
        Ship ship4 = new Ship(new Position(2, 5), 5, Ship.ShipDirection.HORIZONTAL);
        Ship ship5 = new Ship(new Position(8, 5), 4, Ship.ShipDirection.VERTICAL);
        //Ship ship6 = new Ship(new Position(9, 6), 1, Ship.ShipDirection.HORIZONTAL);
        //Ship ship7 = new Ship(new Position(0, 7), 1, Ship.ShipDirection.VERTICAL);
        //Ship ship8 = new Ship(new Position(4, 7), 1, Ship.ShipDirection.VERTICAL);
        //Ship ship9 = new Ship(new Position(0, 9), 2, Ship.ShipDirection.HORIZONTAL);
        //Ship ship10 = new Ship(new Position(7, 9), 2, Ship.ShipDirection.HORIZONTAL);

        ships.add(ship1);
        ships.add(ship2);
        ships.add(ship3);
        ships.add(ship4);
        ships.add(ship5);
        /*
        ships.add(ship6);
        ships.add(ship7);
        ships.add(ship8);
        ships.add(ship9);
        ships.add(ship10);
         */
        setShips(ships);

    }

}
