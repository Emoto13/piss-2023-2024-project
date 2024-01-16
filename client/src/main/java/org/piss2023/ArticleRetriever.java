package org.piss2023;

import com.google.gson.Gson;
import org.article.Article;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticleRetriever {
    // Send GET request to server and return list of articles
    public static HttpURLConnection sendRequestToServer(String serverUrl) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Receive response from server and record it to a string
    public static String getServerResponse(HttpURLConnection conn) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        return response.toString();
    }

    public static void fetchIcons(Article[] articles) {
        int n = 4;
        int imgsPerThread = (int)(articles.length / 4.0);
        List<ImageFetcher> fetchers = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        if (articles.length <= 4) {
            fetchers.add(new ImageFetcher(Arrays.copyOfRange(articles, 0, articles.length)));
            threads.add(new Thread(fetchers.get(0)));
        }
        else {
            for (int i = 0; i < n - 1; i++) {
                fetchers.add(new ImageFetcher(Arrays.copyOfRange(articles, i * imgsPerThread, i * imgsPerThread + imgsPerThread)));
                threads.add(new Thread(fetchers.get(i)));
            }
            fetchers.add(new ImageFetcher(Arrays.copyOfRange(articles, (n - 1) * imgsPerThread, articles.length)));
            threads.add(new Thread(fetchers.get(n - 1)));
        }

        for (Thread t : threads) {
            t.start();
        }

        while (true) {
            boolean allAreDone = true;
            for (Thread t : threads) {
                allAreDone = allAreDone && !t.isAlive();
            }

            if (allAreDone) {
                break;
            }
        }

        List<Article> result = new ArrayList<>();
        for (ImageFetcher f : fetchers) {
            result.addAll(f.getArticles());
        }

        for (int i = 0; i < result.size(); i++) {
            articles[i] = result.get(i);
        }
    }

    public static Article[] retrieveArticles(String country, String category, String keywords, boolean enabledThumbnails) throws IOException {
        Logger logger = Logger.getLogger(ArticleRetriever.class.getName());
        String requestUrl = String.format("http://localhost:8000/articles?country=%s&category=%s", country, category);
        if (!Objects.equals(keywords, "") && !Objects.equals(keywords.charAt(0), ',')) {
            requestUrl += "&keywords=" + keywords;
        }

        HttpURLConnection conn = sendRequestToServer(requestUrl);
        if(conn == null) {
            logger.log(Level.SEVERE, "Could not establish a connection");
        }
        assert conn != null;
        String response = getServerResponse(conn);

        Gson gson = new Gson();
        Article[] articles = gson.fromJson(response, Article[].class);

        if (enabledThumbnails) {
            // Fetch icons for every article
            logger.log(Level.INFO, "Initiating images fetching process");
            fetchIcons(articles);
            logger.log(Level.INFO, "Images fetching process done");
        }

        if (articles.length > 0) {
            return Arrays.copyOfRange(articles, 0, Math.min(articles.length, 50));
        }
        return null;
    }
}
