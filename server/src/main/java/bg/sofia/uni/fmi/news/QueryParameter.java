package bg.sofia.uni.fmi.news;

public enum QueryParameter {
    COUNTRY("country"),
    KEYWORDS("keywords"),
    CATEGORY("category"),
    PAGE("page");

    private final String value;

    QueryParameter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
