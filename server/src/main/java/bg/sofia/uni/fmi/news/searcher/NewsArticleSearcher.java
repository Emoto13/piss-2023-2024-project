package bg.sofia.uni.fmi.news.searcher;

import bg.sofia.uni.fmi.news.adapter.ApiAdapter;
import bg.sofia.uni.fmi.news.adapter.GetArticlesParameters;
import bg.sofia.uni.fmi.news.adapter.NewsApiAdapter;
import bg.sofia.uni.fmi.news.article.Article;
import bg.sofia.uni.fmi.news.exception.ApiError;
import bg.sofia.uni.fmi.news.exception.FailedToRetrieveArticlesException;
import bg.sofia.uni.fmi.news.url.NewsUrlBuilder;
import bg.sofia.uni.fmi.news.url.PaginatedUrlBuilder;

import java.net.http.HttpClient;
import java.util.Collection;
import java.util.List;

public class NewsArticleSearcher implements ArticleSearcher {
    private ApiAdapter apiAdapter;

    public NewsArticleSearcher(String apiKey) {
        this.createApiAdapter(apiKey);
    }

    private void createApiAdapter(String apiKey) {
        PaginatedUrlBuilder urlBuilder = new NewsUrlBuilder(apiKey);
        this.apiAdapter = new NewsApiAdapter(urlBuilder, HttpClient.newHttpClient());
    }

    public List<Article> searchArticlesBy(Collection<String> keywords, String country, String category, Integer page) {
        if (keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords should not be empty");
        }
        GetArticlesParameters parameters = new GetArticlesParameters(keywords, country, category, page);
        try {
            return apiAdapter.getArticles(parameters);
        } catch (FailedToRetrieveArticlesException | ApiError error) {
            System.err.println(error.getMessage());
        }
        return null;
    }
}
