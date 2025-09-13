package com.kushal.matrix.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="movie_id",nullable=false)
    @JsonIgnoreProperties({"schedules"})
    private Movie movie;
    @ManyToOne
    @JoinColumn(name="screen_id",nullable=false)
    @JsonIgnoreProperties({"schedules"})
    private Screen screen;
    private LocalTime showTime;
    private LocalDate date;
}
