package bg.sofia.uni.fmi.news.url;

import bg.sofia.uni.fmi.news.verification.Verifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class NewsUrlBuilder implements PaginatedUrlBuilder {
    public static final int PAGE_SIZE = 50;
    public static final String ENDPOINT = "https://newsapi.org/v2/top-headlines";
    private List<String> keywords;
    private String apiKey;
    private String country;
    private String category;
    private int page;

    public NewsUrlBuilder(String apiKey) {
        this.setApiKey(apiKey);
        this.keywords = new ArrayList<>();
        this.country = null;
        this.category = null;
        this.page = 1;
    }

    private void setApiKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("Api key must not be empty or null");
        }
        this.apiKey = apiKey;
    }

    private String parameterOrNull(String parameterName, String value) {
        if (Verifier.isEmptyOrNull(value)) {
            return null;
        }
        return toParameter(parameterName, value);
    }

    private String toParameter(String parameterName, String value) {
        return String.format("%s=%s", parameterName, value);
    }

    @Override
    public NewsUrlBuilder reset() {
        this.country = null;
        this.category = null;
        this.page = 1;
        this.keywords = new ArrayList<>();
        return this;
    }

    public NewsUrlBuilder withPage(int page) {
        this.page = page;
        return this;
    }

    public NewsUrlBuilder withCountry(String country) {
        this.country = country;
        return this;
    }

    public NewsUrlBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    public NewsUrlBuilder withKeywords(Collection<String> keywords) {
        if (keywords.isEmpty() || Verifier.containsEmptyOrNull(keywords)) {
            return this;
        }
        this.keywords.addAll(keywords);
        return this;
    }


    public String build() {
        String keywordsParameter = parameterOrNull(UrlParameter.KEYWORDS.getName(), String.join("+", keywords));
        String countryParameter = parameterOrNull(UrlParameter.COUNTRY.getName(), country);
        String categoryParameter = parameterOrNull(UrlParameter.CATEGORY.getName(), category);
        String apiKeyParameter = parameterOrNull(UrlParameter.API_KEY.getName(), apiKey);
        String pageParameter = toParameter(UrlParameter.PAGE.getName(), String.valueOf(page));
        String pageSizeParameter = toParameter(UrlParameter.PAGE_SIZE.getName(), String.valueOf(PAGE_SIZE));

        List<String> parameters = Stream.of(keywordsParameter, countryParameter, categoryParameter,
                        apiKeyParameter, pageParameter, pageSizeParameter)
                .filter((value) -> !Verifier.isEmptyOrNull(value)).toList();

        return String.format("%s?%s", NewsUrlBuilder.ENDPOINT, String.join("&", parameters));
    }

    @Override
    public NewsUrlBuilder copy() {
        return new NewsUrlBuilder(this.apiKey).
                withKeywords(this.keywords).
                withCountry(this.country).
                withCategory(this.category).
                withPage(this.page);
    }
}
