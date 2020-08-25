package com.codecool.fiveinarow;

public class FiveInARow {

    public static void main(String[] args) {
        Game game = new Game(11, 11);

        game.enableGodMode();       // show AI thinking
        game.enablePreventMode();   // prevent enemies win – but will not win too fast..

        game.enableAi(1);
        game.enableAi(2);

        game.play(5);
    }
}
