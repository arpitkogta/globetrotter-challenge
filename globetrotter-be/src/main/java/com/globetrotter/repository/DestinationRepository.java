package com.globetrotter.repository;

import com.globetrotter.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    @Query(value = "SELECT * FROM destination ORDER BY RAND() LIMIT :count", nativeQuery = true)
    List<Destination> findRandomDestinations(int count);
}
