package bg.sofia.uni.fmi.news.url;

import java.util.Collection;

public interface PaginatedUrlBuilder {
    NewsUrlBuilder reset();
    NewsUrlBuilder withPage(int page);

    NewsUrlBuilder withCountry(String country);

    NewsUrlBuilder withCategory(String category);

    NewsUrlBuilder withKeywords(Collection<String> keywords);

    String build();
}
