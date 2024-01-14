package org.piss2023;

import org.article.Article;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageFetcher implements Runnable {
    private Article[] articles;

    public List<Article> getArticles() {
        return Arrays.stream(articles).toList();
    }
    public ImageFetcher(Article[] articles) {
        this.articles = articles;
    }
    @Override
    public void run() {
        Logger logger = Logger.getLogger(ImageFetcher.class.getName());
        for (var article : articles) {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(ArticlesFrame.class.getResource("no_preview.png")));
            // Each icon is the image located on the article urlToImage
            if (article.getUrlToImage() != null) {
                logger.log(Level.INFO, "Fetching image");
                try {
                    URL icon_url = new URL(article.getUrlToImage());
                    icon = new ImageIcon(icon_url);
                    Image image = icon.getImage();
                    image = image.getScaledInstance(80, 50, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(image);
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
            }
            article.setIcon(icon);
        }
    }
}
