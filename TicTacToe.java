//Elise Vickery
//10/23/2015
//Basic Tic Tac Toe game using no external libraries

package tictactoe;

import java.util.Scanner;


public class TicTacToe {

    public static String X_MARKED = "X";
    public static String O_MARKED = "O";
    private String[][] board; //grid on which Tic Tac Toe is played
    private int boardSize; //number of sites per side of the square board

    public TicTacToe() {
        super();
    }

    /**
     * Constructor that sets the board size as specified
     * @param size
     */
    public TicTacToe(int size) {
        super();
        setBoardSize(size);
    }

    /**
     * Set size of Tic Tac Toe Grid from the console
     * Only accepts ints greater than 1
     */
    public void setBoardSizeFromConsole() {
        int size = -1;
        boolean reminderPrompt = false;
        Scanner in = new Scanner(System.in);

        while (size <= 1) { //size must be an int greater than 1
            System.out.println("Enter the number of Xs or Os in a row you must get to win (traditionally, we use 3).");
            try {
                size = in.nextInt();
                reminderPrompt = size <= 1;
            }
            catch (Exception e) {
                if (in.hasNext()) { //clear next input (if any)
                    in.next();
                }
                reminderPrompt = true;
            }
            if (reminderPrompt) {
                System.out.println("Please enter an integer greater than one.");
                reminderPrompt = false;
            }
        }
        setBoardSize(size);
    }

    /**
     * Attempt to play an X or O (as indicated) in the location specified by user
     * @param playX = true, play an X (otherwise, play an O)
     * @return - int array of size indicating the row and column containing the newly-played X or O (null if none was played)
     */
    public int[] playXOrOAndDisplay(boolean playX) {
        Scanner in = new Scanner(System.in);
        int[] locationPlayed = null;
        int row = -1;
        int column = -1;
        String toPlay = playX ? X_MARKED : O_MARKED;
        System.out.println("Indicate the location of the next " + toPlay);
        try {
            row = in.nextInt();
            column = in.nextInt();
        }
        catch (Exception e) {
            if (in.hasNext()) { //clear next input (if any)
                in.next();
            }
            System.out.println("You can only enter integers between 0 and " + (getBoardSize() - 1) + ", inclusive");
            return locationPlayed;
        }
        try {
            placeXOrO(playX, /*whether x is being played*/row, column);
            printBoard();
            locationPlayed = new int[2];
            locationPlayed[0] = row;
            locationPlayed[1] = column;
        }
        catch (IllegalArgumentException ie) {
            System.out.println(ie.getMessage());
        }
        return locationPlayed;

    }

    /**
     * Play a game of Tic Tac Toe, prompting the player(s) for size of the game and then locations of Xs and Os
     * @param args
     */
    public static void main(String[] args) {
        TicTacToe ttt = new TicTacToe();
        ttt.setBoardSizeFromConsole(); //Get size of Tic Tac Toe Grid from user input
        System.out.println("To indicate where the X or O should be placed, enter integers between 0 and " + (ttt.getBoardSize() - 1) + ", inclusive, for the row and column, separated by a space.");
        System.out.println("For example, the top right spot would be entered as 0  " + (ttt.getBoardSize() - 1));

        boolean doesXWin = false;
        boolean doesOWin = false;
        int countTurns = 0;
        int[] rowColumnPlayed = new int[2];
        boolean playAgain = true;

        //Continue to prompt the player(s) for input while there is no winner and they have played fewer than the maximum turns
        while ((!doesXWin && !doesOWin) && countTurns < (int)(Math.pow(ttt.getBoardSize(), 2))) {
            while (playAgain) {
                rowColumnPlayed = ttt.playXOrOAndDisplay(true/*Playing X*/);
                playAgain = rowColumnPlayed == null;
            }
            countTurns++;//X has completed a turn
            //play again if they have played fewer than the maximum turns
            playAgain = countTurns < (int)(Math.pow(ttt.getBoardSize(), 2));
            //only check if X has won after it is possible, given the number of turns
            if (countTurns >= ttt.getBoardSize() * 2 - 1) {
                doesXWin = ttt.isSitePartofWinner(rowColumnPlayed[0], rowColumnPlayed[1]);
            }
            if (doesXWin) {
                System.out.println("X wins!");
                return;
            }

            while (playAgain) {
                rowColumnPlayed = ttt.playXOrOAndDisplay(false /*Playing O*/);
                playAgain = rowColumnPlayed == null;
            }
            countTurns++;//O has completed a turn
            //play again if they have played fewer than the maximum turns
            playAgain = countTurns < (int)(Math.pow(ttt.getBoardSize(), 2));
            //only check if O has won after it is possible, given the number of turns
            if (countTurns >= ttt.getBoardSize() * 2) {
                doesOWin = ttt.isSitePartofWinner(rowColumnPlayed[0], rowColumnPlayed[1]);
            }
            if (doesOWin) {
                System.out.println("O wins!");
                return;
            }
        }
        System.out.println("All sites have been filled, it is a tie!");
    }

    /** 
     * Determine whether specified site contains a value
     * @param row - value from 0 to (boardSize - 1)
     * @param column - value from 0 to (boardSize - 1)
     * @return
     */
    public boolean isSiteBlank(int row, int column) {
        return board[row][column] == null || board[row][column].isEmpty();
    }

    /**
     * Displays current state of board,
     * displaying vertical lines on the interior borders between sites
     * and horizontal lines on the interior borders of blank sites
     */
    public void printBoard() {
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (X_MARKED.equals(board[r][c]) || O_MARKED.equals(board[r][c])) {
                    System.out.print(board[r][c]);
                } else {
                    if (r < boardSize - 1) { //horizontal lines on the interior borders of blank sites
                        System.out.print("_");
                    } else {
                        System.out.print(" "); //blank spaces on the bottom border of blank sites
                    }
                }
                if (c < boardSize - 1) {
                    System.out.print("|"); //vertical lines on the interior borders between sites
                }
            }
            System.out.println("");
        }

    }

    /**
     * Places an X or O (as indicated) in the site indicated
     * @param placeX - true = place X, false = place O
     * @param row - value from 0 to (boardSize - 1)
     * @param column - value from 0 to (boardSize - 1)
     */
    public void placeXOrO(boolean placeX, int row, int column) {
        if (row >= boardSize || column >= boardSize || row < 0 || column < 0) {
            throw new IllegalArgumentException("Row and column must be integers between 0 and " + (boardSize - 1));
        } else if (!isSiteBlank(row, column)) {
            throw new IllegalArgumentException("Cannot select a spot that is already occupied.");
        }
        board[row][column] = placeX ? X_MARKED : O_MARKED;
    }


    /**
     * Determines whether site specified is part of the winning "streak"
     * assumes only Xs or Os are on the board
     * @param row - value from 0 to (boardSize - 1)
     * @param column - value from 0 to (boardSize - 1)
     * @return
     */
    public boolean isSitePartofWinner(int row, int column) {
        if (isSiteBlank(row, column)) {
            return false;
        }
        String givenSiteContains = X_MARKED.equals(board[row][column]) ? X_MARKED : O_MARKED;
        //Check diagonal(s)
        int countInARow = 0;
        if (row == column) { //check the 10:30 - 4:30 (clock) angle
            for (int rc = 0; rc < boardSize; rc++) {
                if (!givenSiteContains.equals(board[rc][rc])) {
                    break;
                }
                countInARow++;
            }
            if (countInARow == boardSize) {
                return true;
            }
        }

        countInARow = 0;
        if (row + column == boardSize - 1) { //check the 1:30 - 7:30 (clock) angle
            for (int r = 0; r < boardSize; r++) {
                if (!givenSiteContains.equals(board[r][boardSize - 1 - r])) {
                    break;
                }
                countInARow++;
            }
            if (countInARow == boardSize) {
                return true;
            }
        }
        countInARow = 0;
        //Check on Hortizontal
        for (int c = 0; c < boardSize; c++) {
            if (!givenSiteContains.equals(board[row][c])) {
                break;
            }
            countInARow++;
        }
        if (countInARow == boardSize) {
            return true;
        }
        countInARow = 0;
        //Check on Vertical
        for (int r = 0; r < boardSize; r++) {
            if (!givenSiteContains.equals(board[r][column])) {
                return false;
            }
            countInARow++;
        }
        if (countInARow == boardSize) {
            return true;
        }
        return false;
    }

    /**
     * Sets the size of the board and initializes the board to a square array of that size
     * @param gridSize - specified size of board
     */
    private void setBoardSize(int gridSize) {
        this.boardSize = gridSize;
        this.board = new String[boardSize][boardSize];

    }

    public int getBoardSize() {
        return boardSize;
    }
}
