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


    public List<Article> getArticles(GetArticlesParameters parameters) {
        Logger logger = Logger.getLogger(NewsApiAdapter.class.getName());
        this.urlBuilder = this.urlBuilder.reset()
                .withKeywords(parameters.keywords())
                .withCountry(parameters.country())
                .withCategory(parameters.category());

        int n = 2;
        List<ArticleFetcher> fetchers = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            fetchers.add(new ArticleFetcher(this.urlBuilder.copy(), this.httpClient,1));
            threads.add(new Thread(fetchers.get(i)));
        }

        logger.log(Level.INFO, "Starting threads");
        for (Thread t : threads) {
            t.start();
        }

        while (true) {
            boolean allAreDone = true;
            for (Thread t : threads) {
                allAreDone = allAreDone && !t.isAlive();
            }

            if (allAreDone) {
                break;
            }
        }

        logger.log(Level.INFO, "Threads are done, combining results");
        List<Article> result = new ArrayList<>();
        for (ArticleFetcher f: fetchers) {
            result.addAll(f.getArticles());
        }

        logger.log(Level.INFO, String.format("Result has this many items: %d", result.size()));
        return result;
    }
}
