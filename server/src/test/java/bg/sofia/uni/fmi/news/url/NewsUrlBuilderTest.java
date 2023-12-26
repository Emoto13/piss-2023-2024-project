package bg.sofia.uni.fmi.news.url;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class NewsUrlBuilderTest {
    private PaginatedUrlBuilder urlBuilder;
    private String apiKey;

    @BeforeEach
    void setupTest() {
        this.apiKey = "api-key";
        this.urlBuilder = new NewsUrlBuilder(apiKey);
    }

    @Test
    void TestInvalidApiKeyShouldThrowException() {
        List<String> apiKeys = Arrays.asList(
                null,
                "",
                " "
        );
        List<Exception> exceptions = Arrays.asList(
                new IllegalArgumentException(),
                new IllegalArgumentException(),
                new IllegalArgumentException()
        );

        for (int i = 0; i < apiKeys.size(); i++) {
            String apiKey = apiKeys.get(i);
            Exception exception = exceptions.get(i);

            assertThrows(exception.getClass(), () -> {
                new NewsUrlBuilder(apiKey);
            }, "should throw exception upon invalid api key");
        }
    }

    @Test
    void TestWithNextPageWorksCorrectly() {
        int page = 1;
        urlBuilder = urlBuilder.withPage(page);

        assertTrue(urlBuilder.build().contains("page=1"), "should calculate initial page correctly");
        urlBuilder = urlBuilder.withPage(++page);
        assertTrue(urlBuilder.build().contains("page=2"), "should calculate incremented seen pages correctly");
    }

    @Test
    void TestWithKeywordsThrowsExceptionUponInvalidValue() {
        List<Collection<String>> keywordsList = Arrays.asList(
                new ArrayList<>(),
                List.of("")
        );

        for (int i = 0; i < keywordsList.size(); i++) {
            Collection<String> keywords = keywordsList.get(i);
            assertEquals(urlBuilder, urlBuilder.withKeywords(keywords), "url builder should not change upon empty or missing keywords");
        }
    }

    @Test
    void TestBuildFlowWorksCorrectlyWithAllParameters() {
        String keyword = "keyword";
        String country = "some-country";
        String category = "some-category";
        Collection<String> keywords = List.of(keyword);
        String baseURL = "https://newsapi.org/v2/top-headlines";

        urlBuilder = urlBuilder.reset().withCategory(category).withKeywords(keywords).withCountry(country);
        String url = urlBuilder.build();
        assertTrue(url.contains(baseURL), "should contain base url");
        assertTrue(url.contains(String.format("%s=%s", UrlParameter.COUNTRY.getName(), country)),
                "should contain country parameter");
        assertTrue(url.contains(String.format("%s=%s", UrlParameter.CATEGORY.getName(), category)),
                "should contain category parameter");
        assertTrue(url.contains(String.format("%s=%s", UrlParameter.KEYWORDS.getName(), keyword)),
                "should contain keywords parameter");
        assertTrue(url.contains(String.format("%s=%s", UrlParameter.API_KEY.getName(), apiKey)),
                "should contain api key parameter");
        assertTrue(url.contains(String.format("%s=%s", UrlParameter.PAGE.getName(), "1")),
                "should contain page parameter");
        assertTrue(url.contains(String.format("%s=%s", UrlParameter.PAGE_SIZE.getName(), String.valueOf(NewsUrlBuilder.PAGE_SIZE))),
                "should contain page size parameter");
    }

    @Test
    void TestBuildFlowWorksCorrectlyWithSomeParamters() {
        String keyword = "keyword";
        String country = "some-country";
        Collection<String> keywords = List.of(keyword);
        String baseURL = "https://newsapi.org/v2/top-headlines";

        urlBuilder = urlBuilder.reset().withKeywords(keywords).withCountry(country);
        String url = urlBuilder.build();
        assertTrue(url.contains(baseURL), "should contain base url");
        assertTrue(url.contains(String.format("%s=%s", UrlParameter.COUNTRY.getName(), country)),
                "should contain country parameter");
        assertTrue(url.contains(String.format("%s=%s", UrlParameter.KEYWORDS.getName(), keyword)),
                "should contain keywords parameter");
        assertTrue(url.contains(String.format("%s=%s", UrlParameter.API_KEY.getName(), apiKey)),
                "should contain api key parameter");
        assertTrue(url.contains(String.format("%s=%s", UrlParameter.PAGE.getName(), "1")),
                "should contain page parameter");
        assertTrue(url.contains(String.format("%s=%s", UrlParameter.PAGE_SIZE.getName(), String.valueOf(NewsUrlBuilder.PAGE_SIZE))),
                "should contain page size parameter");

        assertFalse(url.contains(String.format("%s=%s", UrlParameter.CATEGORY.getName(), "")),
                "should not contain category parameter");
    }
}
