package model;

import java.util.ArrayList;

/**
 * Created by PAVEL-PC on 12.01.2017.
 */
public class AlphaBetaPruningAI {
    
    public AlphaBetaPruningAI(){
        
    }

    public Move playBestMove(Model theModel, int alpha, int beta){

        Move bestMove = new Move(-1,-1,-1);
        bestMove.value=-Integer.MAX_VALUE;
        ArrayList<Move> validMoves = theModel.generateValidMoves(playerType.COMPUTER);
        for(int i=0;i<validMoves.size();i++) {
            theModel.makeMove(validMoves.get(i), playerType.COMPUTER);
            theModel.incDepth();
            Move nextMove = min(theModel, alpha, beta, validMoves.get(i));
            if (nextMove.value > bestMove.value) {
                bestMove = validMoves.get(i);
                bestMove.value = nextMove.value;
                alpha = bestMove.value;
            }
            theModel.undoMove(validMoves.get(i),playerType.COMPUTER);
            theModel.decDepth();
            if(alpha>=beta)
                return bestMove;
        }
        return bestMove;
    }
    
    public Move min(Model theModel, int alpha, int beta, Move lastMove2){

        if(theModel.isGameOver()!=0 || theModel.getDepth()==theModel.getComplexity()) {
            lastMove2.value=theModel.score(lastMove2,playerType.PLAYER);
            return lastMove2;
        }
        int cAlpha=alpha;
        int cBeta=beta;
        Move bestMove = new Move(-1,-1,-1);
        bestMove.value=Integer.MAX_VALUE;
        ArrayList<Move> validMoves = theModel.generateValidMoves(playerType.PLAYER);
        for(int i=0;i<validMoves.size();i++) {
            theModel.makeMove(validMoves.get(i), playerType.PLAYER);
            theModel.incDepth();
            Move nextMove = max(theModel, cAlpha, cBeta, validMoves.get(i));
            if (nextMove.value < bestMove.value) {
                bestMove = validMoves.get(i);
                bestMove.value = nextMove.value;
                cBeta = bestMove.value;
            }
            theModel.undoMove(validMoves.get(i),playerType.PLAYER);
            theModel.decDepth();
            if(cAlpha>=cBeta)
                return bestMove;
        }
        return bestMove;
    }
    
    public Move max(Model theModel, int alpha, int beta, Move lastMove1){

        if(theModel.isGameOver()!=0 || theModel.getDepth()==theModel.getComplexity()) {
            lastMove1.value=theModel.score(lastMove1,playerType.COMPUTER);
            return lastMove1;
        }
        int cAlpha=alpha;
        int cBeta=beta;
        Move bestMove = new Move(-1,-1,-1);
        bestMove.value=-Integer.MAX_VALUE;
        ArrayList<Move> validMoves = theModel.generateValidMoves(playerType.COMPUTER);
        for(int i=0;i<validMoves.size();i++) {
            theModel.makeMove(validMoves.get(i), playerType.COMPUTER);
            theModel.incDepth();
            Move nextMove = min(theModel, cAlpha, cBeta, validMoves.get(i));
            if (nextMove.value > bestMove.value) {
                bestMove = validMoves.get(i);
                bestMove.value = nextMove.value;
                cAlpha = bestMove.value;
            }
            theModel.undoMove(validMoves.get(i),playerType.COMPUTER);
            theModel.decDepth();
            if(cAlpha>=cBeta)
                return bestMove;
        }
        return bestMove;
    }
}
