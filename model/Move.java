package model;

/**
 * Created by PAVEL-PC on 12.01.2017.
 */
public class Move {

    public int value;
    public int src;
    public int dst;
    public int removePiece;
    //public moveType moveType;

    public Move(int src, int dst, int removePiece/*, moveType moveType*/)
    {
        this.src=src;
        this.dst=dst;
        //this.moveType=moveType;
        this.removePiece=removePiece;
    }

}
