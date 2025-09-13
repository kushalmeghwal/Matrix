package com.kushal.matrix.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ScreenRequest {
    private String name;
    private int capacity;
    private Long theaterId;
}
