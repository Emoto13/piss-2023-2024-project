package bg.sofia.uni.fmi.news.adapter;

import bg.sofia.uni.fmi.news.article.Article;
import bg.sofia.uni.fmi.news.exception.ApiError;
import bg.sofia.uni.fmi.news.exception.ApiErrorFactory;
import bg.sofia.uni.fmi.news.exception.FailedToRetrieveArticlesException;
import bg.sofia.uni.fmi.news.url.PaginatedUrlBuilder;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewApiAdapter implements ApiAdapter {
    private final HttpClient httpClient;
    private PaginatedUrlBuilder urlBuilder;

    public NewApiAdapter(PaginatedUrlBuilder urlBuilder, HttpClient httpClient) {
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

    private List<Article> getPaginatedArticles(int totalResults) throws ApiError, FailedToRetrieveArticlesException {
        List<Article> articles = new ArrayList<>();
        while (urlBuilder.getSeenArticles() < totalResults) {
            urlBuilder = urlBuilder.withNextPage();
            ArticlesResponse response = getArticlesResponse(urlBuilder.build());
            if (response.hasError()) {
                throw ApiErrorFactory.createError(response.getErrorCode());
            }

            articles.addAll(response.getArticles());
        }
        return articles;
    }

    public List<Article> getArticles(GetArticlesParameters parameters) throws ApiError,
            FailedToRetrieveArticlesException {
        this.urlBuilder = this.urlBuilder.reset()
                .withKeywords(parameters.keywords())
                .withCountry(parameters.country())
                .withCategory(parameters.category());

        ArticlesResponse initialResponse = getArticlesResponse(urlBuilder.build());
        if (initialResponse.hasError()) {
            throw ApiErrorFactory.createError(initialResponse.getErrorCode());
        }

        List<Article> paginatedArticles = getPaginatedArticles(initialResponse.getTotalResults());
        return Stream.concat(initialResponse.getArticles().stream(), paginatedArticles.stream())
                .collect(Collectors.toList());
    }
}
