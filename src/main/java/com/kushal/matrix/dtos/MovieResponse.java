package com.kushal.matrix.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MovieResponse {
    private Long id;
    private String title;
    private int duration;
    private LocalDate releaseDate;
    private String producerName;
}
