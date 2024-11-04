/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship.View;

import com.mycompany.battleship.BattleField;
import com.mycompany.battleship.CpuPlayer;
import com.mycompany.battleship.Player;
import com.mycompany.battleship.Position;
import com.mycompany.battleship.Ship;
import com.mycompany.battleship.View.BattleShipsPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Finestra principale di gioco
 *
 * @author Dario
 */
public class GameGUI extends JFrame {

    private final int WIDTH = 1000;
    private final int HEIGHT = 500;
    private final StartGameFlag startGame;
    private final BattleShipsPanel bsp;
    private final MovesHistoryPanel mhp;
    private final Player player;
    private final CpuPlayer cpuPlayer;
    private static final int KEY_UP = KeyEvent.VK_W;
    private static final int KEY_LEFT = KeyEvent.VK_A;
    private static final int KEY_DOWN = KeyEvent.VK_S;
    private static final int KEY_RIGHT = KeyEvent.VK_D;
    //grandezza delle celle
    private final int DELTA = 35;

    public GameGUI() {
        setFocusable(true);

        //inizializzazione delle board
        BattleField bf = new BattleField();
        BattleField cpubf = new BattleField();

        startGame = new StartGameFlag();

        // settagglia dei giocatori
        player = new Player(bf);
        cpuPlayer = new CpuPlayer(cpubf);

        // inizializzazione dei componenti swing
        bsp = new BattleShipsPanel(new Dimension(400, HEIGHT), bf, DELTA, startGame, cpuPlayer.getMovesHistory());
        mhp = new MovesHistoryPanel(new Dimension(400, HEIGHT), player, DELTA, startGame);
        StartGamePanel sgp = new StartGamePanel();

        setLocation(new Point(150, 150));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());

        // Posizionamento dei componenti
        add(bsp, BorderLayout.WEST);
        add(mhp, BorderLayout.EAST);
        add(sgp, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

                Ship selectedShip = bsp.getSelectedShip();
                System.out.println(selectedShip);

                if (selectedShip != null && startGame.isStopGame()) {

                    switch (e.getKeyCode()) {
                        case KEY_UP:
                            System.out.println("Muoviti sopra");
                            selectedShip.moveTop();
                            break;
                        case KEY_LEFT:
                            System.out.println("Muoviti a sinsitra");
                            selectedShip.moveLeft();
                            break;
                        case KEY_DOWN:
                            System.out.println("Muoviti giu");
                            selectedShip.moveBottom();
                            break;
                        case KEY_RIGHT:
                            System.out.println("Muoviti a destra");
                            selectedShip.moveRight();
                            break;
                        default:
                            break;
                    }

                    System.out.println("Nuove coordinate: " + selectedShip);
                    bsp.repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });

        pack();

    }

    public class StartGamePanel extends JPanel {

        private final JButton startGameButton;
        private final JLabel gameTips;
        private final String textTips = "<html>Configura il campo di battaglia.<br>Seleziona la nave con un click e spostala con i tasti W A S D.</html>";

        public StartGamePanel() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            startGameButton = new JButton("Start Game");
            gameTips = new JLabel(textTips);
            gameTips.setPreferredSize(new Dimension(50, 300));
            gameTips.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(startGameButton);
            //creo uno spazio verticale
            add(Box.createVerticalStrut(50));
            add(gameTips);
            startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            startGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startGame.setStartGame(true);
                    //disabilita il bottone
                    startGameButton.setEnabled(false);
                    System.out.println("Start Game: " + startGame);
                    gameTips.setText("Fa la tua mossa!");
                    bsp.resetSelectedShip();
                    bsp.repaint();
                }

            });

        }

    }

    public class MovesHistoryPanel extends JPanel {

        private final Dimension dimension;
        private final int delta;
        private final Player player;
        private final int startX = 20;
        private final int startY = 20;

        public MovesHistoryPanel(Dimension dimension, Player player, int delta, StartGameFlag startGame) {
            this.dimension = dimension;
            this.player = player;
            //delta = (int) (this.dimension.getWidth() / player.getBattleField().getFieldSize());
            this.delta = delta;
            setBackground(Color.WHITE);
            setPreferredSize(dimension);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    if (startGame.isStartGame()) {
                        int x = (int) Math.ceil((e.getX() - startX) / delta);
                        int y = (int) Math.ceil((e.getY() - startY) / delta);

                        BattleField bf = player.getBattleField();
                        Position attackPos = new Position(x, y);
                        if (bf.isValidPosition(attackPos) && player.getAvaibleMoves().contains(attackPos)) {

                            //attacco il computer
                            Ship ship = cpuPlayer.receiveAttack(attackPos);
                            if (ship != null) {
                                attackPos.setHit(true);
                                if (ship.isSunk()) {
                                    System.out.println("Hai affondato una nave di lunghezza: " + ship.getLength());
                                }
                            }

                            //attackPos.setHit(hit);
                            //aggiornamento mossa utente
                            player.addMove(attackPos);

                            System.out.println("Posizione colpita: " + attackPos);
                            bsp.repaint();

                            if (cpuPlayer.isLose()) {
                                System.out.println("Hai vinto!");
                                startGame.toogle();
                                JOptionPane.showMessageDialog(null, "Hai vinto!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

                            }

                            //Position cpuPos = cpuPlayer.generateRandomPosition();
                            Position cpuPos = cpuPlayer.policy();
                            Ship ship2 = player.receiveAttack(cpuPos);
                            if (ship2 != null) {
                                cpuPos.setHit(true);
                                if (ship2.isSunk()) {
                                    cpuPlayer.addSunkShip(ship2);
                                    System.out.println("Nave affondata: " + ship2.getLength());
                                }
                            }

                            //aggiornamento mosse cpu
                            cpuPlayer.addMove(cpuPos);
                            bsp.repaint();

                            if (player.isLose()) {
                                System.out.println("Hai perso!");
                                startGame.toogle();
                                JOptionPane.showMessageDialog(null, "Hai perso!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                            }

                            System.out.println("CPU colpisce positione: " + cpuPos);
                            System.out.println("-------------------------------");
                        }

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

        public void setEnemyMoves(List<Position> enemyMoves) {
            //implementa

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            BattleField battleField = player.getBattleField();
            //disegnare le linee verticali
            for (int i = 0; i <= battleField.getFieldSize(); i++) {

                int x = i * delta + startX;
                int y = delta * battleField.getFieldSize() + startY;
                g.drawLine(x, startY, x, y);

            }

            //disegna linee orizzontali
            for (int j = 0; j <= battleField.getFieldSize(); j++) {
                int y = j * delta + startX;
                int x = delta * battleField.getFieldSize() + startY;
                g.drawLine(startX, y, x, y);
            }

            // disegno delle posizioni scelte
            List<Position> positions = player.getMovesHistory();
            for (Position pos : positions) {
                int xGui = pos.getX() * delta + startX;
                int yGui = pos.getY() * delta + startY;

                if (pos.isHit()) {
                    g.setColor(Color.red);
                } else {
                    g.setColor(Color.GREEN);
                }
                g.fillRect(xGui, yGui, delta, delta);
            }

            //disegno deppe posizioni cpolpite dall'avversario
        }

    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameGUI().setVisible(true);
            }
        });
    }

}
