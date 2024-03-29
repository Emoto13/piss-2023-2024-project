package org.piss2023;

import org.article.Article;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticlesFrame extends JFrame {
    final String[] countries = {"ae", "ar", "at", "au", "be",
            "bg", "br", "ca", "ch", "cn", "co", "cu", "cz", "de", "eg",
            "fr", "gb", "gr", "hk", "hu", "id", "ie", "il", "in", "it",
            "jp", "kr", "lt", "lv", "ma", "mx", "my", "ng", "nl", "no",
            "nz", "ph", "pl", "pt", "ro", "rs", "ru", "sa", "se", "sg",
            "si", "sk", "th", "tr", "tw", "ua", "us", "ve", "za"
    };
    final String[] categories = {"business", "entertainment", "general", "health", "science", "sports", "technology"};
    private final int width;
    private final int height;
    public JPanel mainPanel;
    public JPanel mainParamsPanel;
    public JPanel extraParamsPanel;

    public ArticlesFrame(int w, int h) {
        width = w;
        height = h;

        this.setLayout(new CardLayout());

        mainPanel = new JPanel();
        mainParamsPanel = new JPanel();
        extraParamsPanel = new JPanel();
        mainPanel.add(mainParamsPanel);
        mainPanel.add(extraParamsPanel);
    }

    public void setUpGUI() {
        Logger logger = Logger.getLogger(ArticlesFrame.class.getName());
        logger.log(Level.INFO, "Setting up application layout...");
        JLabel statusLabel = new JLabel("Load ready.");
        JLabel countryLabel = new JLabel("Country code:");
        JComboBox<String> byCountryBox = new JComboBox<>(countries);
        byCountryBox.setPreferredSize(new Dimension(70, 30));

        JLabel categoryLabel = new JLabel("Category:");
        JComboBox<String> byCategoryBox = new JComboBox<>(categories);
        byCategoryBox.setPreferredSize(new Dimension(150, 30));

        JLabel keywordsLabel = new JLabel("Keywords:");
        JTextField keywordsPrompt = new JTextField();
        keywordsPrompt.setToolTipText("Please provide the keywords like this - dog cat animal (no commas and one space between words)");
        keywordsPrompt.setPreferredSize(new Dimension(250,30));

        JCheckBox enableThumbnails = new JCheckBox("Enable thumbnails:");
        enableThumbnails.setToolTipText("Will show images for the articles, may significantly slower the fetching.");

        JButton searchButton = new JButton("Search");

        mainParamsPanel.add(statusLabel);
        mainParamsPanel.add(countryLabel);
        mainParamsPanel.add(byCountryBox);
        mainParamsPanel.add(categoryLabel);
        mainParamsPanel.add(byCategoryBox);
        extraParamsPanel.add(keywordsLabel);
        extraParamsPanel.add(keywordsPrompt);
        extraParamsPanel.add(enableThumbnails);
        extraParamsPanel.add(searchButton);
        this.add(mainPanel);

        // Search button activates the sending of GET requests
        searchButton.addActionListener(e -> {
            logger.log(Level.INFO, "Search button pressed");
            String country = "us";
            String category = "general";
            String keywords = keywordsPrompt.getText().trim().replace(' ', ',');
            if (!Objects.equals(Objects.requireNonNull(byCountryBox.getSelectedItem()).toString(), "")) {
                country = byCountryBox.getSelectedItem().toString();
            }
            if (!Objects.equals(Objects.requireNonNull(byCategoryBox.getSelectedItem()).toString(), "")) {
                category = byCategoryBox.getSelectedItem().toString();
            }

            statusLabel.setText("Loading...");
            statusLabel.paintImmediately(statusLabel.getVisibleRect());

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            
            Article[] articlesResponse;
            try {
                articlesResponse = ArticleRetriever.retrieveArticles(country, category, keywords, enableThumbnails.isSelected());
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Articles could not be fetched");
                throw new RuntimeException(ex);
            }
            if (articlesResponse != null) {
                JList<Article> list = new JList<>(articlesResponse);
                JScrollPane scrollPane = new JScrollPane(list);
                scrollPane.setPreferredSize(new Dimension(550, 600));
                list.setFixedCellHeight(50);

                ArticleRenderer renderer = new ArticleRenderer();
                renderer.fasterLoad = !enableThumbnails.isSelected();
                list.setCellRenderer(renderer);

                if (mainPanel.getComponentCount() > 2) {
                    mainPanel.remove(mainPanel.getComponent(2));
                }
                mainPanel.add(scrollPane);

                statusLabel.setText("Load ready.");
                mainPanel.revalidate();
                mainPanel.repaint();
            }
            else {
                logger.log(Level.WARNING, "No articles retrieved");
                if (mainPanel.getComponentCount() > 2) {
                    mainPanel.remove(mainPanel.getComponent(2));
                }
                statusLabel.setText("No results.");
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        setSize(width, height);
        setLocationRelativeTo(null);
        setTitle("Articles");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        logger.log(Level.INFO, "Application layout set up successfully.");
    }
}
