package org.piss2023;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ArticleRetrieverTest {
    private URL urlMock;

    @BeforeEach
    void setup() {
        this.urlMock = mock(URL.class);
    }

    @Test
    public void isGettingConnectionToServer() throws IOException {
        HttpURLConnection conn = null;
        when(urlMock.openConnection()).thenReturn(conn);

        assertNull(ArticleRetriever.sendRequestToServer("testUrl"));
    }

    @Test
    public void testGetServerResponse() throws IOException {
        HttpURLConnection conn = mock(HttpURLConnection.class);
        when(conn.getInputStream()).thenReturn(new ByteArrayInputStream("Response read!".getBytes()));
        String expectedResponse = "Response read!";
        String actualResponse = ArticleRetriever.getServerResponse(conn);
        assertEquals(expectedResponse, actualResponse);
    }
}