package bg.sofia.uni.fmi.news.adapter;

import bg.sofia.uni.fmi.news.exception.ApiErrorFactory;
import bg.sofia.uni.fmi.news.exception.ApiErrorType;
import bg.sofia.uni.fmi.news.exception.FailedToRetrieveArticlesException;
import bg.sofia.uni.fmi.news.url.NewsUrlBuilder;
import bg.sofia.uni.fmi.news.article.Article;
import bg.sofia.uni.fmi.news.article.Source;
import bg.sofia.uni.fmi.news.url.PaginatedUrlBuilder;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.ProxySelector;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewsApiAdapterTest {
    // NOTE: This test is commented out as the grader doesn't pass with it
    class MockHttpClient extends HttpClient {
        public MockHttpClient() {
        }

        @Override
        public Optional<CookieHandler> cookieHandler() {
            return Optional.empty();
        }

        @Override
        public Optional<Duration> connectTimeout() {
            return Optional.empty();
        }

        @Override
        public Redirect followRedirects() {
            return null;
        }

        @Override
        public Optional<ProxySelector> proxy() {
            return Optional.empty();
        }

        @Override
        public SSLContext sslContext() {
            return null;
        }

        @Override
        public SSLParameters sslParameters() {
            return null;
        }

        @Override
        public Optional<Authenticator> authenticator() {
            return Optional.empty();
        }

        @Override
        public Version version() {
            return null;
        }

        @Override
        public Optional<Executor> executor() {
            return Optional.empty();
        }

        @Override
        public <T> HttpResponse<T> send(HttpRequest httpRequest, HttpResponse.BodyHandler<T> bodyHandler) throws IOException, InterruptedException {
            return null;
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest httpRequest, HttpResponse.BodyHandler<T> bodyHandler) {
            return null;
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest httpRequest, HttpResponse.BodyHandler<T> bodyHandler, HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
            return null;
        }
    }

    @Spy
    private final HttpClient httpClientMock = mock(MockHttpClient.class);
    private final HttpResponse httpResponseMock = mock(HttpResponse.class);
    private ApiAdapter adapter;
    private Gson gson;

    @BeforeEach
    void setupTest() throws IOException, InterruptedException {
        this.gson = new Gson();
        PaginatedUrlBuilder urlBuilder = new NewsUrlBuilder("api-key");
        this.adapter = new NewsApiAdapter(urlBuilder, httpClientMock);
        when(
                httpClientMock.send(any(HttpRequest.class), any())
        ).thenReturn(httpResponseMock);
    }

    @Test
    void TestGetArticlesWithApiError() {
        ApiErrorType errorType = ApiErrorType.SOURCE_DOES_NOT_EXIST;
        ArticlesResponse errorCriticalResponse = new ArticlesResponse(Status.ERROR.getValue(), errorType.getName(),
                0, new ArrayList<>());
        String json = gson.toJson(errorCriticalResponse);

        when(httpResponseMock.body()).thenReturn(json);
        Assertions.assertThrows(ApiErrorFactory.createError(errorType.getName()).getClass(), () -> {
            adapter.getArticles(new GetArticlesParameters(List.of("money"), "", "", 1));
        }, "should throw api error when error is critical");
    }

    @Test
    void TestGetArticlesRetrievesArticlesCorrectly() {
        Article article = new Article(new Source("id", "source"), "author", "title",
                "description", "url.com", "urlToImage.com", "content");
        ArticlesResponse successResponse = new ArticlesResponse(Status.OK.getValue(), "", 100, List.of(article));

        String json = gson.toJson(successResponse);
        when(httpResponseMock.body()).thenReturn(json);

        assertDoesNotThrow(() -> {
            List<Article> articles = adapter.getArticles(new GetArticlesParameters(List.of("money"), "", "", 1));
            assertFalse(articles.isEmpty(), "articles should not be empty when cannot send request");
        }, "should not throw exception if retrieves articles successfully");
    }


    @Test
    void TestGetArticlesWithFailureToSendRequest() throws IOException, InterruptedException {
        when(
                httpClientMock.send(any(HttpRequest.class), any())
        ).thenThrow(IOException.class);

        Assertions.assertThrows(FailedToRetrieveArticlesException.class, () -> {
            adapter.getArticles(new GetArticlesParameters(List.of("money"), "", "", 1));
        }, "should throw failed to retrieve when cannot send request");
    }

}

