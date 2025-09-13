package com.kushal.matrix.controller;

import com.kushal.matrix.dtos.ScreenRequest;
import com.kushal.matrix.dtos.ScreenResponse;
import com.kushal.matrix.service.ScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screens")
public class ScreenController {
    private final ScreenService screenservice;
    public ScreenController(ScreenService screenservice) {
        this.screenservice = screenservice;
    }

    @PostMapping("/addScreen")
    public ResponseEntity<ScreenResponse> addScreen(@RequestBody ScreenRequest request) {
        return ResponseEntity.ok(screenservice.addScreen(request));
    }

    @GetMapping
    public ResponseEntity<List<ScreenResponse>> getAllScreens() {
        return ResponseEntity.ok(screenservice.getAllScreens());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreenResponse> getScreenById(@PathVariable Long id) {
        return ResponseEntity.ok(screenservice.getScreenById(id));
    }
}
