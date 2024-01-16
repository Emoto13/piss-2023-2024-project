package bg.sofia.uni.fmi.news.adapter;

import bg.sofia.uni.fmi.news.article.Article;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArticleFetcher implements Runnable {
    private final int page;
    private final HttpClient httpClient;
    private PaginatedUrlBuilder urlBuilder;
    private List<Article> articles;

    public ArticleFetcher(PaginatedUrlBuilder urlBuilder, HttpClient httpClient, int page) {
        this.urlBuilder = urlBuilder;
        this.httpClient = httpClient;
        this.page = page;
        this.articles = new ArrayList<>();
    }

    public List<Article> getArticles() {
        return articles;
    }

    private HttpRequest buildRequest(String url) {
        return HttpRequest.newBuilder(URI.create(url)).build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) {
        Logger logger = Logger.getLogger(ArticleFetcher.class.getName());
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            logger.log(Level.WARNING, "Failed to retrieve articles", e.getMessage());
        }
        return null;
    }

    private ArticlesResponse processRawResponse(HttpResponse<String> response) {
        Gson gson = new Gson();
        return gson.fromJson(response.body(), ArticlesResponse.class);
    }

    public ArticlesResponse getArticlesResponse(String url) {
        HttpRequest request = buildRequest(url);
        HttpResponse<String> response = sendRequest(request);
        if (response == null) {
            return new ArticlesResponse(Status.ERROR.getValue(), "Failed to fetch articles", 0, new ArrayList<>());
        }
        return processRawResponse(response);
    }

    @Override
    public void run() {
        Logger logger = Logger.getLogger(ArticleFetcher.class.getName());
        this.urlBuilder = urlBuilder.withPage(this.page);

        ArticlesResponse response = this.getArticlesResponse(urlBuilder.build());
        if (response.hasError()) {
            logger.log(Level.SEVERE, "Failed to receive response from API", response.getErrorCode());
        }

        logger.log(Level.INFO, "Received response from API successfully");
        if (response.getArticles() == null) {
            return;
        }
        this.articles = response.getArticles();
    }
}
