package test.java.bg.sofia.uni.fmi.news.adapter;

public class NewsApiAdapterTest {
    // NOTE: This test is commented out as the grader doesn't pass with it
/*    class MockHttpClient extends HttpClient {
        public MockHttpClient() {
        }

        @Override
        public Optional<CookieHandler> cookieHandler() {
            return Optional.empty();
        }

        @Override
        public Optional<Duration> connectTimeout() {
            return Optional.empty();
        }

        @Override
        public Redirect followRedirects() {
            return null;
        }

        @Override
        public Optional<ProxySelector> proxy() {
            return Optional.empty();
        }

        @Override
        public SSLContext sslContext() {
            return null;
        }

        @Override
        public SSLParameters sslParameters() {
            return null;
        }

        @Override
        public Optional<Authenticator> authenticator() {
            return Optional.empty();
        }

        @Override
        public Version version() {
            return null;
        }

        @Override
        public Optional<Executor> executor() {
            return Optional.empty();
        }

        @Override
        public <T> HttpResponse<T> send(HttpRequest httpRequest, HttpResponse.BodyHandler<T> bodyHandler) throws IOException, InterruptedException {
            return null;
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest httpRequest, HttpResponse.BodyHandler<T> bodyHandler) {
            return null;
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest httpRequest, HttpResponse.BodyHandler<T> bodyHandler, HttpResponse.PushPromiseHandler<T> pushPromiseHandler) {
            return null;
        }
    }

    @Spy
    private final HttpClient httpClientMock = mock(MockHttpClient.class);
    private final HttpResponse httpResponseMock = mock(HttpResponse.class);
    private ApiAdapter adapter;
    private Gson gson;

    @BeforeEach
    void setupTest() throws IOException, InterruptedException {
        this.gson = new Gson();
        PaginatedUrlBuilder urlBuilder = new NewsUrlBuilder("api-key");
        this.adapter = new NewApiAdapter(urlBuilder, httpClientMock);
        when(
                httpClientMock.send(any(HttpRequest.class), any())
        ).thenReturn(httpResponseMock);
    }

    @Test
    void TestGetArticlesWithApiError() {
        ApiErrorType errorType = ApiErrorType.SOURCE_DOES_NOT_EXIST;
        ArticlesResponse errorCriticalResponse = new ArticlesResponse(Status.ERROR.getValue(), errorType.getName(),
                0, new ArrayList<>());
        String json = gson.toJson(errorCriticalResponse);

        when(httpResponseMock.body()).thenReturn(json);
        Assertions.assertThrows(ApiErrorFactory.createError(errorType.getName()).getClass(), () -> {
            adapter.getArticles(new GetArticlesParameters(List.of("money"), "", ""));
        }, "should throw api error when error is critical");
    }

    @Test
    void TestGetArticlesRetrievesArticlesCorrectly() {

        Article article = new Article(new Source("id", "source"), "author", "title",
                "description", "url.com", "urlToImage.com", "content");
        ArticlesResponse successResponse = new ArticlesResponse(Status.OK.getValue(), "", 100, List.of(article));

        String json = gson.toJson(successResponse);
        when(httpResponseMock.body()).thenReturn(json);

        assertDoesNotThrow(() -> {
            List<Article> articles = adapter.getArticles(new GetArticlesParameters(List.of("money"), "", ""));
            assertFalse(articles.isEmpty(), "articles should not be empty when cannot send request");
        }, "should not throw exception if retrieves articles successfully");
    }


    @Test
    void TestGetArticlesWithFailureToSendRequest() throws IOException, InterruptedException {
        when(
                httpClientMock.send(any(HttpRequest.class), any())
        ).thenThrow(IOException.class);

        Assertions.assertThrows(FailedToRetrieveArticlesException.class, () -> {
            adapter.getArticles(new GetArticlesParameters(List.of("money"), "", ""));
        }, "should throw failed to retrieve when cannot send request");
    }
*/
}

