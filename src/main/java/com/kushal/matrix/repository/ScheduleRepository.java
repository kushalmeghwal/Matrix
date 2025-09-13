package com.kushal.matrix.repository;

import com.kushal.matrix.model.Schedule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @EntityGraph(attributePaths = {
            "movie",
            "movie.producer",
            "screen",
            "screen.theater",
            "screen.theater.owner"
    })
    List<Schedule> findByMovieProducerId(Long producerId);

    @EntityGraph(attributePaths = {
            "movie",
            "movie.producer",
            "screen",
            "screen.theater",
            "screen.theater.owner"
    })
    List<Schedule> findByScreenTheaterOwnerId(Long ownerId);

    @EntityGraph(attributePaths = {
            "movie",
            "movie.producer",
            "screen",
            "screen.theater",
            "screen.theater.owner"
    })
    List<Schedule> findAll();

}
