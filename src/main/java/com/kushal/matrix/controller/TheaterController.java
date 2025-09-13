package com.kushal.matrix.controller;

import com.kushal.matrix.dtos.TheaterRequest;
import com.kushal.matrix.dtos.TheaterResponse;
import com.kushal.matrix.service.TheaterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theaters")
public class TheaterController {

    private final TheaterService theaterService;

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
    }

    @PostMapping("/addTheater")
    public ResponseEntity<TheaterResponse> addTheater(@RequestBody TheaterRequest request) {
        return ResponseEntity.ok(theaterService.addTheater(request));
    }

    @GetMapping
    public ResponseEntity<List<TheaterResponse>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterResponse> getTheaterById(@PathVariable Long id) {
        return ResponseEntity.ok(theaterService.getTheaterById(id));
    }
}
