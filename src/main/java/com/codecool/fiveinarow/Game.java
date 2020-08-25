package com.codecool.fiveinarow;

import java.util.Arrays;
import java.util.Scanner;

public class Game implements GameInterface {

    private int[][] board;

    public Game(int nRows, int nCols) {
        board = new int[nRows][nCols];
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                board[row][col] = 0;
            }
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int[] getMove(int player) {
//        If the user provides coordinates that are outside of board, keep asking
//        If the user provides coordinates for a place that is taken, keep asking
//        If the user provides input that doesn't look like coordinates, keep asking

        // char[] columnsLetters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'Z'};
        String columnsLetters = "ABCDEFGHIJKLMNOPQRSTUVXYZ";
        char inputRow, inputRowRaw;
        int inputCol = 0;
        int inputRowNumber = 0;
        boolean keepAsking = true;
        int[] coordinates = new int[2];

        Scanner scanner = new Scanner(System.in);

        while (keepAsking) {
            try {
                System.out.println("Please, give the row, eg.: A ->");
                inputRowRaw = scanner.next().charAt(0);
                inputRow = Character.toUpperCase(inputRowRaw);
                inputRowNumber = columnsLetters.indexOf(inputRow);
                System.out.println("Please, give the column, eg.: 1 ->");
                inputCol = scanner.nextInt();
            } catch (Exception e) {
                keepAsking = true;
                System.out.println("Don't kidding me, dude!");
                continue;
            }

            if (inputRowNumber > board[0].length) {
                keepAsking = true;
                System.out.println("Wrong column letter!");
            } else {
                keepAsking = false;
            }
            if (inputCol > board.length) {
                keepAsking = true;
                System.out.println("Wrong row number!");
            } else {
                keepAsking = false;
            }

            System.out.println("row: " + inputRowNumber);
            System.out.println("col: " + inputCol);
            if (board[inputRowNumber][inputCol] != 0) {
                keepAsking = true;
                System.out.println("This field is not empty!");
            }
            ;
        }

        coordinates[0] = inputRowNumber;
        coordinates[1] = inputCol;
        return coordinates;
    }

    public int[] getAiMove(int player) {
        return null;
    }

    public void mark(int player, int row, int col) {
        if (board[row][col] == 0) {
            try {
                board[row][col] = player;
            } catch (Exception e) {
                System.out.println("Bad..");
            }
        }

//        for (int[] aRow : board) {
//            for (int elem : aRow) {
//                System.out.print(elem);
//            }
//            System.out.println();
//        }
    }


    public boolean hasWon(int player, int howMany) {
        return false;
    }

    public boolean isFull() {
        return false;
    }

    public void printBoard() {
    }

    public void printResult(int player) {
    }

    public void enableAi(int player) {
    }

    public void play(int howMany) {
    }
}
