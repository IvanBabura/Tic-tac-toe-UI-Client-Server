package Server.GameLogic;

//
//Implementation of the game itself
//
public class Game {
    private final GameBoard gameBoard;
    private Player playerOne = null;
    private Player playerTwo = null;

    public Game() {
        gameBoard = new GameBoard();
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public boolean addPlayer(Player player) {
        if (playerOne == null) {
            playerOne = player;
            playerOne.setWhatMySymbol(WhoseTurn.Cross);
        } else if (playerTwo == null) {
            playerTwo = player;
            playerTwo.setWhatMySymbol(WhoseTurn.Zero);
        } else return false;
        return true;
    }

    public String infoOfThisBoard() // Можно переделать под булеан
    {
        String result = "Board №" + gameBoard.getIdGameBoard() + ";";
        if (playerOne == null && playerTwo == null)
            result += "There is no one on this board.";
        else if (playerOne != null & playerTwo == null)
            result += "Player №" + playerOne.getId() + " Waiting for the enemy...";
        else if (playerOne == null)
            result += "Player №" + playerTwo.getId() + " Waiting for the enemy...";
        else result += "In game: №" + playerOne.getId() + " VS №" + playerTwo.getId();

        return result;
    }
    public boolean hasNoPlayers(){
        return playerOne == null && playerTwo == null;
    }

    public boolean waitSecondPlayer(){
        return (playerOne == null && playerTwo != null) || (playerOne != null && playerTwo == null);
    }

    public boolean fullPlayersOnThisBoard(){
        return playerOne != null && playerTwo != null;
    }

}
