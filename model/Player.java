package model;

/**
 * Created by PAVEL-PC on 12.01.2017.
 */
public class Player {

    private int numPieces;
    private int numPiecesOnBoard;
    private boolean canFly;
    private playerType playerType;
    private int Phase;

    Player(playerType playerType){

        Phase=1;
        numPieces=9;
        numPiecesOnBoard=0;
        canFly=false;
        this.playerType=playerType;

    }

    public void setPhase(int Phase){
        this.Phase=Phase;
    }

    public int getPhase() {
        return Phase;
    }

    public int getNumPiecesOnBoard() {
        return numPiecesOnBoard;
    }

    public void removePiece(){
        numPieces--;
        numPiecesOnBoard--;
    }

    public void undoRemovePiece(){
        numPieces++;
        numPiecesOnBoard++;
    }

    public int getNumPiecesLeftToPlace() {
        return (numPieces - numPiecesOnBoard);
    }

    public playerType getPlayerType() {
        return playerType;
    }

    public boolean canItFly() {
        return canFly;
    }

    public void raiseNumPiecesOnBoard() {
        numPiecesOnBoard++;
    }

    public void lowerNumPiecesOnBoard() {
        numPiecesOnBoard--;
    }

}
