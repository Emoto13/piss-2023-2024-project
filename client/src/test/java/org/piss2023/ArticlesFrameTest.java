package org.piss2023;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(1, articlesFrame.getComponentCount());
    }

    @Test
    void isMainPanelProperlyLoaded() {
        assertEquals(2, articlesFrame.mainPanel.getComponentCount());
    }


}
