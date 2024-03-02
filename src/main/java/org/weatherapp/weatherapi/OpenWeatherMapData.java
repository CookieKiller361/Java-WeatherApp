package org.weatherapp.weatherapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OpenWeatherMapData {


    public String getWeatherData(String urlString, String city, String countryCode, String limit, String apiKey) throws IOException {
        //get location Information's
        URL url = new URL(urlString + "q=" + city + "," + countryCode + "&limit=" + limit + "&appid=" + apiKey);
        HttpURLConnection connection = connect(url);
        String output = apiDataToString(connection, url);
        //deletes the Array Syntax of the result, cause readTree can't handle that
        output = output.substring(1, output.length() - 1);
        // filter the JSON Data for lat and lon (coordination's)
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(output.toString());
        String lat = jsonNode.get("lat").asText();
        String lon = jsonNode.get("lon").asText();
        //get Weather Information's
        url = new URL("https://api.openweathermap.org/data/2.5/weather?" + "lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric");
        connection = connect(url);
        output = apiDataToString(connection, url);
        return output;
    }

    /**
     * this methode is only for now and will be changed in future Versions, APIKey would be requested by user input and saved to a config-File.
     */

    private String getApiKey(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        StringBuilder output = new StringBuilder();
        while (scanner.hasNextLine()) {
            output.append(scanner.nextLine());
        }
        return output.toString();
    }

    private HttpURLConnection connect(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        return con;
    }

    public String getCLIWeather(String apiOutputData) throws JsonProcessingException {
        StringBuilder output = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        //this step is very important because the parser can only use one json-Object
        apiOutputData = apiOutputData.replace("}{", ",");
        JsonNode jsonNode = objectMapper.readTree(apiOutputData);
        String name = jsonNode.get("name").asText();
        String temp = jsonNode.get("main").get("temp").asText();
        String feels_like = jsonNode.get("main").get("feels_like").asText();
        String weatherStatus = jsonNode.get("weather").get(0).get("description").asText();
        output.append("City-Name: " + name + "\n");
        output.append("Temp: " + temp + "\n");
        output.append("Feels like : " + feels_like + "\n");
        output.append("Weather status: " + weatherStatus + "\n");
        return output.toString();
    }

    private String apiDataToString(HttpURLConnection connection, URL url) throws IOException {
        StringBuilder output = new StringBuilder();
        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("HttpResponseCode: " + connection.getResponseCode());
        } else {
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNextLine()) {
                output.append(scanner.nextLine());
            }
        }
        return output.toString();
    }

    //this main is only for testing
    public static void main(String[] args) throws IOException {
        OpenWeatherMapData Test = new OpenWeatherMapData();
        String result = Test.getWeatherData("http://api.openweathermap.org/geo/1.0/direct?", "Roetgen", "DE", "1", Test.getApiKey("src/main/resources/other/apiKeyStorage.txt"));
        String cliWeatherInformation = Test.getCLIWeather(result);
        System.out.println(cliWeatherInformation);

    }

}



