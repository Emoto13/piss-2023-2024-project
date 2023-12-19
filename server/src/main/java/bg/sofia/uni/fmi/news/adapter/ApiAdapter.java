package bg.sofia.uni.fmi.news.adapter;

import bg.sofia.uni.fmi.news.article.Article;
import bg.sofia.uni.fmi.news.exception.ApiError;
import bg.sofia.uni.fmi.news.exception.FailedToRetrieveArticlesException;

import java.util.List;

public interface ApiAdapter {
    List<Article> getArticles(GetArticlesParameters parameters) throws ApiError, FailedToRetrieveArticlesException;
}
