package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class mainFrame extends JFrame {

    private mainMenuPanel menuPanel = new mainMenuPanel(this);
    private gameMenuPanel gamePanel;
    private optionsMenuPanel optionsPanel;


    public mainFrame(){
        createFrame();
        this.setVisible(true);
    }

    /**
     * f-kcja tworzaca okno ramki
     */
    private void createFrame(){
        this.add(menuPanel);
        this.setTitle("PSZT GAME");
        this.setSize(850 ,950);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
    }

    /**
     * funkcja ladujaca obrazek
     * @param pathname nazwa obrazka
     */
    public Image loadImage(String pathname) {
        Image tempBackground = null;
        try {
             tempBackground = ImageIO.read(new File(pathname));
        } catch (IOException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  tempBackground;
    }

    /**
     * odswiezanie przyciskow po ponownym ladowaniu paneli
     * @param but przycisk
     */
    public void refresh(JButton but){
        but.validate();
        but.revalidate();
        but.repaint();
    }


    public void addStartButtonActionListener(ActionListener startGame){
        menuPanel.getStartButton().addActionListener(startGame);
    }

    public void addExitButtonActionListener(ActionListener exitGame){
        menuPanel.getExitButton().addActionListener(exitGame);
    }

    public void addOptionsButtonActionListener(ActionListener options){
        menuPanel.getOptionsButton().addActionListener(options);
    }


    public gameMenuPanel getGamePanel() {
        return this.gamePanel;
    }

    public void setGamePanel( gameMenuPanel newGamePanel){
        this.remove(menuPanel);
        this.gamePanel = newGamePanel;
        this.add(gamePanel);
        this.validate();
        this.repaint();
        this.getContentPane().repaint();

    }

    public void setGamePanelFromGamePanel(gameMenuPanel newGamePanel) {
        this.remove(gamePanel);
        this.gamePanel = newGamePanel;
        this.add(gamePanel);
        this.validate();
        this.repaint();
        this.getContentPane().repaint();
    }

    public void setOptionsPanel( optionsMenuPanel newOptionsPanel){
        this.remove(menuPanel);
        this.optionsPanel = newOptionsPanel;
        this.add(this.optionsPanel);
        this.validate();
        this.repaint();
        this.getContentPane().repaint();

    }

    public void setMenuPanelFromOptions( mainMenuPanel newMainMenuPanel){
        this.remove(optionsPanel);
        this.menuPanel = newMainMenuPanel;
        this.add(menuPanel);
        this.validate();
        this.repaint();
        this.getContentPane().repaint();

    }

    public void setMenuPanelFromGame( mainMenuPanel newMainMenuPanel){
        this.remove(gamePanel);
        this.menuPanel = newMainMenuPanel;
        this.add(this.menuPanel);
        this.validate();
        this.repaint();
        this.getContentPane().repaint();

    }


}
