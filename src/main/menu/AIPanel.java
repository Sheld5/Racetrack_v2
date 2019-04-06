package main.menu;

import javax.swing.*;

import java.awt.*;
import java.io.File;

public class AIPanel extends JPanel {
    private static int MAX_NAME_LENGTH = 16;

    private int id;
    private File aiFile;
    private String name;
    private JLabel aiName;
    private JButton remove;

    public AIPanel(int id, File aiFile, Menu menu) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = 12;
        this.setMinimumSize(new Dimension(256, 42));
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, 42));
        this.setMaximumSize(new Dimension(1024, 42));
        this.setBorder(BorderFactory.createLineBorder(Color.gray));

        this.id = id;
        this.aiFile = aiFile;
        name = aiFile.getName();
        name = name.substring(0, name.length() - 5);

        if (name.length() <= MAX_NAME_LENGTH) {
            aiName = new JLabel(name);
        } else {
            String nameShort = name.substring(0, MAX_NAME_LENGTH - 1);
            aiName = new JLabel(nameShort);
        }
        c.gridx = 0;
        add(aiName, c);

        remove = new JButton("remove");
        remove.addActionListener(e -> menu.removeAI(id));
        c.gridx = 1;
        add(remove, c);

    }

    public int getID() {
        return id;
    }

    public String getAIName() {
        return name;
    }

    public File getFile() {
        return aiFile;
    }

}
