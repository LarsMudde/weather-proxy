
# Weather proxy

-------------------
# Instructions
To use this application one may use the built in Swagger-ui, which is available at: `http://localhost:7878/swagger-ui.html`.
The H2 console is located at: `http://localhost:7878/h2`

Configuration and settings can be found in the `application.yml` file located in `src\main\resources`.

**Before running the application please make sure a valid OpenWeatherMap API-key is added to the `application.yml` file.**
If the application is running in a docker container you can set the API-key using the environment variable:  `CONFIGURABLES.OPENWEATHERMAP.API-KEY`.
For example: `docker run -p 7878:7878 -e CONFIGURABLES.OPENWEATHERMAP.API-KEY=[your API-key] larsm/openweatherproxy:latest`.

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
