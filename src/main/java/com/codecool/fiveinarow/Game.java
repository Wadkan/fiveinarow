package com.codecool.fiveinarow;

import java.sql.Struct;
import java.util.Arrays;
import java.util.Scanner;

public class Game implements GameInterface {

    private int[][] board;
    private boolean ifPlayer1AI = false;
    private boolean ifPlayer2AI = false;

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
        char inputColRaw;
        int inputCol = 0;
        int inputRowNumber = 0;
        boolean keepAsking = true;
        int[] coordinates = new int[2];

        Scanner scanner = new Scanner(System.in);

        while (keepAsking) {
            System.out.println("Please, give the coordinates, eg.: A1 ->");
            String input = scanner.nextLine();

            if (input.equals("quit")) {
                coordinates[0] = -1;
                coordinates[1] = -1;
                return coordinates;
            }

            try {
                inputRow = Character.toUpperCase(input.charAt(0));
                inputRowNumber = columnsLetters.indexOf(inputRow);
                inputColRaw = input.charAt(1);
                inputCol = Character.getNumericValue(inputColRaw);
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

            try {

                if (board[inputRowNumber][inputCol] != 0) {
                    keepAsking = true;
                    System.out.println("This field is not empty!");
                }
            } catch (Exception ArrayIndexOutOfBoundsException) {
                System.out.println("Bad input.");
                keepAsking = true;
            }
        }

        coordinates[0] = inputRowNumber;
        coordinates[1] = inputCol;
        return coordinates;
    }

    public int[] getAiMove(int player) {
        if (isFull()) {
            return null;
        }
        int[] arr = {0, 0};
        return arr;
    }

    public void mark(int player, int row, int col) {
        if (board[row][col] == 0) {
            try {
                board[row][col] = player;
            } catch (Exception e) {
                System.out.println("Bad..");
            }
        }
    }


    public boolean hasWon(int player, int howMany) {
        StringBuilder regexBuild = new StringBuilder();
        regexBuild.append(".*");
        for (int i = 0; i < howMany; i++) {
            regexBuild.append("[" + Integer.toString(player) + "]");
        }
        ;
        regexBuild.append(".*");
        String regex = regexBuild.toString();

        for (int[] aRow : board) {
            StringBuilder rowStringBuilder = new StringBuilder();
            for (int elem : aRow) {
                rowStringBuilder.append(elem);
            }
            String rowString = rowStringBuilder.toString();

            if (rowString.matches(regex)) {
                return true;
            }
            ;
        }
        return false;
    }

    public boolean isFull() {
        for (int[] aRow : board) {
            for (int elem : aRow) {
                if (elem == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printBoard() {
        System.out.flush();

        // PRINT HEADER
        String columnsLetters = "ABCDEFGHIJKLMNOPQRSTUVXYZ";
        System.out.print("\t");

        for (int i = 0; i < board[0].length; i++) {
            System.out.print(i + "\t");
        }
        System.out.println();

        // PRINT ROWS
        char printElem = ' ';
        for (int i = 0; i < board.length; i++) {
            System.out.print(columnsLetters.substring(i, i + 1) + "\t");
            for (int elem : board[i]) {
                switch (elem) {
                    case 0:
                        printElem = '.';
                        break;
                    case 1:
                        printElem = 'X';
                        break;
                    case 2:
                        printElem = 'O';
                        break;
                    default:
                        printElem = ' ';
                }
                System.out.print(printElem + "\t");
            }
            System.out.println();
        }
    }

    public void printResult(int player) {
        switch (player) {
            case 0:
                System.out.println("It's a tie!");
            case 1:
                System.out.println("X won!");
            case 2:
                System.out.println("0 won!");
        }
    }

    public void enableAi(int player) {
        if (player == 1) {
            ifPlayer1AI = true;
        } else if (player == 2) {
            ifPlayer2AI = true;
        }
    }

    public void play(int howMany) {
        // PRINT THE STARTER BOARD

//        Player 1 starts the game
        int player = 1; // FIRST PLAYER
        int[] coordinates;

        while (1 == 1) {
            printBoard();

            if ((player == 1 && ifPlayer1AI) || (player == 2 && ifPlayer2AI)) {
                try {
                    Thread.sleep(1000);//time is in ms (1000 ms = 1 second)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                coordinates = getAiMove(player);
            } else {
                coordinates = getMove(player);
            }

            if (coordinates[0] == -1 && coordinates[1] == -1) {
                System.out.println("You left the game. Thank you for playing with COOLCODERS!");
                break;
            }
            mark(player, coordinates[0], coordinates[1]);
            printBoard();

//        The game ends when someone wins or the board is full
            if (hasWon(player, howMany)) {
                if (isFull()) {
                    player = 0;
                }
                printResult(player);
                System.out.println("Thank you for choosing COOLCODERS AGAIN!");
                break;
            }

//        Players alternate their moves (1, 2, 1, 2...)
            if (player == 1) {
                player = 2;
            } else {
                player = 1;
            }
        }

//        The game uses howMany to set the win condition
//        The game handles bad input(wrong coordinates) without crashing


    }
}
