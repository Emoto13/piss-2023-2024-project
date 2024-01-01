package org.piss2023;

import javax.swing.JFrame;

public class ArticlesFrame extends JFrame {

    private int width;
    private int height;

    public ArticlesFrame(int w, int h) {
        width = w;
        height = h;
    }

    public void setUpGUI() {
        setSize(width, height);
        setLocationRelativeTo(null);
        setTitle("Articles");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}
