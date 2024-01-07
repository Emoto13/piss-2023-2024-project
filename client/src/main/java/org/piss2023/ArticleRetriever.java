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
        int thmsPerThread = 5;
        List<ImageFetcher> fetchers = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for(int i = 0; i < n; i++) {
            fetchers.add(new ImageFetcher(Arrays.copyOfRange(articles, i*thmsPerThread, i*thmsPerThread + thmsPerThread)));
            threads.add(new Thread(fetchers.get(i)));
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
        String requestUrl = String.format("http://localhost:8000/articles?country=%s&category=%s", country, category);
        if (!Objects.equals(keywords, "") && !Objects.equals(keywords.charAt(0), ',')) {
            requestUrl += "&keywords=" + keywords;
        }

        HttpURLConnection conn = sendRequestToServer(requestUrl);
        assert conn != null;
        String response = getServerResponse(conn);

        Gson gson = new Gson();
        Article[] articles = gson.fromJson(response, Article[].class);

        if (enabledThumbnails) {
            // Fetch icons for every article
            fetchIcons(articles);
        }

        if (articles.length > 0) {
            return Arrays.copyOfRange(articles, 0, 20);
        }
        return null;
    }
}
