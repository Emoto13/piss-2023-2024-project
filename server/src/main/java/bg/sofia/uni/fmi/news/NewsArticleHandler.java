package bg.sofia.uni.fmi.news;

import bg.sofia.uni.fmi.news.article.Article;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class NewsArticleHandler implements HttpHandler {
    private final ArticleSearcher searcher;
    public NewsArticleHandler(ArticleSearcher searcher) {
        this.searcher = searcher;
    }

    @Override
    public void handle(HttpExchange t) {
        try {
            String query = t.getRequestURI().getQuery();
            Map<String, String> queryMap = queryToMap(query);


            Collection<String> keywords = Arrays.stream(queryMap.getOrDefault(QueryParameter.KEYWORDS.getValue(), "").
                            split(",")).
                    toList();
            String country = queryMap.getOrDefault(QueryParameter.COUNTRY.getValue(), "");
            String category = queryMap.getOrDefault(QueryParameter.CATEGORY.getValue(), "");
            Integer page = Integer.valueOf(queryMap.getOrDefault(QueryParameter.PAGE.getValue(), "0"));

            List<Article> articles = this.searcher.searchArticlesBy(keywords, country, category, page);
            String response = new Gson().toJson(articles);

            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            t.sendResponseHeaders(200, bytes.length);
            OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        } catch (Exception e) {
            System.out.printf("Oops exception %s", e);
        }
    }

    public Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();

        if(query == null) {
            return result;
        }

        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
