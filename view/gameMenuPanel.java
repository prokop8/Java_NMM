package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;


public class gameMenuPanel extends JPanel
{
    private mainFrame frame;
    private JButton tryAgainButton = new JButton("TRY AGAIN");
    private JButton mainMenuButton = new JButton("MAIN MENU");
    private JButton[] playButtonsTable;
    private Image background;

    public gameMenuPanel(mainFrame frame, ActionListener[] listenersTable, MouseListener click){
        this.frame = frame;
        this.setLayout(null);
        this.background = frame.loadImage("gameMenu.jpg");

        //try again
        tryAgainButton.setBounds(25, 830, 250, 60);
        tryAgainButton.setOpaque(false);
        this.add(tryAgainButton);

        //main menu
        mainMenuButton.setBounds(575, 830, 250, 60);
        mainMenuButton.setOpaque(false);
        this.add(mainMenuButton);

        playButtonsTable = new JButton[24];
        this.iniciateMill();

        this.addActionListenerMainMenu(listenersTable[0]);
        this.addActionListenerTryAgain(listenersTable[1]);
        this.addMouseListenerGameButtons(click);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(this.background, 0, 0, null);
        this.requestFocusInWindow();
    }

    /**
     * ustawinie grajacych przyciskow dla gry(mlyn)
     */
    private void iniciateMill(){
        int counter = 0;

        //first row
        for(int  posX = 50; posX <= 750; posX+=350){
            playButtonsTable[counter] = new JButton();
            playButtonsTable[counter].setBounds(posX, 50, 50, 50);
            playButtonsTable[counter].setOpaque(false);
            this.add(playButtonsTable[counter]);
            counter++;
        }

        //second row
        for(int posX = 150; posX <= 650; posX+=250 ){
            playButtonsTable[counter] = new JButton();
            playButtonsTable[counter].setBounds(posX, 150, 50, 50);
            playButtonsTable[counter].setOpaque(false);
            this.add(playButtonsTable[counter]);
            counter++;
        }

        //third row
        for(int posX = 250; posX <= 550; posX+=150 ){
            playButtonsTable[counter] = new JButton();
            playButtonsTable[counter].setBounds(posX, 250, 50, 50);
            playButtonsTable[counter].setOpaque(false);
            this.add(playButtonsTable[counter]);
            counter++;
        }

        //forth row
        for(int posX1 = 50, posX2 = 550; posX1 <= 250; posX1+=100, posX2+=100 ){


            playButtonsTable[counter] = new JButton();
            playButtonsTable[counter].setBounds(posX1, 400, 50, 50);
            playButtonsTable[counter].setOpaque(false);
            this.add(playButtonsTable[counter]);
            counter+=3;



            playButtonsTable[counter] = new JButton();
            playButtonsTable[counter].setBounds(posX2, 400, 50, 50);
            playButtonsTable[counter].setOpaque(false);
            this.add(playButtonsTable[counter]);
            counter-=2;

        }

        counter+=3;

        //fifth row
        for(int posX = 250; posX <= 550; posX+=150 ){
            playButtonsTable[counter] = new JButton();
            playButtonsTable[counter].setBounds(posX, 550, 50, 50);
            playButtonsTable[counter].setOpaque(false);
            this.add(playButtonsTable[counter]);
            counter++;
        }

        //sixth row
        for(int posX = 150; posX <= 650; posX+=250 ){
            playButtonsTable[counter] = new JButton();
            playButtonsTable[counter].setBounds(posX, 650, 50, 50);
            playButtonsTable[counter].setOpaque(false);
            this.add(playButtonsTable[counter]);
            counter++;
        }

        //sevens row
        for(int  posX = 50; posX <= 750; posX+=350){
            playButtonsTable[counter] = new JButton();
            playButtonsTable[counter].setBounds(posX, 750, 50, 50);
            playButtonsTable[counter].setOpaque(false);
            this.add(playButtonsTable[counter]);
            counter++;
        }
    }

    public int getButtonIndex(JButton button) {

        for(int i = 0; i<=23; i++) {
         if (playButtonsTable[i] == button) return i;
        }
        return 0;
    }

    public JButton getButtonWithIndex(int index){
        return playButtonsTable[index];
    }

    private void addActionListenerMainMenu(ActionListener mainMenu){
        mainMenuButton.addActionListener(mainMenu);
    }

    private void addActionListenerTryAgain(ActionListener tryAgain) {
        tryAgainButton.addActionListener(tryAgain);
    }

    private void addMouseListenerGameButtons(MouseListener click) {
        for(int i = 0; i <= 23; i++) {
            playButtonsTable[i].addMouseListener(click);
        }
    }
}
