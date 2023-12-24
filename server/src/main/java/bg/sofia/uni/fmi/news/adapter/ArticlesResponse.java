package bg.sofia.uni.fmi.news.adapter;

import bg.sofia.uni.fmi.news.article.Article;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticlesResponse {
    private String status;
    @SerializedName("code")
    private String errorCode;
    private int totalResults;
    private String message;
    private List<Article> articles;

    public ArticlesResponse(String status, String errorCode, int totalResults, List<Article> articles) {
        this.status = status;
        this.errorCode = errorCode;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<Article> getArticles() {
        return articles;
    }
    public boolean hasError() {
        return Status.fromString(status).equals(Status.ERROR);
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %d %s", status, errorCode, message, totalResults,
                String.join(",", articles.toString()));
    }

}
