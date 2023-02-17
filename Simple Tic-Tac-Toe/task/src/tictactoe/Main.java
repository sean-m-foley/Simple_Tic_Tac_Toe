package tictactoe;

import java.util.Scanner;

public class Main {
    public enum GAME_STATES {NOT_FINISHED, IMPOSSIBLE, DRAW, X_WINS, O_WINS}
    public static final int GAME_COLUMNS = 3;
    public static final int GAME_ROWS = 3;

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        char [][] gameBoard = new char[GAME_ROWS][GAME_COLUMNS];
        char [] currentPlayer = {'X', 'O'};

        // You only need random fill when you have an empty game
        // randomFillGame(gameBoard, GAME_ROWS, GAME_COLUMNS);


        /// used to develop the program and quickly input boards
        // inputGameByLine(gameBoard, s);
        fillGameWithBlanks(gameBoard);
        printGame(gameBoard);
        System.out.println();

        // start asking for moves,
        GAME_STATES gameState = checkPlayerStatus(gameBoard);

        while (gameState != GAME_STATES.DRAW && gameState != GAME_STATES.O_WINS && gameState != GAME_STATES.X_WINS) {
            for (char player : currentPlayer) {
                boolean inputState = false;

                gameState = checkPlayerStatus((gameBoard));
                if (gameState != GAME_STATES.NOT_FINISHED) {
                    break;
                }

                while (!inputState) {
                    inputState = inputGameMove(gameBoard, s, player);
                    printGame(gameBoard);
                    System.out.println();
                }
            }
        }
        System.out.printf("%s", scoreGame(gameBoard));
    }

    public static void printGame(final char [][]gameBoard) {
        System.out.print("-".repeat(GAME_COLUMNS + 6));

        for (int i = 0; i < GAME_ROWS; i++) {
            System.out.print("\n| ");
            for (int j = 0; j < GAME_COLUMNS; j++) {
                System.out.print(gameBoard[i][j] + " ");
            }
            System.out.print("|");
        }

        System.out.print("\n" + "-".repeat(GAME_COLUMNS + 6));
    }


    public static void fillGameWithBlanks(char [][] gameBoard) {
        for (int i = 0; i < GAME_ROWS; i++) {
            for (int j = 0; j < GAME_COLUMNS; j++) {
                gameBoard[i][j] = '_';
            }
        }
    }
    /* Fills the game board with random X's and O's for testing */
    public static void randomFillGame(char [][] gameBoard) {
        int totalXs = 0;
        int totalOs = 0;

        for (int i = 0; i < GAME_ROWS; i++) {
            for (int j = 0; j < GAME_COLUMNS; j++) {
                double randNum = Math.random() * 2;  // forces results 0 or 1;
                char xOrO;

                if (randNum <= 0.99 && totalXs <= totalOs) {
                    totalXs++;
                    xOrO = 'X';
                } else {
                    totalOs++;
                    xOrO = 'O';
                }
                gameBoard[i][j] = xOrO;
            }
        }
    }

    public static void inputGameByLine(char [][] gameBoard, final Scanner s) {
        String input = s.next();
        int inputIndex = 0;

        for (int i = 0; i < GAME_ROWS; i++){
            for (int j = 0; j < GAME_COLUMNS && inputIndex < input.length(); j++ ) {
                    gameBoard[i][j] = input.charAt(inputIndex++); // this can be X, O, _
            }
        }
    }

    public static GAME_STATES checkPlayerStatus(char [][] gameBoard) {
        char[] currentPlayer = {'X', 'O'};                  // we'll check X first, then O
        int x = 0; int o = 0; int blanks = 0;
        boolean xWin = false; boolean oWin = false;

        // get a count, we can do some early exits
        for (int i = 0; i < GAME_ROWS; i++) {
            for (int j = 0; j < GAME_COLUMNS; j++) {
                switch (gameBoard[i][j]) {
                    case 'X' -> x++;
                    case 'O' -> o++;
                    case '_' -> blanks++;
                }
            }
        }

        if (x >= o ? x - o >= 2 : o - x >= 2) {
            // this is an impossible game, there can only be
            // there can be only one more letter than another
            return GAME_STATES.IMPOSSIBLE;
        }

        // check the horizontal
        for (char player : currentPlayer) {
            for (int r = 0; r < GAME_ROWS; r++) {
                boolean matched = false;
                for (int c = 0; c < GAME_COLUMNS; c++) {
                    if (gameBoard[r][c] == player && c == 0) {
                        // the first index has to be the player we're checking,
                        // or we can ignore the rest, the row will not be true.
                        matched = true;
                    } else if (gameBoard[r][c] != player) {
                        matched = false;
                        break;      // we can stop looking here
                    }  // we can just leave it as true if gameBoard[r][c] == player
                }

                if (matched) {
                    switch (player) {
                        case 'X' -> xWin = true;
                        case 'O' -> oWin = true;
                    }
                }
            }
        }

        // check verticals now, basically the same code as above
        for (char player : currentPlayer) {
                for (int c = 0; c < GAME_COLUMNS; c++) {
                    boolean matched = false;
                    for (int r = 0; r < GAME_ROWS; r++) {
                        if (gameBoard[r][c] == player && r == 0) {
                            matched = true;
                        } else if (gameBoard[r][c] != player) {
                            matched = false;
                            break;
                        }
                    }

                    if(matched) {
                        switch (player) {
                            case 'X' -> xWin = true;
                            case 'O' -> oWin = true;
                        }
                    }
                }

        }

        for (char player : currentPlayer) {
            // we can do a short circuit here
            // if the current player isn't in the middle square of
            // the board we can get out, there is no reason to keep checking
            if (gameBoard[GAME_ROWS / 2][GAME_COLUMNS / 2] != player) {
                // skip
                continue;
            }

            boolean matched = false;
            // checks top left to bottom right
            for (int r = 0, c = 0; r < GAME_ROWS; r++, c++) {
                if (gameBoard[r][c] == player && r == 0) {
                    matched = true;
                } else if (gameBoard[r][c] != player) {
                    matched = false;
                    break;  // skip it
                }
            }

            if (matched) {
                switch(player) {
                    case 'X' -> xWin = true;
                    case 'O' -> oWin = true;
                }
            }

            matched = false;

            // checks top right to bottom left
            for (int r = 0, c = GAME_COLUMNS - 1; r < GAME_ROWS; r++, c--) {
                if (gameBoard[r][c] == player && c == 0) {
                    matched = true;
                } else if (gameBoard[r][c] != player) {
                    matched = false;
                    break;
                }
            }

            if (matched) {
                switch (player) {
                    case 'X' -> xWin = true;
                    case 'O' -> oWin = true;
                }
            }
        }

        if (xWin && oWin) {
            return GAME_STATES.IMPOSSIBLE;
        } else if (xWin) {
            return GAME_STATES.X_WINS;
        } else if (oWin) {
            return GAME_STATES.O_WINS;
        } else if (blanks == 0) {
            return GAME_STATES.DRAW;
        } else {
            return GAME_STATES.NOT_FINISHED;
        }
    }

    public static String scoreGame(char [][] gameBoard) {
        // first we will evaluate for impossible or games that have not finished.
        return switch(checkPlayerStatus(gameBoard)) {
            case X_WINS -> "X wins";
            case O_WINS -> "O wins";
            case DRAW -> "Draw";
            case IMPOSSIBLE -> "Impossible";
            case NOT_FINISHED -> "Game not finished";
        };
    }

    public static boolean inputGameMove(char [][] gameBoard, Scanner s, char player) {
        // this returns false if the inputs are bad, true if they're good
        // it will only process one input at a time
        int[] enteredNumbers = { 0, 0 };
        boolean error = false;

        try {
            // it's possible that this fails, if it works then
            enteredNumbers[0] = Integer.parseInt(s.next());
        } catch (Exception e) {
            error = true;
        }

        try {
            // it's possible that this fails, if it works then
            enteredNumbers[1] = Integer.parseInt(s.next());
        } catch (Exception e) {
            error = true;
        }

        if (error) {
            System.out.println("You should enter numbers!");
            return false;
        }

        if ((enteredNumbers[0] < 1 || enteredNumbers[0] > GAME_ROWS) ||
                (enteredNumbers[1] < 1 || enteredNumbers[1] > GAME_COLUMNS)) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        }

        enteredNumbers[0]--; enteredNumbers[1]--;

        if (gameBoard[enteredNumbers[0]][enteredNumbers[1]] == '_') {
            gameBoard[enteredNumbers[0]][enteredNumbers[1]] = player;
            return true;
        } else {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }
    }
}
