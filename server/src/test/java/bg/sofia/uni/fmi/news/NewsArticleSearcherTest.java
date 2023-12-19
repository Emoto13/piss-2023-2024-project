package test.java.bg.sofia.uni.fmi.news;

import bg.sofia.uni.fmi.news.article.Article;
import bg.sofia.uni.fmi.news.ArticleSearcher;
import bg.sofia.uni.fmi.news.NewsArticleSearcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class NewsArticleSearcherTest {
    private ArticleSearcher searcher;

    @BeforeEach
    void setupTest() {
        String apiKey = "e0bb5055b1a144b78180f51abb33644f";
        this.searcher = new NewsArticleSearcher(apiKey);
    }

    @Test
    void TestSearchArticlesBy_EmptyKeywords() {
        assertThrows(IllegalArgumentException.class, () -> {
            searcher.searchArticlesBy(new ArrayList<>(), "", "");
        }, "should throw exception when keywords are empty");
    }

    @Test
    void TestSearchArticlesBy_InvalidAPIKey() {
        assertDoesNotThrow(() -> {
            ArticleSearcher testSearcher = new NewsArticleSearcher("invalid-api-key");
            List<Article> articles = testSearcher.searchArticlesBy(List.of("keyword"), "", "");
            assertNull(articles, "articles shou;d be null");
        }, "should not handle exception when api key is invalid");
    }

    @Test
    void TestSearchArticlesBy_WorksCorrectly() {
        assertDoesNotThrow(() -> {
            List<Article> articles = searcher.searchArticlesBy(List.of("keyword"), "", "");
            assertNotNull(articles, "articles should not be null");
        }, "should not handle exception when api key is invalid");
    }
}
