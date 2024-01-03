## Frontend documentation

### Starting the application
Make sure you have Java 19 or higher version and maven 3.9.6

To run the project:
```
mvn clean compile exec:java
```

- The project has some helpful tooltips on hover, be sure to check them out.
- Also, most articles do not utilize the keywords parameter, specifying may result in lack of results shown.
- Note that the application is running even if there are no visual clues of it.
  - When the **Status** message says "Loading..." the program is fetching information
  - When the **Status** message says "Ready." the program is done with the loading and is ready to send a new request