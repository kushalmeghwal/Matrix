package com.kushal.matrix.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="screens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Screen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;
    @ManyToOne
    @JoinColumn(name="theater_id",nullable=false)
    @JsonIgnoreProperties({"screens"})
    private Theater theater;
    @OneToMany(mappedBy = "screen",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"screen"})
    private List<Schedule> schedules;
}
