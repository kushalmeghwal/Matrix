package com.kushal.matrix.service;


import com.kushal.matrix.dtos.ScreenRequest;
import com.kushal.matrix.dtos.ScreenResponse;
import com.kushal.matrix.model.Screen;
import com.kushal.matrix.model.Theater;
import com.kushal.matrix.repository.ScreenRepository;
import com.kushal.matrix.repository.TheaterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final TheaterRepository theaterRepository;

    public ScreenService(ScreenRepository screenRepository, TheaterRepository theaterRepository) {
        this.screenRepository = screenRepository;
        this.theaterRepository = theaterRepository;
    }

    public ScreenResponse addScreen(ScreenRequest request) {
        Theater theater = theaterRepository.findById(request.getTheaterId())
                .orElseThrow(() -> new RuntimeException("Theater not found"));

        Screen screen = new Screen();
        screen.setName(request.getName());
        screen.setCapacity(request.getCapacity());
        screen.setTheater(theater);

        Screen saved = screenRepository.save(screen);

        return new ScreenResponse(saved.getId(), saved.getName(), saved.getCapacity(), saved.getTheater().getName());
    }

    public List<ScreenResponse> getAllScreens() {
        return screenRepository.findAll().stream()
                .map(screen -> new ScreenResponse(
                        screen.getId(),
                        screen.getName(),
                        screen.getCapacity(),
                        screen.getTheater().getName()
                ))
                .collect(Collectors.toList());
    }

    public ScreenResponse getScreenById(Long id) {
        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        return new ScreenResponse(
                screen.getId(),
                screen.getName(),
                screen.getCapacity(),
                screen.getTheater().getName()
        );
    }
}
