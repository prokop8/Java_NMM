package model;

/**
 * Created by PAVEL-PC on 12.01.2017.
 */
public class Position {

    private boolean isOccupied;
    private int positionIndex;
    private playerType playerOccupying;
    private int[] adjacentPositionsIndexes;

    Position(int index){

        isOccupied=false;
        playerOccupying=playerType.NOONE;
        positionIndex=index;

    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public playerType getPlayerOccupyingIt() {
        return playerOccupying;
    }

    public void setAsOccupied(playerType player) {
        isOccupied = true;
        playerOccupying = player;
    }

    public playerType setAsUnoccupied() {
        isOccupied = false;
        playerType oldPlayer = playerOccupying;
        playerOccupying = playerType.NOONE;
        return oldPlayer;
    }

    public void addAdjacentPositionsIndexes(int pos1, int pos2) {
        adjacentPositionsIndexes = new int[2];
        adjacentPositionsIndexes[0] = pos1;
        adjacentPositionsIndexes[1] = pos2;
    }

    public void addAdjacentPositionsIndexes(int pos1, int pos2, int pos3) {
        adjacentPositionsIndexes = new int[3];
        adjacentPositionsIndexes[0] = pos1;
        adjacentPositionsIndexes[1] = pos2;
        adjacentPositionsIndexes[2] = pos3;
    }

    public void addAdjacentPositionsIndexes(int pos1, int pos2, int pos3, int pos4) {
        adjacentPositionsIndexes = new int[4];
        adjacentPositionsIndexes[0] = pos1;
        adjacentPositionsIndexes[1] = pos2;
        adjacentPositionsIndexes[2] = pos3;
        adjacentPositionsIndexes[3] = pos4;
    }

    public int[] getAdjacentPositionsIndexes() {
        return adjacentPositionsIndexes;
    }

    public boolean isAdjacentToThis(int posIndex) {
        for(int i = 0; i < adjacentPositionsIndexes.length; i++) {
            if(adjacentPositionsIndexes[i]== posIndex) {
                return true;
            }
        }
        return false;
    }

}
