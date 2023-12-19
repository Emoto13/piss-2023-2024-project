package bg.sofia.uni.fmi.news.article;

public class Article {
    private final Source source;
    private final String author;
    private final String title;
    private final String description;
    private final String url;
    private final String urlToImage;
    private final String content;

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

    @Override
    public String toString() {
        return String.format(
                "Article: %s, Author: %s, Title: %s, Description: %s, Url: %s, UrlToImage: %s, Content: %s",
                source, author, title, description, url, urlToImage, content);
    }
}

