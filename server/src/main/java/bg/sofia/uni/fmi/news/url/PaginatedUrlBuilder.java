package bg.sofia.uni.fmi.news.url;

import java.util.Collection;

public interface PaginatedUrlBuilder {
    NewsUrlBuilder reset();
    NewsUrlBuilder withNextPage();

    NewsUrlBuilder withCountry(String country);

    NewsUrlBuilder withCategory(String category);

    NewsUrlBuilder withKeywords(Collection<String> keywords);

    int getSeenArticles();

    String build();
}
