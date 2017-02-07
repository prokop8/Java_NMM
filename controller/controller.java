package controller;

import model.AlphaBetaPruningAI;
import model.Model;
import model.Move;
import model.playerType;
import view.gameMenuPanel;
import view.mainFrame;
import view.mainMenuPanel;
import view.optionsMenuPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class controller {

    private mainFrame frame;
    private Model model;
    private AlphaBetaPruningAI AI;

    private ActionListener[] mainMenuListeners;
    private ActionListener[] gameMenuListeners;
    private ActionListener[] optionsMenuListener;

    int src = -1;
    int dst = -1;


    public controller(mainFrame frame, Model model) {
        this.frame = frame;
        this.model = model;
        this.AI = new AlphaBetaPruningAI();

        //obrabianie tablicy z listenerami dla main menu
        this.mainMenuListeners = new ActionListener[3];
        mainMenuListeners[0] = new startGame();
        mainMenuListeners[1] = new options();
        mainMenuListeners[2] = new exitGame();

        //obrabianie tablicy z listenerami dla game menu
        this.gameMenuListeners = new ActionListener[2];
        gameMenuListeners[0] = new mainMenu();
        gameMenuListeners[1] = new tryAgain();

        //obrabianie tablicy z listenerami dla options menu
        this.optionsMenuListener = new ActionListener[3];
        optionsMenuListener[0] = new easy(2, this.model);
        optionsMenuListener [1] = new medium(4, this.model);
        optionsMenuListener[2] = new hard(6, this.model);



        this.frame.addStartButtonActionListener(new startGame());
        this.frame.addExitButtonActionListener(new exitGame());
        this.frame.addOptionsButtonActionListener(new options());

    }

    class clickEvent implements MouseListener{

        int src, dst;
        long counter = 0;
        int tmp;

        @Override
        public void mouseClicked(MouseEvent e) {


            if (model.deleting) {//sprawdzamy,czy mamy usuwac
                model.deleting = false;

                //pobranie JBUTTON do usuwanie
                JButton source = (JButton) e.getSource();
                int buttonIndex = frame.getGamePanel().getButtonIndex(source);
                model.removePiece(buttonIndex, playerType.COMPUTER);

                //sprawdzenie na WIN/LOSE
                tmp = model.isGameOver();
                if(tmp != 0){
                    if(tmp == 1)
                        JOptionPane.showMessageDialog(null,"YOU LOSE");
                    else
                        JOptionPane.showMessageDialog(null,"YOU WIN");
                }

                //usuwamy wybrany JBUTTON
                frame.getGamePanel().getButtonWithIndex(buttonIndex).setOpaque(false);
                frame.refresh(frame.getGamePanel().getButtonWithIndex(buttonIndex));

                //ruch komputera
                Move compMove = AI.playBestMove(model, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                model.makeMove(compMove, playerType.COMPUTER);

                //pobranie punktu docelowego
                int dstKomp = compMove.dst;

                //sprawdzenie czy komputer zrobil ruch w drugiej fazie
                if(compMove.src != -1){
                    frame.getGamePanel().getButtonWithIndex(compMove.src).setOpaque(false);
                    frame.refresh(frame.getGamePanel().getButtonWithIndex(compMove.src));
                }

                //kolorowanie punktu docelowego komputera
                frame.getGamePanel().getButtonWithIndex(dstKomp).setOpaque(true);
                frame.getGamePanel().getButtonWithIndex(dstKomp).setBackground(Color.BLACK);
                frame.refresh(frame.getGamePanel().getButtonWithIndex(dstKomp));

                //jesli komputer usunal jakos kostke gracza
                if (compMove.removePiece != -1) {
                    frame.getGamePanel().getButtonWithIndex(compMove.removePiece).setOpaque(false);
                    frame.refresh(frame.getGamePanel().getButtonWithIndex(compMove.removePiece));
                }

                //sprawdzienie na WIN/LOSE
                tmp = model.isGameOver();
                if(tmp != 0){
                    if(tmp == 1)
                        JOptionPane.showMessageDialog(null,"YOU LOSE");
                    else
                        JOptionPane.showMessageDialog(null,"YOU WIN");
                }

            }

            else {//nic nie kasujemy

                if (model.getPlayer(0).getPhase() == 1) { //pierwsza faza

                    //JBUTTON wybrany przez gracza
                    JButton source = (JButton) e.getSource();
                    int buttonIndex = frame.getGamePanel().getButtonIndex(source);

                    //chodzi gracz
                    if (model.placePieceOfPlayer(buttonIndex, playerType.PLAYER)) {
                        JButton workButton = frame.getGamePanel().getButtonWithIndex(buttonIndex);
                        workButton.setOpaque(true);
                        workButton.setBackground(Color.WHITE);
                        workButton.setBorderPainted(true);

                        Move move = new Move(-1, buttonIndex, -1);
                        //sprawzenie czy nie ma czegos na kasowanie
                        boolean check = model.madeMill(playerType.PLAYER, move);
                        if (!check) {

                            Move compMove = AI.playBestMove(model, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                            model.makeMove(compMove, playerType.COMPUTER);
                            int dstKomp = compMove.dst;
                            //ustawienie nowych kostek na plansze
                            frame.getGamePanel().getButtonWithIndex(dstKomp).setOpaque(true);
                            frame.getGamePanel().getButtonWithIndex(dstKomp).setBackground(Color.BLACK);

                            if (compMove.removePiece != -1) {
                                frame.getGamePanel().getButtonWithIndex(compMove.removePiece).setOpaque(false);
                                frame.refresh(frame.getGamePanel().getButtonWithIndex(compMove.removePiece));
                            }

                        }
                        else {
                            model.deleting = true;
                        }

                    }


                }
                else if (model.getPlayer(0).getPhase() == 2 || model.getPlayer(0).getPhase() == 3) {//faza 2 lub 3

                    //pierwszy click - zapisz jako src
                    if(model.moveFromTwo == false){

                        JButton source = (JButton) e.getSource();
                        src = frame.getGamePanel().getButtonIndex(source);
                        model.moveFromTwo = true;

                        if(model.getPosition(src).getPlayerOccupyingIt()!=playerType.PLAYER) {
                            src=-1;
                            model.moveFromTwo = false;
                        }
                    }

                    //drugi click - zapisz jako dst i przesun
                    else if(model.moveFromTwo == true){

                        JButton source = (JButton) e.getSource();
                        dst = frame.getGamePanel().getButtonIndex(source);
                        model.moveFromTwo = false;


                        if(model.movePieceFromTo(src, dst, playerType.PLAYER)) {

                            frame.getGamePanel().getButtonWithIndex(src).setOpaque(false);
                            frame.refresh(frame.getGamePanel().getButtonWithIndex(src));
                            frame.getGamePanel().getButtonWithIndex(dst).setOpaque(true);
                            frame.getGamePanel().getButtonWithIndex(dst).setBackground(Color.WHITE);
                            frame.refresh(frame.getGamePanel().getButtonWithIndex(dst));

                            //sprawdzenie na WIN/LOSE
                            tmp = model.isGameOver();
                            if(tmp != 0){
                                if(tmp == 1)
                                    JOptionPane.showMessageDialog(null,"YOU LOSE");
                                else
                                    JOptionPane.showMessageDialog(null,"YOU WIN");
                            }

                            //ruch komputera
                            Move move = new Move(src, dst, -1);
                            boolean check = model.madeMill(playerType.PLAYER, move);
                            if (!check) {//sprawdzenie czy nie trzeba kasowac

                                Move compMove = AI.playBestMove(model, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                                model.makeMove(compMove, playerType.COMPUTER);

                                src = compMove.src;
                                dst = compMove.dst;

                                frame.getGamePanel().getButtonWithIndex(src).setOpaque(false);
                                frame.refresh(frame.getGamePanel().getButtonWithIndex(src));
                                frame.getGamePanel().getButtonWithIndex(dst).setOpaque(true);
                                frame.getGamePanel().getButtonWithIndex(dst).setBackground(Color.BLACK);
                                frame.refresh(frame.getGamePanel().getButtonWithIndex(dst));


                                src = -1;
                                dst = -1;

                                if (compMove.removePiece != -1) {
                                    frame.getGamePanel().getButtonWithIndex(compMove.removePiece).setOpaque(false);
                                    frame.refresh(frame.getGamePanel().getButtonWithIndex(compMove.removePiece));
                                }

                                //ponowne spradzenie na WIN/LOSE
                                tmp = model.isGameOver();
                                if(tmp != 0){
                                    if(tmp == 1)
                                        JOptionPane.showMessageDialog(null,"YOU LOSE");
                                    else
                                        JOptionPane.showMessageDialog(null,"YOU WIN");
                                }

                            } else {
                                model.deleting = true;
                            }
                        }

                        else{
                            src = -1;
                            dst = -1;
                            model.moveFromTwo = false;
                        }
                    }
                }//koniec fazy 2/3
            }//koniec sprawdzania na kasowanie/ruch w fazie
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }
    }

    /**
     * tworzenie nowego panelu main menu
     */
    class mainMenu implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            frame.setMenuPanelFromGame(new mainMenuPanel(frame,mainMenuListeners));
        }
    }

    /**
     * tworzenie nowego panelu game menu do gry
     */
    class startGame implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            model = null;
            model = new Model();
            frame.setGamePanel(new gameMenuPanel(frame, gameMenuListeners, new clickEvent()));
        }
    }

    /**
     * odnowienie panelu game menu
     */
    class tryAgain implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            model = null;
            model = new Model();
            frame.setGamePanelFromGamePanel(new gameMenuPanel(frame, gameMenuListeners, new clickEvent()));
        }
    }

    /**
     * obrabianie wyjscia z gry
     */
    class exitGame implements ActionListener {
        public void actionPerformed(ActionEvent arg0){
            System.exit(0);
        }
    }

    /**
     * wejscie do menu options
     */
    class options implements  ActionListener{
        public void actionPerformed(ActionEvent arg0){
            frame.setOptionsPanel(new optionsMenuPanel(frame, optionsMenuListener));
        }
    }

    /**
     * obrabianie dodawania latwego poziomu gru
     */
    class easy implements ActionListener{

        int complexity;
        Model model;

        public easy(int complexity, Model model){
            this.complexity = complexity;
            this.model = model;
        }

        public void actionPerformed(ActionEvent arg0) {
            model.setComplexity(this.complexity);
            frame.setMenuPanelFromOptions(new mainMenuPanel(frame, mainMenuListeners));
        }
    }

    /**
     * obrabianie dodawania sredniego poziomu gry
     */
    class  medium implements ActionListener{

        int complexity;
        Model model;

        public medium(int complexity, Model model) {
            this.complexity = complexity;
            this.model = model;
        }

        public void actionPerformed(ActionEvent arg0) {
            this.model.setComplexity(this.complexity);
            frame.setMenuPanelFromOptions(new mainMenuPanel(frame, mainMenuListeners));
        }
    }

    /**
     * obrabania dodawania trudnego poziomu gry
     */
    class hard implements  ActionListener{

        int complexity;
        Model model;

        public hard(int complexity, Model model) {
            this.complexity = complexity;
            this.model = model;
        }

        public void actionPerformed(ActionEvent arg){
            this.model.setComplexity(this.complexity);
            frame.setMenuPanelFromOptions(new mainMenuPanel(frame, mainMenuListeners));
        }
    }

}
