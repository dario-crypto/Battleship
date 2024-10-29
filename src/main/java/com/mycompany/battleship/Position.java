/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.battleship;

import java.util.List;
import java.util.Set;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 *
 * @author Dario
 */
public class Position {

    private int x;
    private int y;
    private boolean hit;

    @Override
    public String toString() {
        return "Position{" + "x=" + x + ", y=" + y + '}';
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public Position() {

    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
        final Position other = (Position) obj;
        if (this.x != other.x) {
            return false;
        }
        return this.y == other.y;
    }

    public boolean isAligned(Position p) {

        return isHorizontalAligned(p) || isVerticalAligned(p);

    }

    public boolean isHorizontalAligned(Position p) {

        return Math.abs(x - p.getX()) == 1 && y == p.getY();

    }

    public boolean isVerticalAligned(Position p) {
        return Math.abs(y - p.getY()) == 1 && x == p.getX();
    }

    public static void main(String args[]) {
        Position p = new Position(2, 2);
        Position p2 = new Position(2, 3);
        Position p3 = new Position(5, 5);
        Position p4 = new Position(5, 6);
        System.out.println(p.isVerticalAligned(p2));

        SimpleGraph<Position, DefaultEdge> g
                = new SimpleGraph<Position, DefaultEdge>(DefaultEdge.class);

        g.addVertex(p);
        g.addVertex(p2);
        g.addVertex(p3);
        g.addVertex(p4);
        g.addEdge(p, p2);
        g.addEdge(p3, p4);
        g.addVertex(p3);

        ConnectivityInspector<Position, DefaultEdge> conn = new ConnectivityInspector(g);
        List<Set<Position>> components = conn.connectedSets();
        for (Set<Position> component : components) {
            System.out.println(component);
        }

    }
}
