/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author Dario
 */
public class CpuPlayer extends Player {

    //lunghezza delle navi da colpire
    //La probabilita di colpire una nave da 5 è piu alta rispetto alle altre
    //private int currentTarget = 5;
    private static final int MAX_SHIP_LENGTH = 5;
    private static final int MIN_SHIP_LENGTH = 1;
    private List<Ship> sunkShips = new ArrayList<>();
    private static final Logger LOG = Logger.getLogger(CpuPlayer.class.getName());

    public CpuPlayer(BattleField battleField) {
        super(battleField);
    }

    public void addSunkShip(Ship ship) {
        /**
         * Aggiunge la navi affondate del giocatore avversario
         */
        sunkShips.add(ship);
    }

    public int getCountSunkShipsByLength(int length) {
        return (int) sunkShips.stream().filter(s -> s.getLength() == length).count();

    }

    public int getCountAliveShipsByLength(int length) {
        /**
         * Resituisce il numero di navi non affondate di una data lunghezza
         */
        return super.getBattleField().getCountShipsByLenght(length) - getCountSunkShipsByLength(length);
    }

    public int updateTarget() {
        /**
         * Aggiornata il target corrente a partire dalla massima lunghezza
         * considerando le navi affondate
         */
        int currentTarget = MAX_SHIP_LENGTH;
        while (getCountAliveShipsByLength(currentTarget) == 0 && currentTarget > MIN_SHIP_LENGTH) {
            currentTarget -= 1;
        }
        return currentTarget;
    }

    public Position policy() {
        
        int currentTarget = updateTarget();
        //rendere l'eccezione unchecked
        List<ShadowShip> shadowShips = getAlignmentHitPositions();
        Set<Position> forbPositions = forbiddenPositions(shadowShips);
        Set<Position> attacks = generateAttackablePositions(shadowShips);
        Set<Position> avaiblePositions = super.getAvaibleMoves().stream().collect(Collectors.toSet());
        Set<Position> diagAttack = diagonalStrategy(currentTarget);

        avaiblePositions.removeAll(forbPositions);
        //intersezione
        attacks.retainAll(avaiblePositions);
        //System.out.println("SHADOW SHIPS: " + shadowShips);
        //System.out.println("NAVI AFFONDATE: " + sunkShips);
        diagAttack.retainAll(avaiblePositions);
        //System.out.println("CURRENT TARGET: " + currentTarget);

        if (!attacks.isEmpty()) {
            //System.out.println("ATTACK: " + attacks);
            LOG.info("Attack Strategy");
            return attacks.stream().collect(Collectors.toList()).get(0);
        } else {
            //calcolo le probabilita in base al target
            List<Pair<Position, Double>> distribution = getHitMap(diagAttack, currentTarget);
            //System.out.println("POSIZIONI DISPONIBILI: " + avaiblePositions);
            //System.out.println("DISTRIBUTION: " + distribution);
            LOG.info("Diagonal Strategy");
            EnumeratedDistribution<Position> enumeratedDistribution = new EnumeratedDistribution(distribution);
            return enumeratedDistribution.sample();
            //return diagAttack.stream().collect(Collectors.toList()).get(0);
        }

    }

    public List<Pair<Position, Double>> getHitMap(Set<Position> positions, int target) {
        List<Pair<Position, Double>> hitMap = new ArrayList<>();
        for (Position pos : positions) {
            double prob = 0.;
            // Controllo delle posizioni circostanti
            prob += BattleField.isValidPosition(new Position(pos.getX(), pos.getY() - (target - 1))) ? 1 : 0; // top
            prob += BattleField.isValidPosition(new Position(pos.getX(), pos.getY() + target - 1)) ? 1 : 0; // bottom
            prob += BattleField.isValidPosition(new Position(pos.getX() - (target - 1), pos.getY())) ? 1 : 0; // left
            prob += BattleField.isValidPosition(new Position(pos.getX() + target - 1, pos.getY())) ? 1 : 0; // right
            hitMap.add(new Pair(pos, prob));
        }
        return hitMap;
    }

    public Position generateRandomPosition() {
        /**
         * Genera un posizione casuale tra quelle non giocate
         */
        List<Position> avaibleMoves = getAvaibleMoves();
        Random random = new Random();
        int index = random.nextInt(avaibleMoves.size());
        return avaibleMoves.get(index);
    }

    public Set<Position> diagonalStrategy(int distance) {

        /**
         * distance è la distanza tra i campioni lungo le x e le y
         *
         */
        List<Position> positions = super.getAvaibleMoves();
        //List<Position> strategy = new ArrayList<>();

        return positions.stream().filter(p -> (p.getX() + p.getY()) % distance == 0).collect(Collectors.toSet());

    }

    public Set<Position> generateAttackablePositions(List<ShadowShip> shadowShips) {

        /**
         *
         * Non considera le posizioni disponibili
         */
        List<Position> attacks = new ArrayList<>();

        // considero solo le shadowShips non affondate
        List<ShadowShip> shadowShipsFilter = shadowShips.stream().filter(s -> {
            for (Ship sunkShip : sunkShips) {
                if (s.match(sunkShip)) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());

        for (ShadowShip ss : shadowShipsFilter) {
            List<Position> shipPositions = ss.getPositions();

            // Posizioni di attacco per navi di lunghezza superiori ad 1
            if (ss.getLenght() > 1) {
                Position firstPos = ss.getHead();
                Position lastPos = ss.getTail();

                if (ss.isVertical()) {
                    attacks.add(new Position(firstPos.getX(), firstPos.getY() - 1)); // top
                    attacks.add(new Position(lastPos.getX(), lastPos.getY() + 1)); // bottom
                } else { // Nave orizzontale
                    attacks.add(new Position(firstPos.getX() - 1, firstPos.getY())); //left
                    attacks.add(new Position(lastPos.getX() + 1, lastPos.getY())); // right
                }
            } else {
                // Posizioni di attacco per nave di lunghezza 1
                Position pos = shipPositions.get(0);
                attacks.add(new Position(pos.getX(), pos.getY() - 1)); // top
                attacks.add(new Position(pos.getX(), pos.getY() + 1)); // bottom
                attacks.add(new Position(pos.getX() - 1, pos.getY())); // left
                attacks.add(new Position(pos.getX() + 1, pos.getY())); // right
            }
        }

        // Filtraggio finale delle posizioni valide
        return attacks.stream().filter(p -> BattleField.isValidPosition(p)).collect(Collectors.toSet());
    }

    public List<ShadowShip> getAlignmentHitPositions() {
        SimpleGraph<Position, DefaultEdge> g
                = new SimpleGraph<>(DefaultEdge.class);

        List<Position> hitPositions = super.getHitPositions();

        //inizializzazione delle partizioni
        hitPositions.forEach(hitPos -> g.addVertex(hitPos));

        // creazione delle connessioni
        for (int i = 0; i < hitPositions.size() - 1; i++) {

            for (int j = i + 1; j < hitPositions.size(); j++) {
                Position pi = hitPositions.get(i);
                Position pj = hitPositions.get(j);
                if (pi.isAligned(pj)) {
                    g.addEdge(pi, pj);

                }

            }
        }

        //individuazioni delle componenti connesse che rappresentano i pezzi colpiti adiacenti
        ConnectivityInspector<Position, DefaultEdge> conn = new ConnectivityInspector(g);
        List<Set<Position>> components = conn.connectedSets();

        // Trasformare ogni componente in uno ShadowShip
        return components.stream()
                .map(component -> new ShadowShip(new ArrayList<>(component)))
                .collect(Collectors.toList());

    }

    public Set<Position> forbiddenPositions(List<ShadowShip> shadowShips) {
        /**
         * P O P P O P
         * P X P O X O
         * P X P P O P
         * P O P
         */
        Set<Position> positions = new HashSet<>();

        for (ShadowShip ss : shadowShips) {
            List<Position> shipPositions = ss.getPositions();

            // Aggiungi posizioni diagonali (angoli)
            positions.add(new Position(shipPositions.get(0).getX() - 1, shipPositions.get(0).getY() - 1)); // top left
            positions.add(new Position(shipPositions.get(0).getX() + 1, shipPositions.get(0).getY() - 1)); // top right
            positions.add(new Position(shipPositions.get(shipPositions.size() - 1).getX() - 1, shipPositions.get(shipPositions.size() - 1).getY() + 1)); // bottom left
            positions.add(new Position(shipPositions.get(shipPositions.size() - 1).getX() + 1, shipPositions.get(shipPositions.size() - 1).getY() + 1)); // bottom right

            // Aggiungi celle laterali per ogni posizione della nave
            for (Position pos : shipPositions) {
                if (ss.getLenght() > 1) {
                    if (ss.isVertical()) {
                        positions.add(new Position(pos.getX() - 1, pos.getY())); //LEFT
                        positions.add(new Position(pos.getX() + 1, pos.getY()));//RIGHT
                    } else {
                        positions.add(new Position(pos.getX(), pos.getY() - 1));//TOP
                        positions.add(new Position(pos.getX(), pos.getY() + 1));//BOTTOM
                    }
                }
            }
        }

        // Filtra posizioni non valide
        return positions.stream().filter(BattleField::isValidPosition).collect(Collectors.toSet());
    }

    public static void main(String args[]) {
        BattleField bf = new BattleField();
        CpuPlayer cpu = new CpuPlayer(bf);

        Position p1 = new Position(7, 1);
        Position p2 = new Position(7, 0);
        Position p3 = new Position(7, 2);
        Position p4 = new Position(4, 1);
        Position p5 = new Position(3, 1);
        Position p6 = new Position(2, 1);

        p1.setHit(true);
        p2.setHit(true);
        p3.setHit(true);
        p4.setHit(true);
        p5.setHit(true);
        p6.setHit(true);

        cpu.addMove(p1);
        cpu.addMove(p2);
        cpu.addMove(p3);
        cpu.addMove(p4);
        cpu.addMove(p5);
        cpu.addMove(p6);

        Position move = cpu.policy();
        System.out.println("Mossa effettuata: " + move);

        //individuare le mosse probite
        //individuo i possibili attacchi
        //rimuovo dall'insieme delle mosse disponili le mosse proibite
        //filtro solo i possibili attacchi presenti nelle mosse disponibili
        // calcolo per ogni possibile attacco la probabilita
        // scelgo l'attacco con prob piu alta, se la prob è 0 scelgo una posizione a caso
    }

}
