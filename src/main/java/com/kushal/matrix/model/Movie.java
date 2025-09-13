package com.kushal.matrix.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLType;
import java.time.LocalDate;

@Entity
@Table(name="movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private int duration;
    private LocalDate releaseDate;
    @ManyToOne
    @JoinColumn(name="producer_id",nullable=false)
    private User producer;

}
