package bg.sofia.uni.fmi.news.searcher;

import bg.sofia.uni.fmi.news.article.Article;

import java.util.Collection;
import java.util.List;

public interface ArticleSearcher {
    List<Article> searchArticlesBy(Collection<String> keywords, String country, String category, Integer page);
}
