package com.kushal.matrix.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MovieRequest {
    private String title;
    private int duration;
    private LocalDate releaseDate;
    private Long producerId;
}
