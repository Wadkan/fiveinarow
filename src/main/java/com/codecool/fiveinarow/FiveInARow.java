package com.codecool.fiveinarow;

public class FiveInARow {

    public static void main(String[] args) {
        Game game = new Game(11, 11);

        int[] coordinates = game.getMove(1);
        game.mark(1, coordinates[0], coordinates[1]);

        game.enableAi(1);
        game.enableAi(2);
        game.play(5);
    }
}
