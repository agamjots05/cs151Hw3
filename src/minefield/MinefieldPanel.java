package minefield;

import javax.swing.*;

import java.awt.*;
import mvc.AppFactory;
import mvc.AppPanel;

public class MinefieldPanel extends AppPanel
{
    private JButton[] buttons;

    public MinefieldPanel(AppFactory factory)
    {
        super(factory);

        // Grid Layout
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.insets = new Insets(20, 30, 10, 30);
        
        String[] commandsName = factory.getEditCommands();
        buttons = new JButton[commandsName.length];
        int row = 1; int col = 1;
        for (int i = 0; i < commandsName.length; i++) {
            buttons[i] = new JButton(commandsName[i]);
            buttons[i].addActionListener(this);

            // Button grid constraint
            gridConstraints.gridx = col;
            gridConstraints.gridy = row;
            col++;
            if (col > 2) { col = 1; row++; }

            controlPanel.add(buttons[i], gridConstraints);
        }
    }

    public static void main(String[] args) {
        AppFactory factory = new MinefieldFactory();
        AppPanel panel = new MinefieldPanel(factory);
        panel.display();
    }
}
