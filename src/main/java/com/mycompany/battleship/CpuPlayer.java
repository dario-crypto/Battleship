/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship;


import java.util.List;
import java.util.Random;

/**
 *
 * @author Dario
 */
public class CpuPlayer extends Player {

    public CpuPlayer(BattleField battleField) {
        super(battleField);
    }

    public Position generatePosition() {
        /**
         * Genera un posizione casuale tra quelle non giocate
         */
        List<Position> avaibleMoves = getAvaibleMoves();
        Random random = new Random();
        int index = random.nextInt(avaibleMoves.size());
        return avaibleMoves.get(index);
    }

}
