package org.piss2023;

import org.article.Article;

import javax.swing.*;
import java.awt.*;

// Custom renderer for the articles
public class ArticleRenderer extends JLabel implements ListCellRenderer<Article> {
    public boolean fasterLoad = false;
    @Override
    public Component getListCellRendererComponent(JList<? extends Article> list, Article article, int index, boolean isSelected, boolean cellHasFocus) {
        String title = article.getTitle();
        ImageIcon icon = article.getIcon();

        if (!fasterLoad) {
            setIcon(icon);
        }
        setText(title);
        return this;
    }
}
