package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class optionsMenuPanel extends JPanel {

    private JButton easyComplexityLevelButton = new JButton("EASY");
    private JButton mediumComplexityLevelButton = new JButton("MEDIUM");
    private JButton hardComplexityLevelButton = new JButton("HARD");
    private mainFrame frame;
    private Image background;
    
    public optionsMenuPanel(mainFrame frame, ActionListener[] listenersTable) {
        this.frame = frame;
        this.setLayout(null);
        this.background = frame.loadImage("optionsMenu.jpg");

        //latwy poziom przycisk
        easyComplexityLevelButton.setBounds(25, 830, 250, 60);
        easyComplexityLevelButton.setOpaque(false);
        this.add(easyComplexityLevelButton);

        //sredni poziom przycisk
        mediumComplexityLevelButton.setBounds(300, 830, 250, 60);
        mediumComplexityLevelButton.setOpaque(false);
        this.add(mediumComplexityLevelButton);

        //trudny poziom przycisk
        hardComplexityLevelButton.setBounds(575, 830, 250, 60);
        hardComplexityLevelButton.setOpaque(false);
        this.add(hardComplexityLevelButton);

        this.addActionListenerEasy(listenersTable[0]);
        this.addActionListenerMedium(listenersTable[1]);
        this.addActionListenerHard(listenersTable[2]);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(this.background, 0, 0, null);
        this.requestFocusInWindow();
    }

    private void addActionListenerEasy(ActionListener easyLevel){
        easyComplexityLevelButton.addActionListener(easyLevel);
    }

    private void addActionListenerMedium(ActionListener mediumLevel){
        mediumComplexityLevelButton.addActionListener(mediumLevel);
    }

    private void addActionListenerHard(ActionListener hardLevel){
        hardComplexityLevelButton.addActionListener(hardLevel);
    }

}
