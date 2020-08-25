package com.codecool.fiveinarow;

import java.util.Scanner;
import java.util.Random;

public class Game implements GameInterface {

    private int[][] board;
    private boolean ifPlayer1AI = false;
    private boolean ifPlayer2AI = false;
    private boolean godMode = false;
    int howManyGlobal;
    boolean preventMode = false;

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

    public int[] getAiWinnerMove(int player, boolean ifPreventMode, boolean ifNextRightWinningMove) {
        StringBuilder regexBuildForWin = new StringBuilder();
        int startsCheckFrom = (ifPreventMode) ? howManyGlobal - 2 : 1;   // if prevent, check -1 and -2 to winning row
        int startIfNextWinning = (ifNextRightWinningMove) ? howManyGlobal - 1 : 0;
        int winnerColumn = -1;

        for (int howManyIter = startsCheckFrom; howManyIter < howManyGlobal; howManyIter++) {
            if (ifPreventMode && godMode) {
                System.out.print("TRY TO FIND AND PREVENT ENEMY'S WIN: ");
                System.out.print(howManyIter);
            }

            // TODO regex expand for 1.1.1 and 11.1 and 1.11 cases
            regexBuildForWin.append(".*");
            for (int i = 0; i < howManyIter; i++) {
                regexBuildForWin.append("[" + Integer.toString(player) + "]");
            }
            regexBuildForWin.append(".*");
            String regex = regexBuildForWin.toString();
            System.out.println("REGEX: " + regex);

            for (int winnerRow = startIfNextWinning; winnerRow < board.length; winnerRow++) {
                StringBuilder rowStringBuilder = new StringBuilder();
                for (int columnForAppend = 0; columnForAppend < board[0].length; columnForAppend++) {
                    rowStringBuilder.append(board[winnerRow][columnForAppend]);
                }
                String rowString = rowStringBuilder.toString();

                if (rowString.matches(regex)) {
                    for (int winnerColumnCheck = 0; winnerColumnCheck < board[0].length - 2; winnerColumnCheck++) {
                        String twoCharForCheck = rowString.substring(winnerColumnCheck, winnerColumnCheck + 2);

                        if (godMode) {
                            System.out.print(twoCharForCheck);
                            System.out.print("-");
                            System.out.print(twoCharForCheck);
                            System.out.print(" ");
                        }
                        if ((twoCharForCheck.equals(Integer.toString(player) + "0"))) {
                            winnerColumn = winnerColumnCheck + 1;
                        } else if (twoCharForCheck.equals("0" + Integer.toString(player))) {
                            winnerColumn = winnerColumnCheck;
                        }
                        if (winnerColumn != -1) {
                            if (godMode) {
                                System.out.println();
                                System.out.println("---------WINNER COORDINATES FOUND!---------");
                            }
                            int[] winnerCoordinates = {winnerRow, winnerColumn};
                            return winnerCoordinates;
                        }
                    }
                    System.out.println();
                }
            }
        }
        int[] winnerCoordinates = {-1, -1};   // return if not match for winner coordinates
        return winnerCoordinates;
    }


    public int[] getAiMove(int player) {
        int[] coordinates = {-1, -1};

        if (isFull()) {
            return null;
        }
        int row, col;
        Random rand = new Random();

        // GET WHETHER NEXT MOVE IS WINNING
        coordinates = getAiWinnerMove((player == 1) ? 2 : 1, true, false);
        if (coordinates[0] != -1 && coordinates[1] != -1) {
            return coordinates;
        }

        // GET ENEMY WINNING MOVE
        if (preventMode) {
            coordinates = getAiWinnerMove((player == 1) ? 2 : 1, true, false);
        }
        // GET WINNING MOVE if the enemy is not in a winning state
        if (coordinates[0] == -1 && coordinates[1] == -1) {
            coordinates = getAiWinnerMove(player, false, false);
        }

        if (coordinates[0] == -1 && coordinates[1] == -1) {
            // GET RANDOM MOVE IF NO WINNING STATES
            while (1 == 1) {
                row = rand.nextInt(board.length);
                col = rand.nextInt(board[0].length);
                if (board[row][col] == 0) {
                    break;
                }
            }
            int[] randomCoordinates = {row, col};
            return randomCoordinates;     // return random coordinates, if there is no winner ones.
        } else {
            return coordinates;     // return WINNER coordinates, if there is some.
        }
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
        // TODO clear screen

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
                break;
            case 1:
                System.out.println("X won!");
                break;
            case 2:
                System.out.println("0 won!");
                break;
        }
    }

    public void enableAi(int player) {
        if (player == 1) {
            ifPlayer1AI = true;
        } else if (player == 2) {
            ifPlayer2AI = true;
        }
    }

    public void enableGodMode() {
        godMode = true;
    }

    public void enablePreventMode() {
        preventMode = true;
    }

    public void play(int howMany) {
        // PRINT THE STARTER BOARD
        printBoard();
        howManyGlobal = howMany;

//        Player 1 starts the game
        int player = 1; // FIRST PLAYER
        int[] coordinates;

        while (1 == 1) {

            System.out.println("Player" + player + "'s turn.");
            if ((player == 1 && ifPlayer1AI) || (player == 2 && ifPlayer2AI)) {
                System.out.println("AI is thinking.");
                try {
                    Thread.sleep(10);//time is in ms (1000 ms = 1 second)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                coordinates = getAiMove(player);
            } else {
                coordinates = getMove(player);
            }

            if (isFull()) {
                player = 0;
                printResult(player);
                break;
            }

            if (coordinates[0] == -1 && coordinates[1] == -1) {
                System.out.println("You left the game. Thank you for playing with COOLCODERS!");
                break;
            }
            mark(player, coordinates[0], coordinates[1]);
            printBoard();

//        The game ends when someone wins or the board is full
            if (hasWon(player, howMany)) {
                printResult(player);
                break;
            }

//        Players alternate their moves (1, 2, 1, 2...)
            if (player == 1) {
                player = 2;
            } else {
                player = 1;
            }
        }
        System.out.println("Thank you for choosing COOLCODERS AGAIN!");
    }
}
