package com.globetrotter.service;

import com.globetrotter.model.Destination;
import com.globetrotter.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    private final Random random = new Random();

    public Destination getRandomDestination() {
        List<Destination> destinations = destinationRepository.findRandomDestinations(1);
        return destinations.isEmpty() ? null : destinations.get(0);
    }

    public List<String> getRandomClues(Destination destination, int count) {
        List<String> allClues = destination.getClues();
        List<String> selectedClues = new ArrayList<>();

        // Return all clues if there are fewer than requested
        if (allClues.size() <= count) {
            return new ArrayList<>(allClues);
        }

        // Randomly select clues
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < allClues.size(); i++) {
            indices.add(i);
        }

        for (int i = 0; i < count; i++) {
            int randomIndex = random.nextInt(indices.size());
            int clueIndex = indices.remove(randomIndex);
            selectedClues.add(allClues.get(clueIndex));
        }

        return selectedClues;
    }

    public List<Destination> getRandomOptions(Destination correctDestination, int count) {
        List<Destination> allDestinations = destinationRepository.findAll();
        List<Destination> options = new ArrayList<>();

        // Add correct destination
        options.add(correctDestination);

        // Remove correct destination from the pool
        allDestinations.removeIf(d -> d.getId().equals(correctDestination.getId()));

        // Randomly select other destinations
        for (int i = 0; i < count - 1 && !allDestinations.isEmpty(); i++) {
            int randomIndex = random.nextInt(allDestinations.size());
            options.add(allDestinations.remove(randomIndex));
        }

        // Shuffle options
        java.util.Collections.shuffle(options);

        return options;
    }

    public Optional<Destination> findById(Long id) {
        return destinationRepository.findById(id);
    }
}