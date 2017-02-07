package model;

import java.util.ArrayList;

/**
 * Created by PAVEL-PC on 21.01.2017.
 */
public abstract class Game {

    public abstract void makeMove(Move move,playerType playerType);
    public abstract void undoMove(Move move,playerType playerType);
    public abstract ArrayList<Move> generateValidMoves(playerType playerType);
    public abstract int score(Move move, playerType playerType);
    public abstract int isGameOver();
    public abstract void incDepth();
    public abstract void decDepth();
    public abstract int getComplexity();
    public abstract int getDepth();

}
