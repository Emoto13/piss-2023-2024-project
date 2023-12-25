package bg.sofia.uni.fmi.news;

import java.net.InetSocketAddress;

import bg.sofia.uni.fmi.news.handler.NewsArticleHandler;
import bg.sofia.uni.fmi.news.searcher.ArticleSearcher;
import bg.sofia.uni.fmi.news.searcher.NewsArticleSearcher;
import com.sun.net.httpserver.HttpServer;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("STARTING HTTP SERVER");
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        String apiKey = "e0bb5055b1a144b78180f51abb33644f";
        ArticleSearcher searcher = new NewsArticleSearcher(apiKey);
        server.createContext("/articles", new NewsArticleHandler(searcher));
        server.setExecutor(null); // creates a default executor
        server.start();
    }

}