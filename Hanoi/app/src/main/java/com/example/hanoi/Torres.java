package com.example.hanoi;

import java.util.Stack;
import java.util.Random;

public class Torres {
    public Tower towerA;
    public Tower towerB;
    public Tower towerC;
    private final int capacity = 7;
    private final int totalPieces = 5;  
    private Random random;

    public Torres() {
        towerA = new Tower(capacity);
        towerB = new Tower(capacity);
        towerC = new Tower(capacity);
        random = new Random();

        generateRandomPieces();
    }

    private void generateRandomPieces() {
        // Limpiar torres antes de generar
        towerA.getStack().clear();
        towerB.getStack().clear();
        towerC.getStack().clear();

        // Repartimos piezas aleatoriamente entre las torres
        for (int i = 0; i < totalPieces; i++) {
            towerA.push(random.nextInt(5) + 1);
            towerB.push(random.nextInt(5) + 1);
            towerC.push(random.nextInt(5) + 1);
        }
    }

    public boolean movePiece(Tower from, Tower to) {
        Integer piece = from.peek();
        if (piece == null) return false;

        if (to.isEmpty() || to.peek() > piece) {
            from.pop();
            to.push(piece);
            return true;
        }
        return false;
    }
}
