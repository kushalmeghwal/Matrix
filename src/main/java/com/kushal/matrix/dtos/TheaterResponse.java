package com.kushal.matrix.dtos;

import com.kushal.matrix.model.TheaterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TheaterResponse {
    private Long id;
    private String name;
    private String location;
    private TheaterType type;
    private String ownerEmail;
    private List<ScreenResponse> screens;
}
