package org.piss2023;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArticlesFrameTest {
    private ArticlesFrame articlesFrame;

    @BeforeEach
    void setup() {
        int w = 600;
        int h = 800;
        articlesFrame = new ArticlesFrame(w, h);
    }

    @Test
    void isFrameSuccessfullyCreated() {
         assertTrue(articlesFrame.getComponentCount() == 1);
    }

    @Test
    void isMainPanelProperlyLoaded() {
        assertTrue(articlesFrame.mainPanel.getComponentCount() == 2);
    }


}
