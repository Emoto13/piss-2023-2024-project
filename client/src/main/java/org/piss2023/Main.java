package org.piss2023;

import com.google.gson.Gson;
import org.article.Article;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    // Send GET request to server and return list of articles
    public static Article[] search(String country, String category, String keywords, boolean enabledThumbnails) {
        try {
            String serverUrl = String.format("http://localhost:8000/articles?country=%s&category=%s", country, category);
            if (!Objects.equals(keywords, "") && !Objects.equals(keywords.charAt(0), ',')) {
                serverUrl += "&keywords=" + keywords;
            }
            URL url = new URL(serverUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Gson gson = new Gson();
            Article[] articlesResponse = gson.fromJson(response.toString(), Article[].class);

            if (enabledThumbnails) {
                // Fetch icons for every article
                for (var article :
                        articlesResponse) {
                    ImageIcon icon = new ImageIcon(ArticlesFrame.class.getResource("no_preview.png"));
                    if (article.getUrlToImage() != null) {
                        try {
                            URL icon_url = new URL(article.getUrlToImage());
                            icon = new ImageIcon(icon_url);
                            Image image = icon.getImage();
                            image = image.getScaledInstance(80, 50, Image.SCALE_SMOOTH);
                            icon = new ImageIcon(image);

                            // Add the label to your frame or panel
                        } catch (MalformedURLException ex) {
                            ex.printStackTrace();
                        }
                    }
                    article.setIcon(icon);
                }
            }
            in.close();
            return Arrays.copyOfRange(articlesResponse, 0, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Setting up Java Swing UI
    public static void main(String[] args) {
        ArticlesFrame frame = new ArticlesFrame(600, 800);
        frame.setLayout(new CardLayout());

        JPanel view = new JPanel();
        JPanel mainParams = new JPanel();
        JPanel extraParams = new JPanel();
        view.add(mainParams);
        view.add(extraParams);

        JLabel statusLabel = new JLabel("Ready.");
        JLabel countryLabel = new JLabel("Country code:");
        final String[] countries = {"ae", "ar", "at", "au", "be", "bg", "br", "ca", "ch", "cn", "co", "cu", "cz", "de", "eg", "fr", "gb", "gr", "hk", "hu", "id", "ie", "il", "in", "it", "jp", "kr", "lt", "lv", "ma", "mx", "my", "ng", "nl", "no", "nz", "ph", "pl", "pt", "ro", "rs", "ru", "sa", "se", "sg", "si", "sk", "th", "tr", "tw", "ua", "us", "ve", "za"};
        JComboBox<String> byCountryBox = new JComboBox<>(countries);
        byCountryBox.setPreferredSize(new Dimension(50, 30));

        JLabel categoryLabel = new JLabel("Category:");
        final String[] categories = {"business", "entertainment", "general", "health", "science", "sports", "technology"};
        JComboBox<String> byCategoryBox = new JComboBox<>(categories);
        byCategoryBox.setPreferredSize(new Dimension(150, 30));

        JLabel keywordsLabel = new JLabel("Keywords:");
        JTextField keywordsPrompt = new JTextField();
        keywordsPrompt.setToolTipText("Please provide the keywords like this - dog cat animal (no commas and one space between words)");
        keywordsPrompt.setPreferredSize(new Dimension(250,30));

        JCheckBox enableThumbnails = new JCheckBox("Enable thumbnails:");
        enableThumbnails.setToolTipText("Will show images for the articles, may significantly slower the fetching.");

        JButton searchButton = new JButton("Търси");

        mainParams.add(statusLabel);
        mainParams.add(countryLabel);
        mainParams.add(byCountryBox);
        mainParams.add(categoryLabel);
        mainParams.add(byCategoryBox);
        extraParams.add(keywordsLabel);
        extraParams.add(keywordsPrompt);
        extraParams.add(enableThumbnails);
        extraParams.add(searchButton);
        frame.add(view);

        // Search button activates the sending of GET requests
        searchButton.addActionListener(e -> {
            String country = "us";
            String category = "general";
            String keywords = keywordsPrompt.getText().trim().replace(' ', ',');
            if (!Objects.equals(byCountryBox.getSelectedItem().toString(), "")) {
                country = byCountryBox.getSelectedItem().toString();
            }
            if (!Objects.equals(byCategoryBox.getSelectedItem().toString(), "")) {
                category = byCategoryBox.getSelectedItem().toString();
            }

            statusLabel.setText("Loading...");
            view.revalidate();
            view.repaint();

            // Dialog message that enables changing the status indication while the method is executing
            JOptionPane.showMessageDialog(null, "Loading", "Status", JOptionPane.INFORMATION_MESSAGE);
            Article[] articlesResponse = search(country, category, keywords, enableThumbnails.isSelected());
            JList<Article> list = new JList<>(articlesResponse);
            JScrollPane scrollPane = new JScrollPane(list);
            scrollPane.setPreferredSize(new Dimension(550, 600));
            list.setFixedCellHeight(50);

            ArticleRenderer renderer = new ArticleRenderer();
            renderer.fasterLoad = !enableThumbnails.isSelected();
            list.setCellRenderer(renderer);

            if (view.getComponentCount() > 2) {
                view.remove(view.getComponent(2));
            }
            view.add(scrollPane);

            statusLabel.setText("Ready.");
            view.revalidate();
            view.repaint();
        });
        frame.setUpGUI();
    }
}