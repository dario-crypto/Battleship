/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship.View;

import com.mycompany.battleship.BattleField;
import com.mycompany.battleship.Position;
import com.mycompany.battleship.Ship;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JPanel;

/**
 * Board per gestire lo schema di gioco
 *
 * @author Dario
 */
public class BattleShipsPanel extends JPanel {

    private final int delta;
    private final int startX = 20;
    private final int startY = 20;
    private final BattleField battleField;
    private Ship selectedShip;
    private final List<Position> enemyPositions;
    private final Color SHIP_COLOR = Color.BLACK;
    private final Color HIT_COLOR = Color.RED;
    private final Color SELECTED_POSITION = Color.GREEN;
    private final Color SELECTED_SHIP_COLOR = Color.BLUE;
    private final Color BACKGROUND_COLOR = Color.WHITE;

    public BattleShipsPanel(Dimension dimension, BattleField battleField, int delta, StartGameFlag startGameFlag, List<Position> enemyPositions) {

        this.battleField = battleField;
        setBackground(BACKGROUND_COLOR);
        setPreferredSize(dimension);
        this.delta = delta;
        this.enemyPositions = enemyPositions;

        //delta = (int) (this.dimension.getWidth() / battleField.getFieldSize());
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!startGameFlag.isStartGame()) {
                    int x = (int) Math.ceil((e.getX() - startX) / delta);
                    int y = (int) Math.ceil((e.getY() - startY) / delta);

                    System.out.println("Pisizione cliccata: " + new Position(x, y));
                    selectedShip = battleField.getShipByPosition(new Position(x, y));
                    System.out.println("Selected Ship: " + selectedShip);
                    repaint();
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

    }

    public void resetSelectedShip() {
        selectedShip = null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        //disegnare le linee verticali
        for (int i = 0; i <= battleField.getFieldSize(); i++) {

            int x = i * delta + startX;
            int y = delta * battleField.getFieldSize() + startY;
            g.drawLine(x, startY, x, y);

        }

        //disegna linee orizzontali
        for (int j = 0; j <= battleField.getFieldSize(); j++) {
            int y = j * delta + startY;
            int x = delta * battleField.getFieldSize() + startX;
            g.drawLine(startX, y, x, y);
        }

        //disegnare le navi
        List<Ship> ships = battleField.getShips();
        for (Ship ship : ships) {

            if (ship.equals(selectedShip)) {
                g.setColor(SELECTED_SHIP_COLOR);
            }

            List<Position> positions = ship.getPositions();
            for (Position pos : positions) {
                int xGui = pos.getX() * delta + startX;
                int yGui = pos.getY() * delta + startY;
                g.fillRect(xGui, yGui, delta, delta);

            }

            g.setColor(Color.BLACK);

        }
        g.setColor(SELECTED_POSITION);
        for (Position pos : enemyPositions) {
            int xGui = pos.getX() * delta + startX;
            int yGui = pos.getY() * delta + startY;
            g.fillRect(xGui, yGui, delta, delta);

        }

        // disegna pezzi colpiti
        List<Position> hitsPos = battleField.getHits();

        g.setColor(HIT_COLOR);
        
        for (Position pos : hitsPos) {
            int xGui = pos.getX() * delta + startX;
            int yGui = pos.getY() * delta + startY;
            g.fillRect(xGui, yGui, delta, delta);

        }
        //g.setColor(Color.BLACK);

    }

    public void drawShip(Graphics g, Ship ship) {
        List<Position> positions = ship.getPositions();
        for (Position pos : positions) {
            int xGui = pos.getX() * delta + startX;
            int yGui = pos.getY() * delta + startY;
            g.fillRect(xGui, yGui, delta, delta);
        }

    }

    public Ship getSelectedShip() {
        return selectedShip;
    }

    public void setSelectedShip(Ship selectedShip) {
        this.selectedShip = selectedShip;
    }

}
