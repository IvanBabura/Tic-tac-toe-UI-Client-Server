package Server.GameLogic;

public enum WhoseTurn {
    Zero {public char getValue(){return '0';} },
    Cross {public char getValue(){return 'X';} },
    Empty {public char getValue(){return ' ';} },
    Error {public char getValue(){return 'e';} };
    public abstract char getValue();
}
