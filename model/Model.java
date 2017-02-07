package model;

import java.util.ArrayList;

/**
 * Created by PAVEL-PC on 12.01.2017.
 */
public class Model extends Game{

    /*tablica graczy*/
    private Player[] Player;
    /*tablica pol*/
    private Position[] boardPositions;
    /*tablica mlynow*/
    private Position[][] millCombinations;
    private int complexity;
    private int depth;

    /*czy usuwamy punkt*/
    public boolean deleting = false;
    /*przesuwamy*/
    public boolean moveFromTwo = false;

    //private int Phase; //1, 2, 3  https://kartikkukreja.wordpress.com/2014/03/17/heuristicevaluation-function-for-nine-mens-morris/

    public Model(){

        boardPositions = new Position[24];
        //Phase = 1;
        Player = new Player[2];
        Player[0] = new Player(playerType.PLAYER);
        Player[1] = new Player(playerType.COMPUTER);
        initBoard();
        initMillCombinations();
        complexity=4;
        depth=0;



    }
    /**
     * funkcja ktora zwraca gracza
     * @param number
     * @return Player
     */
    public Player getPlayer(int number){
        return Player[number];
    }

    /**
     * funkcja ktora ustawia zlozonosc
     * @param complexity
     */
    public void setComplexity(int complexity){
        this.complexity=complexity;
    }

    /**
     * funkcja pobiera zlozonosc
     * @return complexity
     */
    public  int getComplexity(){
        return complexity;
    }

    /**
     * funkcja inkrementujaca glebokosc
     */
    public void incDepth(){
        depth++;
    }

    /**
     * funkcja dekrementujaca zlozonosc
     */
    public void decDepth(){
        depth--;
    }

    /**
     * funkcja pobierajaca glebokosc
     * @return depth
     */
    public int getDepth(){
        return depth;
    }

    /**
     * funkcja zwracajaca pozycje pod danym indeksem
     * @param posIndex
     * @return boardPosition
     */
    public Position getPosition(int posIndex){
        return boardPositions[posIndex];
    }

    /**
     * funkcja sprawdzajaca czy pozycja pod zadanym indeksem jest zajeta czy nie
     * @param posIndex
     * @return czy jest zajeta dana pozycja
     */
    public boolean positionIsAvailable(int posIndex){
        return !boardPositions[posIndex].isOccupied();
    }

    /**
     * funkcja zwracajaca kombinacje mlynu pod danym indeksem
     * @param index
     * @return millCombinations
     */
    public Position[] getMillCombination(int index){
        return millCombinations[index];
    }

    /**
     * funkcja ktora generuje mozliwe ruchy dla zadanego gracza
     * @param playerType
     * @return validMoves
     */
    public ArrayList<Move> generateValidMoves(playerType playerType){
        Position adjacent;
        int Phase;
        ArrayList<Move> validMoves = new ArrayList<>();
        if(playerType== model.playerType.PLAYER)
            Phase = Player[0].getPhase();
        else
            Phase = Player[1].getPhase();
        if(Phase==1) {
            for (int i = 0; i < 24; i++) {
                Move move = new Move(-1, -1, -1);
                if (!getPosition(i).isOccupied()) {
                    getPosition(i).setAsOccupied(playerType);
                    move.dst = i;
                    checkMove(playerType, validMoves, move);
                    getPosition(i).setAsUnoccupied();
                }
            }
        }
        else if(Phase==2) {
            for (int i = 0; i < 24; i++) {
                if (getPosition(i).getPlayerOccupyingIt() == playerType) {
                    int[] adjacents = getPosition(i).getAdjacentPositionsIndexes();
                    for (int j = 0; j < adjacents.length; j++) {
                        Move move = new Move(i, -1, -1);
                        adjacent = getPosition(adjacents[j]);
                        if (!adjacent.isOccupied()) {
                            adjacent.setAsOccupied(playerType);
                            move.dst = adjacents[j];
                            getPosition(i).setAsUnoccupied();
                            checkMove(playerType, validMoves, move);
                            getPosition(i).setAsOccupied(playerType);
                            adjacent.setAsUnoccupied();
                        }
                    }
                }
            }
        }
        else if(Phase==3){
            ArrayList<Integer> playerSpaces = new ArrayList<>();
            ArrayList<Integer> freeSpaces = new ArrayList<>();
            for(int i=0;i<24;i++){
                if(getPosition(i).getPlayerOccupyingIt() == playerType) {
                    playerSpaces.add(i);
                } else if(!getPosition(i).isOccupied()) {
                    freeSpaces.add(i);
                }
            }
            for(int i=0;i<playerSpaces.size();i++){
                Position src=getPosition(playerSpaces.get(i));
                src.setAsUnoccupied();
                for(int j=0;j<freeSpaces.size();j++){
                    Move move = new Move(src.getPositionIndex(), -1, -1);
                    Position dest = getPosition(freeSpaces.get(j));
                    dest.setAsOccupied(playerType);
                    move.dst = freeSpaces.get(j);
                    checkMove(playerType, validMoves, move);
                    dest.setAsUnoccupied();
                }
                src.setAsOccupied(playerType);
            }
        }
        return validMoves;
    }

    /**
     * funkcja ktora wykonuje zadany ruch przez zadanego gracza
     * @param move
     * @param playerType
     */
    public void makeMove(Move move,playerType playerType){
        Position position=getPosition(move.dst);
        position.setAsOccupied(playerType);
        int Phase;
        if(playerType== model.playerType.PLAYER)
            Phase = Player[0].getPhase();
        else
            Phase = Player[1].getPhase();

        if(Phase==1){
            if(Player[0].getPlayerType()==playerType) {
                Player[0].raiseNumPiecesOnBoard();
                if(Player[0].getNumPiecesLeftToPlace()==0)
                    Player[0].setPhase(2);
            }
            else {
                Player[1].raiseNumPiecesOnBoard();
                if(Player[1].getNumPiecesLeftToPlace()==0)
                    Player[1].setPhase(2);
            }
        }
        else{
            getPosition(move.src).setAsUnoccupied();
        }
        if(move.removePiece != -1) {
            getPosition(move.removePiece).setAsUnoccupied();
            if(Player[0].getPlayerType()==playerType) {
                Player[1].removePiece();
                if(Player[1].getNumPiecesOnBoard()==3 && Player[1].getPhase()==2)
                    Player[1].setPhase(3);
            }
            else {
                Player[0].removePiece();
                if(Player[0].getNumPiecesOnBoard()==3 && Player[0].getPhase()==2)
                    Player[0].setPhase(3);
            }
        }
    }

    /**
     * funkcja ktora cofa podany ruch przez danego gracza
     * @param move
     * @param playerType
     */
    public void undoMove(Move move, playerType playerType) {
        Position position = getPosition(move.dst);
        position.setAsUnoccupied();
        int Phase;
        if(playerType== model.playerType.PLAYER)
            Phase = Player[0].getPhase();
        else
            Phase = Player[1].getPhase();

        if (Phase == 1) {
            if (Player[0].getPlayerType() == playerType)
                Player[0].lowerNumPiecesOnBoard();
            else
                Player[1].lowerNumPiecesOnBoard();
        }
        else {
            if(move.src!=-1)
                getPosition(move.src).setAsOccupied(playerType);
            else{
                if (Player[0].getPlayerType() == playerType) {
                    Player[0].lowerNumPiecesOnBoard();
                    if(Player[0].getNumPiecesLeftToPlace()>0)
                        Player[0].setPhase(1);
                }
                else {
                    Player[1].lowerNumPiecesOnBoard();
                    if(Player[1].getNumPiecesLeftToPlace()>0)
                        Player[1].setPhase(1);
                }
            }
        }
        if (move.removePiece != -1) {
            if (Player[0].getPlayerType() == playerType) {
                getPosition(move.removePiece).setAsOccupied(playerType.COMPUTER);
                Player[1].undoRemovePiece();
                if(Player[1].getNumPiecesOnBoard()>3 && Player[1].getPhase()==3)
                    Player[1].setPhase(2);
            } else {
                getPosition(move.removePiece).setAsOccupied(playerType.PLAYER);
                Player[0].undoRemovePiece();
                if(Player[0].getNumPiecesOnBoard()>3 && Player[0].getPhase()==3)
                    Player[0].setPhase(2);
            }
        }

    }

    /**
     * funkcja ktora sprawdza czy ostatni ruch danego gracza stworzyl mlyn
     * jeslu tak to dodaje wszystkie mozliwe ruchy usuwania
     * @param player
     * @param validMoves
     * @param move
     */
    public void checkMove(playerType player, ArrayList<Move> validMoves, Move move) {

        boolean madeMill = false;
        for (int i = 0; i < 16; i++) {
            int playerPieces = 0;
            boolean selectedPiece = false;
            Position[] row = getMillCombination(i);
            for (int j = 0; j < 3; j++) {
                if (row[j].getPlayerOccupyingIt() == player) {
                    playerPieces++;
                }
                if (row[j].getPositionIndex() == move.dst) {
                    selectedPiece = true;
                }
            }
            if (playerPieces == 3 && selectedPiece) {
                madeMill = true;
                for(int k=0;k<24;k++){
                    if(getPosition(k).getPlayerOccupyingIt()!=player && getPosition(k).getPlayerOccupyingIt()!=playerType.NOONE){
                        move.removePiece=k;
                        validMoves.add(move);
                    }
                }
            }
        }
        if(!madeMill)
            validMoves.add(move);
    }

    /**
     * funkcja ktora sprawdza czy zadany ruch tworzy mlyn
     * @param player
     * @param move
     * @return madeMill
     */
    public boolean madeMill(playerType player, Move move) {

        boolean madeMill = false;
        for (int i = 0; i < 16; i++) {
            int playerPieces = 0;
            boolean selectedPiece = false;
            Position[] row = getMillCombination(i);
            for (int j = 0; j < 3; j++) {
                if (row[j].getPlayerOccupyingIt() == player) {
                    playerPieces++;
                }
                if (row[j].getPositionIndex() == move.dst) {
                    selectedPiece = true;
                }
            }
            if (playerPieces == 3 && selectedPiece) {
                madeMill = true;
            }
        }
        return madeMill;
    }

    /**
     * funkcja ktora zmniejsza ilosc kostek zadanego gracza
     * @param player
     */
    public void decNumPiecesOfPlayer(playerType player) {
        if (player == Player[0].getPlayerType())
            Player[0].removePiece();
        else
            Player[1].removePiece();
    }

    /**
     * funkcja usuwania kostki zadanego gracza z zadanej pozycji
     * @param boardIndex
     * @param playerType
     * @return
     */
    public boolean removePiece(int boardIndex, playerType playerType){
        if(!positionIsAvailable(boardIndex) && playerType==getPosition(boardIndex).getPlayerOccupyingIt()) {
            getPosition(boardIndex).setAsUnoccupied();
            decNumPiecesOfPlayer(playerType);
            int Phase;
            if(playerType== model.playerType.PLAYER) {
                Phase = Player[0].getPhase();
                if(Phase==2 && Player[0].getNumPiecesOnBoard()==3)
                    Player[0].setPhase(3);
            }
            else {
                Phase = Player[1].getPhase();
                if (Phase == 2 && Player[1].getNumPiecesOnBoard() == 3)
                    Player[1].setPhase(3);
            }
            return true;
        }
        return false;
    }

    /**
     * funkcja ktora sprawdza czy zadany ruch jest poprawny
     * @param currentPositionIndex
     * @param nextPositionIndex
     * @return
     */
    public boolean validMove(int currentPositionIndex, int nextPositionIndex){
        Position currentPos = getPosition(currentPositionIndex);
        if(currentPos.isAdjacentToThis(nextPositionIndex) && !getPosition(nextPositionIndex).isOccupied()) {
            return true;
        }
        return false;
    }

    /**
     * funkcja ktora wykonuje ruch zadanego gracza w fazie 1 i 2
     * @param srcIndex
     * @param destIndex
     * @param player
     * @return
     */
    public boolean movePieceFromTo(int srcIndex, int destIndex, playerType player){
        if(getPosition(srcIndex).getPlayerOccupyingIt()==player) {
            if(positionIsAvailable(destIndex)) {
                int numberOfPieces;
                if(Player[0].getPlayerType()==player)
                    numberOfPieces=Player[0].getNumPiecesOnBoard();
                else
                    numberOfPieces=Player[1].getNumPiecesOnBoard();
                if(validMove(srcIndex, destIndex) || numberOfPieces == 3) {
                    getPosition(srcIndex).setAsUnoccupied();
                    getPosition(destIndex).setAsOccupied(player);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * funkcja ktora ustawia kostki gracza na fazie 1
     * @param srcIndex
     * @param player
     * @return
     */
    public boolean placePieceOfPlayer(int srcIndex, playerType player){
        if(positionIsAvailable(srcIndex)) {
            getPosition(srcIndex).setAsOccupied(player);
            if(Player[0].getPlayerType()==player) {
                Player[0].raiseNumPiecesOnBoard();
                if(Player[0].getNumPiecesLeftToPlace()==0)
                    Player[0].setPhase(2);
            }
            else {
                Player[1].raiseNumPiecesOnBoard();
                if(Player[1].getNumPiecesLeftToPlace()==0)
                    Player[1].setPhase(2);
            }
            return true;
        }
        return false;
    }

    /**
     * funkcja ktora sprawdza czy dana pozycja jest zablokowana
     * @param position
     * @return
     */
    public boolean isBlocked(int position){
        int check=0;

        for(int i=0;i<boardPositions[position].getAdjacentPositionsIndexes().length;i++){
            if(boardPositions[boardPositions[position].getAdjacentPositionsIndexes()[i]].isOccupied())
                check++;
        }
        if(check==boardPositions[position].getAdjacentPositionsIndexes().length)
            return true;
        else
            return false;
    }

    /**
     * funkcja ktora sprawdza koniec gry
     * @return 0 jesli nie koniec, -1 jesli wygrywa gracz, 1 jesli wygrywa oponent
     */
    public int isGameOver(){
        if(Player[0].getNumPiecesLeftToPlace()!=0 || Player[1].getNumPiecesLeftToPlace()!=0)
            return 0;
        if(Player[0].getNumPiecesOnBoard()==2 && Player[0].getNumPiecesLeftToPlace()==0)
            return 1;
        else if(Player[1].getNumPiecesOnBoard()==2 && Player[1].getNumPiecesLeftToPlace()==0)
            return -1;
        else{
            boolean playerHasValidMove = false;
            boolean computerHasValidMove = false;
            playerType player;

            for(int i=0;i<24;i++){
                Position position = boardPositions[i];
                player=position.getPlayerOccupyingIt();
                if(player!=playerType.NOONE){
                    int[] adjacent = position.getAdjacentPositionsIndexes();
                    for(int j=0;j<adjacent.length;j++){
                        Position adjacentPosition = boardPositions[adjacent[j]];
                        if(!adjacentPosition.isOccupied()){
                            if(!playerHasValidMove)
                                playerHasValidMove=(player==playerType.PLAYER);
                            if(!computerHasValidMove)
                                computerHasValidMove=(player==playerType.COMPUTER);
                            break;
                        }
                    }
                }
                if(playerHasValidMove && computerHasValidMove)
                    return 0;
            }
            if(!playerHasValidMove && Player[0].getPhase()!=3)
                return 1;
            if(!computerHasValidMove && Player[1].getPhase()!=3)
                return -1;
            else
                return 0;
        }
    }

    /**
     * funkcja inicjulizujaca mape
     */
    private void initBoard(){

        for(int i = 0; i < 24; i++) {
            boardPositions[i] = new Position(i);
        }

        // outer square
        boardPositions[0].addAdjacentPositionsIndexes(1,9);
        boardPositions[1].addAdjacentPositionsIndexes(0,2,4);
        boardPositions[2].addAdjacentPositionsIndexes(1,14);
        boardPositions[9].addAdjacentPositionsIndexes(0,10,21);
        boardPositions[14].addAdjacentPositionsIndexes(2,13,23);
        boardPositions[21].addAdjacentPositionsIndexes(9,22);
        boardPositions[22].addAdjacentPositionsIndexes(19,21,23);
        boardPositions[23].addAdjacentPositionsIndexes(14,22);
        // middle square
        boardPositions[3].addAdjacentPositionsIndexes(4,10);
        boardPositions[4].addAdjacentPositionsIndexes(1,3,5,7);
        boardPositions[5].addAdjacentPositionsIndexes(4,13);
        boardPositions[10].addAdjacentPositionsIndexes(3,9,11,18);
        boardPositions[13].addAdjacentPositionsIndexes(5,12,14,20);
        boardPositions[18].addAdjacentPositionsIndexes(10,19);
        boardPositions[19].addAdjacentPositionsIndexes(16,18,20,22);
        boardPositions[20].addAdjacentPositionsIndexes(13,19);
        // inner square
        boardPositions[6].addAdjacentPositionsIndexes(7,11);
        boardPositions[7].addAdjacentPositionsIndexes(4,6,8);
        boardPositions[8].addAdjacentPositionsIndexes(7,12);
        boardPositions[11].addAdjacentPositionsIndexes(6,10,15);
        boardPositions[12].addAdjacentPositionsIndexes(8,13,17);
        boardPositions[15].addAdjacentPositionsIndexes(11,16);
        boardPositions[16].addAdjacentPositionsIndexes(15,17,19);
        boardPositions[17].addAdjacentPositionsIndexes(12,16);

    }

    /**
     * funkcja inicjulizujaca kombinacje mlynow
     */
    private void initMillCombinations() {
        millCombinations = new Position[16][3];

        //outer square
        millCombinations[0][0] = boardPositions[0];
        millCombinations[0][1] = boardPositions[1];
        millCombinations[0][2] = boardPositions[2];
        millCombinations[1][0] = boardPositions[0];
        millCombinations[1][1] = boardPositions[9];
        millCombinations[1][2] = boardPositions[21];
        millCombinations[2][0] = boardPositions[2];
        millCombinations[2][1] = boardPositions[14];
        millCombinations[2][2] = boardPositions[23];
        millCombinations[3][0] = boardPositions[21];
        millCombinations[3][1] = boardPositions[22];
        millCombinations[3][2] = boardPositions[23];
        //middle square
        millCombinations[4][0] = boardPositions[3];
        millCombinations[4][1] = boardPositions[4];
        millCombinations[4][2] = boardPositions[5];
        millCombinations[5][0] = boardPositions[3];
        millCombinations[5][1] = boardPositions[10];
        millCombinations[5][2] = boardPositions[18];
        millCombinations[6][0] = boardPositions[5];
        millCombinations[6][1] = boardPositions[13];
        millCombinations[6][2] = boardPositions[20];
        millCombinations[7][0] = boardPositions[18];
        millCombinations[7][1] = boardPositions[19];
        millCombinations[7][2] = boardPositions[20];
        //inner square
        millCombinations[8][0] = boardPositions[6];
        millCombinations[8][1] = boardPositions[7];
        millCombinations[8][2] = boardPositions[8];
        millCombinations[9][0] = boardPositions[6];
        millCombinations[9][1] = boardPositions[11];
        millCombinations[9][2] = boardPositions[15];
        millCombinations[10][0] = boardPositions[8];
        millCombinations[10][1] = boardPositions[12];
        millCombinations[10][2] = boardPositions[17];
        millCombinations[11][0] = boardPositions[15];
        millCombinations[11][1] = boardPositions[16];
        millCombinations[11][2] = boardPositions[17];
        //others
        millCombinations[12][0] = boardPositions[1];
        millCombinations[12][1] = boardPositions[4];
        millCombinations[12][2] = boardPositions[7];
        millCombinations[13][0] = boardPositions[9];
        millCombinations[13][1] = boardPositions[10];
        millCombinations[13][2] = boardPositions[11];
        millCombinations[14][0] = boardPositions[12];
        millCombinations[14][1] = boardPositions[13];
        millCombinations[14][2] = boardPositions[14];
        millCombinations[15][0] = boardPositions[16];
        millCombinations[15][1] = boardPositions[19];
        millCombinations[15][2] = boardPositions[22];
    }

    /**
     * funkcja ewplujaca
     * @param move
     * @param playerType
     * @return
     */
    public int score(Move move, playerType playerType){

        int score=0;
        int Phase;
        if(playerType== model.playerType.PLAYER)
            Phase = Player[0].getPhase();
        else
            Phase = Player[1].getPhase();
        if(Phase==1)
            score=18*closedMorris(move, playerType) + 26*numberOfMorrises() + numberOfBlockedOpponentPieces()
                    + 9*numberOfPieces() + 10*numberOfTwoPieceConfigurations() + 7*numberOfThreePieceConfigurations();
        else if(Phase==2)
            score=14*closedMorris(move, playerType) + 43*numberOfMorrises() + 10*numberOfBlockedOpponentPieces()
                    + 11*numberOfPieces() + 8*doubleMorris() + 1086*winningConfiguration();
        else if(Phase==3)
            score=16*closedMorris(move, playerType) + 10*numberOfTwoPieceConfigurations() + numberOfThreePieceConfigurations()
                    + 1190*winningConfiguration();

        return score;
    }

    /**
     * funkcja ktora sprawdza czy w danym ruchu gracz stworzyl mlyn
     * @param move
     * @param playerType
     * @return
     */
    public int closedMorris(Move move, playerType playerType){

        if(madeMill(playerType,move)){
            if(playerType==playerType.PLAYER)
                return -1;
            else
                return 1;
        }
        return 0;

    }

    /**
     * funkcja ktora liczy roznice miedzy liczba mlynow graczy
     * @return
     */
    public int numberOfMorrises(){
        int countPlayer=0;
        int countComputer=0;
        for (int i = 0; i < 16; i++) {
            int playerPieces = 0;
            int computerPieces = 0;
            Position[] row = getMillCombination(i);
            for (int j = 0; j < 3; j++) {
                if (row[j].getPlayerOccupyingIt() == playerType.PLAYER) {
                    playerPieces++;
                }
                if (row[j].getPlayerOccupyingIt() == playerType.COMPUTER) {
                    computerPieces++;
                }
            }
            if (playerPieces == 3)
                countPlayer++;
            if (computerPieces == 3)
                countComputer++;
        }
        return -(countPlayer-countComputer);
    }

    /**
     * funkcja ktora liczy roznice miedzy zablokowanymi kostkami graczy
     * @return
     */
    public int numberOfBlockedOpponentPieces(){
        int countPlayer=0;
        int countComputer=0;
        for(int i=0;i<24;i++){
            if(boardPositions[i].getPlayerOccupyingIt()==playerType.PLAYER && isBlocked(i))
                countPlayer++;
            if(boardPositions[i].getPlayerOccupyingIt()==playerType.COMPUTER && isBlocked(i))
                countComputer++;
        }
        return -(countComputer-countPlayer);
    }

    /**
     * funkcja ktora liczy roznice kostek graczy
     * @return
     */
    public int numberOfPieces(){
        return -(Player[0].getNumPiecesOnBoard()-Player[1].getNumPiecesOnBoard());
    }

    /**
     * funkcja ktora liczy kombinacje skladajaca sie z dwuch kostek dla dwoch graczy
     * @return
     */
    public int numberOfTwoPieceConfigurations() {
        int countPlayer = 0;
        int countComputer = 0;
        for (int i = 0; i < 16; i++) {
            int playerPieces = 0;
            int computerPieces = 0;
            int noonePieces = 0;
            Position[] row = getMillCombination(i);
            for (int j = 0; j < 3; j++) {
                if (row[j].getPlayerOccupyingIt() == playerType.PLAYER)
                    playerPieces++;
                else if (row[j].getPlayerOccupyingIt() == playerType.COMPUTER)
                    computerPieces++;
                else
                    noonePieces++;
                if (playerPieces == 2 && noonePieces == 1)
                    countPlayer++;
                else if (computerPieces == 2 && noonePieces == 1)
                    countComputer++;
            }
        }
        return -(countPlayer - countComputer);
    }

    /**
     * funkcja ktora liczy kombinacje skladajaca sie z trzech kostek dla dwoch graczy
     * @return
     */
    public int numberOfThreePieceConfigurations(){
        int countPlayer = 0;
        int countComputer = 0;
        ArrayList<ArrayList<Integer>> computerThree = new ArrayList<>();
        ArrayList<ArrayList<Integer>> playerThree = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            int playerPieces = 0;
            int computerPieces = 0;
            int noonePieces = 0;
            Position[] row = getMillCombination(i);
            for (int j = 0; j < 3; j++) {
                ArrayList<Integer> playerTwo = new ArrayList<>();
                ArrayList<Integer> computerTwo = new ArrayList<>();
                if (row[j].getPlayerOccupyingIt() == playerType.PLAYER) {
                    playerPieces++;
                    playerTwo.add(row[j].getPositionIndex());
                }
                else if (row[j].getPlayerOccupyingIt() == playerType.COMPUTER) {
                    computerPieces++;
                    computerTwo.add(row[j].getPositionIndex());
                }
                else
                    noonePieces++;
                if (playerPieces == 2 && noonePieces == 1)
                    playerThree.add(playerTwo);
                else if (computerPieces == 2 && noonePieces == 1)
                    computerThree.add(computerTwo);
            }
        }
        if(playerThree.size()>1){
            for(int i=0;i<playerThree.size()-1;i++){
                for(int j=i+1;j<playerThree.size();j++){
                    for(int k=0;k<2;k++){
                        if(check(playerThree.get(i),playerThree.get(j)))
                            countPlayer++;
                    }
                }
            }
        }
        if(computerThree.size()>1){
            for(int i=0;i<computerThree.size()-1;i++){
                for(int j=i+1;j<computerThree.size();j++){
                    for(int k=0;k<2;k++){
                        if(check(computerThree.get(i), computerThree.get(j)))
                            countComputer++;
                    }
                }
            }
        }
        return -(countPlayer-countComputer);
    }

    /**
     * funkcja sprawdzajaca czy listy maja wspolny punkt
     * @param first
     * @param second
     * @return
     */
    public boolean check(ArrayList<Integer> first, ArrayList<Integer> second){

        for(int i=0;i<first.size();i++) {
            for (int j = 0; j < second.size(); j++) {
                if(first.get(i)==second.get(j))
                    return true;
            }
        }
        return false;
    }

    /**
     * funkcja ktora liczy ilosc dwoch mlynow graczy
     * @return
     */
    public int doubleMorris(){
        int countPlayer = 0;
        int countComputer = 0;
        ArrayList <Position[]> playerMorris = new ArrayList<>();
        ArrayList <Position[]> computerMorris = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            int playerPieces = 0;
            int computerPieces = 0;
            Position[] row = getMillCombination(i);
            for (int j = 0; j < 3; j++) {
                if (row[j].getPlayerOccupyingIt() == playerType.PLAYER) {
                    playerPieces++;
                }
                if (row[j].getPlayerOccupyingIt() == playerType.COMPUTER) {
                    computerPieces++;
                }
            }
            if (playerPieces == 3)
                playerMorris.add(getMillCombination(i));
            if (computerPieces == 3)
                computerMorris.add(getMillCombination(i));
        }
        if(playerMorris.size()>1){
            for(int i=0;i<playerMorris.size()-1;i++){
                for(int j=i+1;j<playerMorris.size();j++){
                    for(int k=0;k<3;k++){
                        for(int l=0;l<3;l++) {
                            if (playerMorris.get(i)[k].getPositionIndex() == playerMorris.get(j)[l].getPositionIndex())
                                countPlayer++;
                        }
                    }
                }
            }
        }
        if(computerMorris.size()>1){
            for(int i=0;i<computerMorris.size()-1;i++){
                for(int j=i+1;j<computerMorris.size();j++){
                    for(int k=0;k<3;k++){
                        for(int l=0;l<3;l++) {
                            if (computerMorris.get(i)[k].getPositionIndex() == computerMorris.get(j)[l].getPositionIndex())
                                countComputer++;
                        }
                    }
                }
            }
        }
        return -(countPlayer-countComputer);
    }

    /**
     * funckja sprawdzajaca koniec gry
     * @return
     */
    public int winningConfiguration(){
        return isGameOver();
    }


}
