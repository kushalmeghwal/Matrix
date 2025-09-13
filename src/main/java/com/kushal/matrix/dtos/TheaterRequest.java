package com.kushal.matrix.dtos;

import com.kushal.matrix.model.TheaterType;
import lombok.Data;

import java.util.List;

@Data
public class TheaterRequest {
    private String name;
    private String location;
    private TheaterType type;
    private List<ScreenRequest> screens;
}
