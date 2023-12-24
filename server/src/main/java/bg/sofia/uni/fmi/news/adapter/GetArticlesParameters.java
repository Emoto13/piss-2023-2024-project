package bg.sofia.uni.fmi.news.adapter;

import java.util.Collection;

public record GetArticlesParameters(Collection<String> keywords, String country, String category, int page) {
}
