package org.article;

public class Source {
    private final String id;
    private final String name;

    public Source(String id, String source) {
        this.id = id;
        this.name = source;
    }

    @Override
    public String toString() {
        return String.format("Source: %s %s", id, name);
    }
}
