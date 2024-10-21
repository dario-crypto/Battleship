/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship.View;

/**
 *
 * @author Dario
 */
/**
 * Oggetto condiviso dalle finestre di gioco per conservare lo stato della
 * partita
 *
 * @author Dario
 */
public class StartGameFlag {

    private boolean startGame;

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }

    public void toogle() {
        startGame = !startGame;
    }

    public boolean isStopGame() {
        return !startGame;
    }

}
