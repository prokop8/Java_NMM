package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class mainMenuPanel extends JPanel {

    private JButton startButton = new JButton("START");
    private JButton exitButton = new JButton("EXIT");
    private JButton optionsButton = new JButton("OPTIONS");
    private mainFrame frame;
    private Image background;


    /**
     * konstruktor twarzacy panel na samym poczatku gry.Przy pierwszym ladowaniu ramki
     * @param frame ramka,do ktorej bedzie przywiazany panel
     */
    public mainMenuPanel(mainFrame frame) {
        this.frame = frame;
        this.setLayout(null);
        this.background = frame.loadImage("mainMenu.jpg");

        //start
        startButton.setBounds(25, 830, 250, 60);
        startButton.setOpaque(false);
        this.add(startButton);

        //exit
        optionsButton.setBounds(300, 830, 250, 60);
        optionsButton.setOpaque(false);
        this.add(optionsButton);

        //textfield
        exitButton.setBounds(575, 830, 250, 60);
        exitButton.setOpaque(false);
        this.add(exitButton);
    }

    /**
     * tworzenie paneli main menu przy zwracaniu z menu game
     * @param frame ramka,do ktorej bedzie przywiazany panel
     * @param listenerTable tablica listenerow dla przyciskow
     */
    public mainMenuPanel(mainFrame frame, ActionListener[] listenerTable){
        this.frame = frame;
        this.setLayout(null);
        this.background = frame.loadImage("mainMenu.jpg");

        //start
        startButton.setBounds(25, 830, 250, 60);
        startButton.setOpaque(false);
        this.add(startButton);

        //buttons
        optionsButton.setBounds(300, 830, 250, 60);
        optionsButton.setOpaque(false);
        this.add(optionsButton);

        //exit
        exitButton.setBounds(575, 830, 250, 60);
        exitButton.setOpaque(false);
        this.add(exitButton);

        this.addActionListenerStart(listenerTable[0]);
        this.addActionListenerOptions(listenerTable[1]);
        this.addActionListenerExit(listenerTable[2]);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(this.background, 0, 0, null);
        this.requestFocusInWindow();
    }

    private void addActionListenerStart(ActionListener start){
        startButton.addActionListener(start);
    }

    private void addActionListenerExit(ActionListener exit){
        exitButton.addActionListener(exit);
    }

    private void addActionListenerOptions(ActionListener options){
        optionsButton.addActionListener(options);
    }

    public JButton getStartButton(){
        return this.startButton;
    }

    public JButton getExitButton(){
        return this.exitButton;
    }

    public JButton getOptionsButton(){
        return this.optionsButton;
    }
}
