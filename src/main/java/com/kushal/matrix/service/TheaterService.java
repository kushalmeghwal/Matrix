package com.kushal.matrix.service;

import com.kushal.matrix.dtos.ScreenResponse;
import com.kushal.matrix.dtos.TheaterRequest;
import com.kushal.matrix.dtos.TheaterResponse;
import com.kushal.matrix.model.Screen;
import com.kushal.matrix.model.Theater;
import com.kushal.matrix.model.User;
import com.kushal.matrix.repository.TheaterRepository;
import com.kushal.matrix.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterService {
    private final TheaterRepository theaterRepository;

    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    public TheaterResponse addTheater(TheaterRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userDetails.getUser();

        List<Screen> screens=request.getScreens().stream()
                .map(sr->Screen.builder()
                        .name(sr.getName())
                        .capacity(sr.getCapacity())
                        .build())
                .collect(Collectors.toList());

        Theater theater = Theater.builder()
                .name(request.getName())
                .location(request.getLocation())
                .type(request.getType())
                .owner(currentUser)
                .screens(screens)
                .build();

        screens.forEach(sc -> sc.setTheater(theater));

        Theater saved=theaterRepository.save(theater);
        return mapToResponse(saved);
    }

    public List<TheaterResponse> getAllTheaters() {
        return theaterRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TheaterResponse getTheaterById(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Theater not found with id: " + id));
        return mapToResponse(theater);
    }

    private TheaterResponse mapToResponse(Theater theater) {
        return TheaterResponse.builder()
                .id(theater.getId())
                .name(theater.getName())
                .location(theater.getLocation())
                .type(theater.getType())
                .ownerEmail(theater.getOwner().getEmail())
                .screens(theater.getScreens().stream()
                        .map(sr -> ScreenResponse.builder()
                                .id(sr.getId())
                                .name(sr.getName())
                                .capacity(sr.getCapacity())
                                .theaterName(sr.getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

}
