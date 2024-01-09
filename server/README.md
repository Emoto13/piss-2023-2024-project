## Backend documentation

### Running the server
Make sure you have Java 19 or higher version and maven 3.9.6.

To run the project:
```
mvn clean compile exec:java
```

To build exec:
```
mvn package 
```

or

```
mvn clean compile assembly:single
```

To run tests:
```
 mvn clean test -Dnet.bytebuddy.experimental=true
```

Endpoint: `http://localhost:8000/articles?category=some-category\&country=some-country-iso2&keywords=key-words-separate-by-comma&page\=page-number`

News API documentation: https://newsapi.org/docs/endpoints/top-headlines


TODOs:
1. Validate page value
2. Write tests
3. 