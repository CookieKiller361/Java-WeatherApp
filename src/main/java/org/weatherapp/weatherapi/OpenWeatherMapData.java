package org.weatherapp.weatherapi;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenWeatherMapData {


    public String getWeatherData(String urlString, String city, String countryCode, String limit, String apiKey) throws IOException {
        URL url = new URL(urlString + "q=" + city + "," + countryCode + "&limit=" + limit + "&appid=" + apiKey);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        StringBuilder output = new StringBuilder();
        if (con.getResponseCode() != 200) {
            throw new RuntimeException("HttpResponseCode: " + con.getResponseCode());
        } else {
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNextLine()) {
                output.append(scanner.nextLine());
            }
        }
        output.deleteCharAt(0);
        output.deleteCharAt(output.length() - 1);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(output.toString());
        String lat = jsonNode.get("lat").asText();
        String lon = jsonNode.get("lon").asText();
        url = new URL("https://api.openweathermap.org/data/2.5/weather?" + "lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric");
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();
        if (con.getResponseCode() != 200) {
            throw new RuntimeException("HttpResponseCode: " + con.getResponseCode());
        } else {
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNextLine()) {
                output.append(scanner.nextLine());
            }
        }
        //todo transform the data into Json-format and extract the location data and put the into actually weatherData API
        return output.toString();
    }

    private String getApiKey(String path) {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        StringBuilder output = new StringBuilder();
        while (scanner.hasNextLine()) {
            output.append(scanner.nextLine());
        }
        return output.toString();
    }

    //this main is only for testing
    public static void main(String[] args) throws IOException {
        OpenWeatherMapData Test = new OpenWeatherMapData();
        //just for testing
        String result = Test.getWeatherData("http://api.openweathermap.org/geo/1.0/direct?",
                "Roetgen",
                "DE",
                "1",
                Test.getApiKey("src/main/resources/other/apiKeyStorage.txt"));
        System.out.println(result);
    }

}



