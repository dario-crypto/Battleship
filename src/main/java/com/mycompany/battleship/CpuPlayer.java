/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public CpuPlayer(BattleField battleField) {
        super(battleField);
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
         * Distranza tra le posizioni tra le righe e le colonne
         *
         *
         */

        List<Position> positions = super.getAvaibleMoves();
        //List<Position> strategy = new ArrayList<>();

        return positions.stream().filter(p -> (p.getX() + p.getY()) % distance == 0).collect(Collectors.toSet());

    }

    public List<Position> generateAttackablePositions() throws Exception {

        /**
         *
         * Non considera le posizioni disponibili
         */
        List<ShadowShip> shadowShips = getAlignmentHitPositions();

        // Ottenere le navi colpite
        List<Position> attacks = new ArrayList<>();

        for (ShadowShip ss : shadowShips) {
            List<Position> shipPositions = ss.getPositions();

            // Posizioni di attacco per navi di lunghezza > 1
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
        return attacks.stream().filter(p -> BattleField.isValidPosition(p)).collect(Collectors.toList());
    }

    public List<ShadowShip> getAlignmentHitPositions() throws Exception {
        SimpleGraph<Position, DefaultEdge> g
                = new SimpleGraph<Position, DefaultEdge>(DefaultEdge.class);

        List<Position> hitPositions = super.getHitPositions();

        //inizializzazione delle partizioni
        hitPositions.forEach(hitPos -> g.addVertex(hitPos));

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

        //tramite le shadow ships individuo la direzione dei pezzi colpiti adiacenti
        List<ShadowShip> shadowShips = new ArrayList<>();
        for (Set<Position> component : components) {
            ShadowShip shadowShip = new ShadowShip(new ArrayList(component));
            shadowShips.add(shadowShip);
        }

        return shadowShips;

    }

    public Set<Position> forbiddenPositions(List<ShadowShip> shadowShips) {
        /**
         * P O P P O P
         * P X P O X P
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
        Position p1 = new Position(5, 5);
        Position p2 = new Position(5, 6);
        Position p5 = new Position(5, 7);
        Position p3 = new Position(8, 8);
        Position p4 = new Position(9, 8);

        Position p6 = new Position(0, 0);
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

        //individuare le mosse probite
        //individuo i possibili attacchi
        //rimuovo dall'insieme delle mosse dipobili le mosse proibite
        //filtro solo i possibili attacchi presenti nelle mosse disponibili
        // calcolo per ogni possibile attacco la probabilita
        // scelgo l'attacco con prob piu alta, se la prob è 0 scelgo una posizione a caso
        try {
            List<ShadowShip> shadowShips = cpu.getAlignmentHitPositions();
            Set<Position> forbPositions = cpu.forbiddenPositions(shadowShips);

            //forbPositions.forEach(p -> System.out.println(p));
            shadowShips.forEach(ss -> System.out.println(ss));
            cpu.generateAttackablePositions().forEach(a -> System.out.println("Attack: " + a));

            forbPositions.forEach(p -> System.out.println("Forbidden position: " + p));

        } catch (Exception ex) {
            Logger.getLogger(CpuPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
