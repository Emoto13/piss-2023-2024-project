package bg.sofia.uni.fmi.news.handler;

import bg.sofia.uni.fmi.news.searcher.ArticleSearcher;
import com.sun.net.httpserver.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;


import java.io.OutputStream;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NewsArticleHandlerTest {
    @Spy
    private final ArticleSearcher searcher = mock(ArticleSearcher.class);
    private final HttpExchange exchange = mock(HttpExchange.class);
    private final URI uri = mock(URI.class);
    private final OutputStream outputStream = mock(OutputStream.class);
    private HttpHandler handler;


    @BeforeEach
    void setupTest() {
        this.handler = new NewsArticleHandler(searcher);
    }

    @Test
    void TestHandle_Success() {
        when(
                exchange.getRequestURI()
        ).thenReturn(uri);
        when(uri.getQuery()).thenReturn("keywords=musk&country=US&category=business");
        when(
                exchange.getResponseBody()
        ).thenReturn(outputStream);

        assertDoesNotThrow(() -> {
            this.handler.handle(exchange);
        });

        assertNotNull(exchange.getResponseBody());
    }

    @Test
    void TestHandle_EmptyQuery() {
        when(
                exchange.getRequestURI()
        ).thenReturn(uri);
        when(uri.getQuery()).thenReturn(null);
        when(
                exchange.getResponseBody()
        ).thenReturn(outputStream);

        assertDoesNotThrow(() -> {
            this.handler.handle(exchange);
        });

        assertNotNull(exchange.getResponseBody());
    }

}
