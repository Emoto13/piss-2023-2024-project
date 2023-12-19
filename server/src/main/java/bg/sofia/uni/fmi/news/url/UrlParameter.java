package bg.sofia.uni.fmi.news.url;

public enum UrlParameter {
    COUNTRY("country"),
    CATEGORY("category"),
    KEYWORDS("q"),
    API_KEY("apiKey"),
    PAGE_SIZE("pageSize"),
    PAGE("page");

    private final String name;

    UrlParameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
