package com.kushal.matrix.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="theaters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String location;
    @Enumerated(EnumType.STRING)
    private TheaterType type;
    @ManyToOne
    @JoinColumn(name="owner_id",nullable=false)
    @JsonIgnoreProperties({"theaters"})
    private User owner;
    @OneToMany(mappedBy = "theater",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnoreProperties({"theater"})
    private List<Screen> screens;
}
