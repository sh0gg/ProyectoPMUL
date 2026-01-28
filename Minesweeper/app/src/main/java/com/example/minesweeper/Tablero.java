package com.example.minesweeper;

import java.util.Random;

public class Tablero {

    public final int ancho, alto, numeroDeMinas;

    public final boolean[][] minas;
    public final boolean[][] revelada;
    public final boolean[][] conBandera;
    public final int[][] adyacentes;

    public boolean gameOver = false;
    public boolean victoria = false;

    public Tablero(int ancho, int alto, int numeroDeMinas) {
        this.ancho = ancho;
        this.alto = alto;
        this.numeroDeMinas = Math.min(numeroDeMinas, ancho * alto - 1);

        this.minas = new boolean[alto][ancho];
        this.revelada = new boolean[alto][ancho];
        this.conBandera = new boolean[alto][ancho];
        this.adyacentes = new int[alto][ancho];

        colocarMinas();
        calcularAdyacentes();
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < ancho && y >= 0 && y < alto;
    }

    private void colocarMinas() {
        Random rnd = new Random();
        int colocadas = 0;
        while (colocadas < numeroDeMinas) {
            int x = rnd.nextInt(ancho);
            int y = rnd.nextInt(alto);
            if (!minas[y][x]) {
                minas[y][x] = true;
                colocadas++;
            }
        }
    }

    private void calcularAdyacentes() {
        int[] dx = {-1,-1,-1, 0,0, 1,1,1};
        int[] dy = {-1, 0, 1,-1,1,-1,0,1};
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                if (minas[y][x]) continue;
                int c = 0;
                for (int k = 0; k < 8; k++) {
                    int nx = x + dx[k];
                    int ny = y + dy[k];
                    if (inBounds(nx, ny) && minas[ny][nx]) c++;
                }
                adyacentes[y][x] = c;
            }
        }
    }

    public void alternarBandera(int x, int y) {
        if (!inBounds(x, y) || revelada[y][x] || gameOver) return;
        conBandera[y][x] = !conBandera[y][x];
        comprobarVictoria();
    }

    public void revelar(int x, int y) {
        if (!inBounds(x, y) || revelada[y][x] || conBandera[y][x] || gameOver) return;

        revelada[y][x] = true;

        if (minas[y][x]) {
            gameOver = true;
            victoria = false;
            return;
        }

        if (adyacentes[y][x] == 0) {
            for (int ny = y - 1; ny <= y + 1; ny++) {
                for (int nx = x - 1; nx <= x + 1; nx++) {
                    if (nx == x && ny == y) continue;
                    revelar(nx, ny);
                }
            }
        }
        comprobarVictoria();
    }

    public void comprobarVictoria() {
        if (gameOver) return;
        // Condición 1: todas las casillas sin mina están reveladas
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                if (!minas[y][x] && !revelada[y][x]) {
                    victoria = false;
                    return;
                }
            }
        }
        victoria = true;
        gameOver = true;
    }
}
