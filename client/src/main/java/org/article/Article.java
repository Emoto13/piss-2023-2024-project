package org.article;

import javax.swing.*;

public class Article {
    private final Source source;
    private final String author;
    private final String title;
    private final String description;
    private final String url;
    private final String urlToImage;
    private transient ImageIcon icon;
    private final String content;
    public String getTitle() {
        return this.title;
    }
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    public ImageIcon getIcon() {
        return this.icon;
    }
    public String getUrlToImage() {
        return this.urlToImage;
    }
    public Article(Source source, String author, String title, String description, String url, String urlToImage,
                   String content) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.content = content;
    }

//    @Override
//    public String toString() {
//        return String.format(
//                "Article: %s, Author: %s, Title: %s, Description: %s, Url: %s, UrlToImage: %s, Content: %s",
//                source, author, title, description, url, urlToImage, content);
//    }

    @Override
    public String toString() {
        return String.format(
                "%s, Description: %s," + "\n" + "Url: %s, UrlToImage: %s, Content: %s",
                title, description, url, urlToImage, content);
    }
}

