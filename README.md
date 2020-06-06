
# Weather proxy


Available on Docker hub `https://hub.docker.com/repository/docker/larsm/weather-proxy`

-------------------
# Instructions

**Before running the application please make sure a valid OpenWeatherMap API-key is added to the `application.yml` file.**
If the application is running in a docker container you can set the API-key using the environment variable:  `CONFIGURABLES.OPENWEATHERMAP.API-KEY`.
For example: `docker run -p 7878:7878 -e CONFIGURABLES.OPENWEATHERMAP.API-KEY=[your API-key] larsm/weather-proxy:latest`.

##### Swagger
To use this application, one may use the built in Swagger-ui, which is available at: `http://localhost:7878/swagger-ui.html`.

##### H2 Console
The H2 console is located at: `http://localhost:7878/h2`
##### Test Report
To gain insights in code coverage please find the JaCoCo code coverage visualisation after running `mvn verify` in `target\site\jacoco\index.html`
##### Configuration
Configuration and settings can be found in the `application.yml` file located in `src\main\resources`.

-------------------

# Considerations
During the development I have had to make some choices and considerations, there are things that I would have (liked) to do differently but chose not to. This because the e-mail with the assignment explains this test is to see how well I can follow assignments. However in this segment I'd like to discuss some of the noteworthy considerations and suggestions that came up during development.

- DELETE: `/cities/{cityName}` - The assignment specifically mentions the cityName in delete may not be null, however it does not specify if this is the String value "null"/"NULL" or just null as in nothing. I chose to implement it in a way that allowsw the string values "null" and "NULL" but not null as in nothing. This could easily be changed by adding an assertion in the controller which checks for the string values "null" and "NULL".
- Database design: the assignment describes the software "must do CRUD operations on an H2 in-memory database table", I complied and wrote the software using a single database table, personally for future expansions I would consider splitting the weather and the cities into seperate tables.
- Right now as per the specification all cities are queried on the OpenWeatherMap api using the `q="cityName"` parameter of the querystring. However this could sometimes lead to misunderstandings. For example when searching for "Utrecht" OpenWeatherMap will return the Province Utrecht, instead of its capital that is also named Utrecht. 
- This "issue" is related to the previous issue. When storing the name of a city in the database, I had to choose wether I wanted to store the name the user entered, or the name returned by OpenWeatherMaps. These could be different names, for example when you search for "Barendrecht", OpenWeatherMaps would return "Gemeente Barendrecht" as its name. I chose to use the name from OpenWeatherMaps as it most accurately indicates the location of the temperatures. But this is not without implications, because if the user wants to search the city using "Barendrecht" after storing it in the database, the user wouldn't find it. The user would have to search for "Gemeente Barendrecht". This could be confusing. There's two ways to solve this. 1. Store the name of the city the user entered to add the city to the database (or maybe both). 2. Change the search function to search for city names containing or similar to the ones entered by the user. This comes with the risk of retrieving wrong data or having multiple matches when entering the name of a city.
- Personally I would prefer to store all the data from the OpenWeatherMaps api, and query it in various ways, instead of just taking some data and storing it. It's 2020 and storage is relatively cheap in general.
- Lombok: Generally I tend not to use Lombok, but in this case I opted to use it, for simplicity and to show I do know how to use Lombok. There are serveral reasons why one would want to stay away from Lombok in various situations.

-------------------
# Assignment
Create a Spring Boot application that acts as a proxy for an online weather service (`https://openweathermap.org/api`). The application must do the following:

- Using Hibernate, it must do CRUD operations on an H2 in-memory database table
- The following fields from the API's JSON response must be must be stored:

  - id
  - name
  - temp_min
  - temp_max
  - sunrise

- The app must read data using the online API GET endpoint : `api.openweathermap.org/data/2.5/weather?q={city name}&appid={your api key}`
  - Supply the city name and the subscribed API key (see below)
- The app must expose a RESTful service (`/weatherproxy`) with the following endpoints:
  - GET: `/cities/{city name}` : Retrieve the city name from the H2 table and return all the fields in the response. Return a Http status 404 if the record is not found in the table
  - GET: `/cities` : Retrieve all records and return them in the endpoint response.
  - DELETE: `/cities/{city name}` : Delete the record from the table. `{city name}` is may not be null. If the city name doesn't exist, just return as if it did
  - POST: `/cities/{city name}` : Call the online weather API and store the result in the H2 table. It is ok to overwrite an existing record if one exists already

- Create at least one unit test that mocks the call to the online service
- call the Spring Boot endpoint with any tool you like, eg. postman, curl, etc
- For bonus points, start the application in a Docker container.
