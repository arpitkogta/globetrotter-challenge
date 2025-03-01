package com.globetrotter.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globetrotter.model.Destination;
import com.globetrotter.repository.DestinationRepository;
import com.globetrotter.service.GooglePlacesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DestinationRepository destinationRepository;
    private final GooglePlacesService googlePlacesService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DataInitializer(DestinationRepository destinationRepository, GooglePlacesService googlePlacesService) {
        this.destinationRepository = destinationRepository;
        this.googlePlacesService = googlePlacesService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Load JSON file
        File jsonFile = new ClassPathResource("data.json").getFile();
        List<Map<String, Object>> destinations = readJsonFile(jsonFile);

        for (Map<String, Object> entry : destinations) {
            String city = (String) entry.get("city");
            String country = (String) entry.get("country");
            List<String> clues = (List<String>) entry.get("clues");
            List<String> funFacts = (List<String>) entry.get("fun_fact");
            List<String> trivia = (List<String>) entry.get("trivia");

            String imageUrl = googlePlacesService.getImageUrl(city);

            Destination destination = new Destination();
            destination.setName(city);
            destination.setCountry(country);
            destination.setClues(clues);
            destination.setFunFacts(funFacts);
            destination.setTrivia(trivia);
            destination.setImageUrl(imageUrl);

            destinationRepository.save(destination);
            System.out.println("Saved: " + city + " -> " + imageUrl);
        }
    }

    private List<Map<String, Object>> readJsonFile(File file) throws IOException {
        return objectMapper.readValue(file, new TypeReference<>() {});
    }
}
