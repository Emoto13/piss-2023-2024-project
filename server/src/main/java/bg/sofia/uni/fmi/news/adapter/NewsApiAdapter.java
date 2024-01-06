package bg.sofia.uni.fmi.news.adapter;

import bg.sofia.uni.fmi.news.article.Article;
import bg.sofia.uni.fmi.news.exception.ApiError;
import bg.sofia.uni.fmi.news.exception.ApiErrorFactory;
import bg.sofia.uni.fmi.news.exception.FailedToRetrieveArticlesException;
import bg.sofia.uni.fmi.news.searcher.NewsArticleSearcher;
import bg.sofia.uni.fmi.news.url.PaginatedUrlBuilder;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewsApiAdapter implements ApiAdapter {
    private final HttpClient httpClient;
    private PaginatedUrlBuilder urlBuilder;

    public NewsApiAdapter(PaginatedUrlBuilder urlBuilder, HttpClient httpClient) {
        this.urlBuilder = urlBuilder;
        this.httpClient = httpClient;
    }

    private HttpRequest buildRequest(String url) {
        return HttpRequest.newBuilder(URI.create(url)).build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws FailedToRetrieveArticlesException {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new FailedToRetrieveArticlesException(e.getMessage());
        }
    }

    private ArticlesResponse processRawResponse(HttpResponse<String> response) {
        Gson gson = new Gson();
        return gson.fromJson(response.body(), ArticlesResponse.class);
    }

    public ArticlesResponse getArticlesResponse(String url) throws FailedToRetrieveArticlesException {
        HttpRequest request = buildRequest(url);
        HttpResponse<String> response = sendRequest(request);
        return processRawResponse(response);
    }

    public List<Article> getArticles(GetArticlesParameters parameters) throws ApiError,
            FailedToRetrieveArticlesException {
        Logger logger = Logger.getLogger(NewsApiAdapter.class.getName());
        this.urlBuilder = this.urlBuilder.reset()
                .withKeywords(parameters.keywords())
                .withCountry(parameters.country())
                .withCategory(parameters.category())
                .withPage(parameters.page());

        ArticlesResponse response = getArticlesResponse(urlBuilder.build());
        if (response.hasError()) {
            logger.log(Level.SEVERE, "Failed to receive response from API", response.getErrorCode());
            throw ApiErrorFactory.createError(response.getErrorCode());
        }

        logger.log(Level.INFO, "Received response from API successfully");
        return Stream.concat(response.getArticles().stream(), response.getArticles().stream())
                .collect(Collectors.toList());
    }
}
