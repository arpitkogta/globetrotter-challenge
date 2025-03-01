package com.globetrotter.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GooglePlacesService {

    @Value("${google.places.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getImageUrl(String cityName) {
        try {
            // 1. Call Google Places API to search for city
            String searchUrl = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json" +
                    "?input=" + cityName +
                    "&inputtype=textquery&fields=photos" +
                    "&key=" + apiKey;

            String searchResponse = restTemplate.getForObject(searchUrl, String.class);
            JSONObject jsonResponse = new JSONObject(searchResponse);

            // 2. Extract "photo_reference"
            if (jsonResponse.has("candidates") && jsonResponse.getJSONArray("candidates").length() > 0) {
                JSONObject photoData = jsonResponse.getJSONArray("candidates").getJSONObject(0);
                if (photoData.has("photos")) {
                    String photoRef = photoData.getJSONArray("photos").getJSONObject(0).getString("photo_reference");

                    // 3. Construct Photo URL
                    return "https://maps.googleapis.com/maps/api/place/photo" +
                            "?maxwidth=400&photoreference=" + photoRef +
                            "&key=" + apiKey;
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching image for city: " + cityName + " - " + e.getMessage());
        }
        return null; // Fallback in case of error
    }
}
