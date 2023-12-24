package bg.sofia.uni.fmi.news;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {

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