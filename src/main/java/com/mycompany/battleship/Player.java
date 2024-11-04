/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship;

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
public class Player {

    private BattleField battleField;

    private int winMatches = 0;

    private List<Position> movesHistory = new ArrayList<>();

    public Player(BattleField battleField) {
        this.battleField = battleField;
    }

    public Ship receiveAttack(Position position) {
        return battleField.hit(position);
    }

    public BattleField getBattleField() {
        return battleField;
    }

    public void setBattleField(BattleField battleField) {
        this.battleField = battleField;
    }

    public int getWinMatches() {
        return winMatches;
    }

    public void setWinMatches(int winMatches) {
        this.winMatches = winMatches;
    }

    public List<Position> getMovesHistory() {
        return movesHistory;
    }

    public void setMovesHistory(List<Position> movesHistory) {
        this.movesHistory = movesHistory;
    }

    public void addMove(Position position) {
        movesHistory.add(position);

    }

    public List<Position> getAvaibleMoves() {

        Set<Position> allMoves = new HashSet<>(battleField.getAllMoves());
        Set<Position> moveHistorySet = new HashSet<>(movesHistory);
        // differenza tra insiemi
        allMoves.removeAll(moveHistorySet);
        return new ArrayList<>(allMoves);
    }
    
    
    public List<Position> getHitPositions(){
      return movesHistory.stream().filter(p->p.isHit()).collect(Collectors.toList());
    }
    
    public List<Position> getHitsMoves() {
        return battleField.getHitMoves();
    }

    public boolean isLose() {
        /**
         * Restituisce true se tutte le navi sono state affondate, altrimenti
         * restituisce false
         */
        return battleField.getCountSunkShips() == battleField.getCountShips();
    }

    public Position getLastSelectedPosition() {
        return movesHistory.get(movesHistory.size() - 1);
    }

    public static void main(String args[]) {

    }

}
