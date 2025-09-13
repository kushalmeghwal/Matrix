package com.kushal.matrix.controller;

import com.kushal.matrix.dtos.GroupedScheduleResponse;
import com.kushal.matrix.model.User;
import com.kushal.matrix.security.CustomUserDetails;
import com.kushal.matrix.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * Get complete schedule for all theaters/screens/movies.
     * Accessible to all roles (user, producer, owner).
     * - If no schedule exists yet, it will generate one automatically.
     */
    @GetMapping
    public ResponseEntity<List<GroupedScheduleResponse>> getScheduleForUser() {
        return ResponseEntity.ok(scheduleService.getOrGenerateScheduleForUser());
    }

    /**
     * Producer: view schedule for only his movies.
     */
    @GetMapping("/producer")
    @PreAuthorize("hasRole('PRODUCER')")
    public ResponseEntity<List<GroupedScheduleResponse>> getMyScheduleForProducer(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(scheduleService.getMyScheduleForProducer(userDetails.getUser()));
    }

    /**
     * Owner: view schedule for only his theaters.
     */
    @GetMapping("/owner")
    @PreAuthorize("hasRole('THEATER_OWNER')")
    public ResponseEntity<List<GroupedScheduleResponse>> getMyScheduleForOwner(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(scheduleService.getMyScheduleForOwner(userDetails.getUser()));
    }
}
