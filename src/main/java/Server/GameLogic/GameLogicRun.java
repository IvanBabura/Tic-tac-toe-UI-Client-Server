package Server.GameLogic;

import java.io.IOException;

public class GameLogicRun implements Runnable {
    private final Game game;
    private WhoseTurn whoseTurn = WhoseTurn.Cross;
    private WhoseTurn winner = WhoseTurn.Empty;

    public GameLogicRun(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        String message = "";
        try {
            if (((int) (Math.random() * 2)) == 1) {
                Player tempPlayer1 = game.getPlayerOne();
                Player tempPlayer2 = game.getPlayerTwo();
                game.setPlayerTwo(tempPlayer1);
                game.getPlayerTwo().setWhatMySymbol(WhoseTurn.Zero);
                game.setPlayerOne(tempPlayer2);
                game.getPlayerOne().setWhatMySymbol(WhoseTurn.Cross);
            }
            String tempStrRank = "0";

            game.getPlayerTwo().sendMessage("Your opponent chooses the board size.");
            int countOfAttempt = 5;

            while (countOfAttempt > 0 && (Integer.parseInt(tempStrRank) <= 2 || Integer.parseInt(tempStrRank) > 10)) {
                countOfAttempt--;
                game.getPlayerOne().sendMessage("Enter a value for the rank of the board (from 3 to 10): ");
                tempStrRank = game.getPlayerOne().getMessage();
                if (tempStrRank.equals("")) {
                    tempStrRank = "0";
                }
            }
            if (countOfAttempt <= 0) {
                tempStrRank = "3";
                message = "The player could not decide on the size of the board.\n" +
                        "The size will be set " + game.getGameBoard().getRank() + "x" + game.getGameBoard().getRank() + ".";
                game.getPlayerOne().sendMessage(message);
                game.getPlayerTwo().sendMessage(message);
            }
            game.getGameBoard().createBoard(Integer.parseInt(tempStrRank));

            //"rank" - this is a crutch trigger for the formation of the client board
            game.getPlayerOne().sendMessage("rank");
            game.getPlayerTwo().sendMessage("rank");
            game.getPlayerOne().sendMessage(tempStrRank);
            game.getPlayerTwo().sendMessage(tempStrRank);

            //send the players their "symbol"
            game.getPlayerOne().sendMessage(String.valueOf(game.getPlayerOne().getWhatMySymbol().getValue()));
            game.getPlayerTwo().sendMessage(String.valueOf(game.getPlayerTwo().getWhatMySymbol().getValue()));

            message = "\nBoard size " + game.getGameBoard().getRank() + "x" + game.getGameBoard().getRank() + ".";
            game.getPlayerOne().sendMessage(message);
            game.getPlayerTwo().sendMessage(message);

            //Start the game
            while (winner == WhoseTurn.Empty && whoseTurn != WhoseTurn.Error) {
                getStepFromPlayerAndDoIt();
            }

            //send the players trigger for end game
            game.getPlayerOne().sendMessage("winner");
            game.getPlayerTwo().sendMessage("winner");
            if (whoseTurn == WhoseTurn.Error) {
                message = " === Your rival has come out === ";
            } else {
                message = " End of the game.\n" +
                        "<<< Player winner is " + winner + " >>> ";
            }
            game.getPlayerOne().sendMessage(message);
        } catch (Exception e) {
            whoseTurn = WhoseTurn.Error;
            message = "!!! There has been a breakdown !!!";
            e.printStackTrace();
        }finally {
            try {
                game.getPlayerOne().sendMessage(message);
                game.getPlayerTwo().sendMessage(message);

                game.getPlayerOne().sendMessage("exit");
                game.getPlayerTwo().sendMessage("exit");

                game.getPlayerOne().closeConnections();
                game.getPlayerTwo().closeConnections();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    private void getStepFromPlayerAndDoIt() {
        String gettingMessage = "";
        try {
            int row, column;
            WhoseTurn tempWhoseStartTurn = whoseTurn;
            Player whoseTurnPlayer;
            Player whoseWaitPlayer;
            while (tempWhoseStartTurn == whoseTurn) {
                if (whoseTurn == WhoseTurn.Cross) {
                    whoseTurnPlayer = game.getPlayerOne();
                    whoseWaitPlayer = game.getPlayerTwo();
                } else {
                    whoseTurnPlayer = game.getPlayerTwo();
                    whoseWaitPlayer = game.getPlayerOne();
                }
                whoseTurnPlayer.sendMessage("!wait");
                whoseTurnPlayer.sendMessage("Select a cell or enter х:х");

                whoseWaitPlayer.sendMessage("wait");
                whoseWaitPlayer.sendMessage("Waiting for the opponent's move...");

                gettingMessage = whoseTurnPlayer.getMessage();
                try {
                    String[] coordinates = gettingMessage.split(":");
                    row = Integer.parseInt(coordinates[0]);
                    column = Integer.parseInt(coordinates[1]);
                    doStep(row, column);
                    checkWinner(row, column);
                    whoseWaitPlayer.sendMessage("" + row + ":" + column);
                } catch (ArrayIndexOutOfBoundsException e) {
                    whoseTurnPlayer.sendMessage("You entered something wrong. Repeat.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            whoseTurn = WhoseTurn.Error;
            try {
                game.getPlayerOne().closeConnections();
                game.getPlayerTwo().closeConnections();
            } catch (Exception ignored) {
            }
        }
    }

    private void doStep(int x, int y) {
        char[][] board = game.getGameBoard().getBoard();
        int rank = game.getGameBoard().getRank();
        if (x >= 0 && x <= rank && y >= 0 && y <= rank) {
            if (board[x][y] == WhoseTurn.Empty.getValue()) {
                board[x][y] = whoseTurn.getValue();
                if (whoseTurn == WhoseTurn.Cross) {
                    whoseTurn = WhoseTurn.Zero;
                } else {
                    whoseTurn = WhoseTurn.Cross;
                }
            }
        }
    }

    private void checkWinner(int x, int y) {
        char[][] board = game.getGameBoard().getBoard();
        char checkValue = board[x][y];
        int rank = game.getGameBoard().getRank();
        boolean win = true;
        if (x == y || (x == ((rank - y) - 1)) || (y == ((rank - x) - 1))) {
            //diagonal falling
            for (int i = 0; i < rank; i++) {
                if (board[i][i] != checkValue) {
                    win = false;
                    break;
                }
            }
            checkWinner(win);

            //ascending diagonal
            win = true;
            for (int i = 0; i < rank; i++) {
                if (board[(rank - i) - 1][i] != checkValue) {
                    win = false;
                    break;
                }
            }
            checkWinner(win);
        }

        //horizontal
        win = true;
        for (int i = 0; i < rank; i++) {
            if (board[x][i] != checkValue) {
                win = false;
                break;
            }
        }
        checkWinner(win);

        //vertical
        win = true;
        for (int i = 0; i < rank; i++) {
            if (board[i][y] != checkValue) {
                win = false;
                break;
            }
        }
        checkWinner(win);
    }

    private void checkWinner(boolean win) {
        if (win) {
            if (whoseTurn == WhoseTurn.Cross) {
                winner = WhoseTurn.Zero;
            } else {
                winner = WhoseTurn.Cross;
            }
        }
    }
}
