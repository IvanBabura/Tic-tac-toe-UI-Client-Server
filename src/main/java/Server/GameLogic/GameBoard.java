package Server.GameLogic;

//
//Game Board class
//
public class GameBoard {
    private static int countGameBoards = 0;
    private static int idGameBoard;
    private int rank; //number of rows and/or columns on the board
    private char[][] board;

    public GameBoard() {
        idGameBoard = countGameBoards++;
    }

    public int getIdGameBoard() {
        return idGameBoard;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public char[][] getBoard() {
        return board;
    }

    public void createBoard(int rank){
        setRank(rank);
        board = new char[rank][rank];
        for(int i = 0; i < rank; i++) {
            for (int j = 0; j < rank; j++)
                board[i][j] = WhoseTurn.Empty.getValue();
        }
    }
}
