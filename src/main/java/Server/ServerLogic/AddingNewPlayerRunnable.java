package Server.ServerLogic;

import Server.GameLogic.Game;
import Server.GameLogic.GameLogicRun;
import Server.GameLogic.Player;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class AddingNewPlayerRunnable implements Runnable {
    private final ServerGUI serverGUI;
    LinkedList<Game> listGames;
    Player player;

    public AddingNewPlayerRunnable(ServerGUI serverGUI, LinkedList<Game> listGames, Player player) {
        this.serverGUI = serverGUI;
        this.listGames = listGames;
        this.player = player;
    }

    @Override
    public void run() {
        boolean successfulConnection = false;
        try {
            String message;
            Game game = null;

            //connecting new client to the board
            if (listGames.isEmpty()) {
                game = createNewBoard();
            }
            for (Game listGame : listGames) {
                game = listGame;
                if (game.hasNoPlayers()) {
                    player.sendMessage("Joining a new empty game...");
                    successfulConnection = attemptToJoinPlayerToGame(player, game);
                    break;
                }
                if (game.waitSecondPlayer()) {
                    serverGUI.printToFormLn("We are awaiting confirmation from Player №" + player.getId());
                    message = "There is an open play.\n\n" + game.infoOfThisBoard()
                            + "\n\tJoin?\nWrite \"yes\", if you want to join.\n " +
                            "Otherwise I will look for a new game.";
                    player.sendMessage(message);
                    String clientAnswer = player.getMessage();
                    if (Objects.equals(clientAnswer, "yes")) {
                        successfulConnection = attemptToJoinPlayerToGame(player, game);
                        break;
                    }
                }
            }
            if (!successfulConnection) {
                game = createNewBoard();
                successfulConnection = attemptToJoinPlayerToGame(player, game);
                player.sendMessage("There are no open games. Joining a new empty game.");
            }
            if (successfulConnection) {
                if (game.fullPlayersOnThisBoard()) {
                    serverGUI.printToFormLn("The game begins on " + game.infoOfThisBoard());
                    GameLogicRun gameLogicRun = new GameLogicRun(game);
                    Thread gameLogicRunThread = new Thread(gameLogicRun, "gameStart");
                    gameLogicRunThread.setDaemon(true);
                    gameLogicRunThread.start();
                } else {
                    serverGUI.printToFormLn("There are not enough players on board №" + game.getGameBoard().getIdGameBoard());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean attemptToJoinPlayerToGame(Player player, Game game) {
        serverGUI.printToFormLn(" <Joining a player to the board> ");
        try {
            player.sendMessage("Attempting to connect to the board...");
            if (!game.addPlayer(player)) {
                String message = "Unable to join on" + game.infoOfThisBoard();
                player.sendMessage(message);
                serverGUI.printToFormLn(message);
                return false;
            } else {
                serverGUI.printToFormLn("Joined Player №" + player.getId() + " on " + game.infoOfThisBoard());
                player.sendMessage("Welcome to our server!\nYou connected to the board №"
                        + game.getGameBoard().getIdGameBoard()
                        + ".\nWaiting for an opponent to connect...");
                return true;
                //successfulConnection = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            serverGUI.printToFormLn("Player № "  + player.getId() +  " Joining Error on " + game.infoOfThisBoard());
            try {
                player.sendMessage("Board join error.");
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private Game createNewBoard() {
        Game game = new Game();
        listGames.add(game);
        serverGUI.printToFormLn("<New board> I created: " + listGames.getLast().infoOfThisBoard());
        return game;
    }

}
