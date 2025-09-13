package com.kushal.matrix.service;

import com.kushal.matrix.dtos.GroupedScheduleResponse;
import com.kushal.matrix.model.*;
import com.kushal.matrix.repository.MovieRepository;
import com.kushal.matrix.repository.ScheduleRepository;
import com.kushal.matrix.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final ScheduleRepository scheduleRepository;

    /**
     * Generate schedules for new movies/screens while keeping old schedules intact.
     */
    @Transactional
    private List<Schedule> updateSchedulesIfNeeded() {
        List<Movie> movies = movieRepository.findAll();
        List<Screen> screens = screenRepository.findAll();
        List<Schedule> existingSchedules = scheduleRepository.findAll();

        if (movies.isEmpty() || screens.isEmpty()) {
            return existingSchedules;
        }

        // Track already scheduled (movie, screen, date)
        Set<String> existingKeys = existingSchedules.stream()
                .map(s -> s.getMovie().getId() + "-" + s.getScreen().getId() + "-" + s.getDate())
                .collect(Collectors.toSet());

        List<Schedule> newSchedules = new ArrayList<>();
        LocalDate startDate = LocalDate.now()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

        for (int day = 0; day < 3; day++) {
            LocalDate currentDate = startDate.plusDays(day);

            for (Screen screen : screens) {
                LocalTime currentTime = LocalTime.of(10, 0);
                int showsCount = 0;

                for (Movie movie : movies) {
                    if (showsCount >= 3) break;

                    String key = movie.getId() + "-" + screen.getId() + "-" + currentDate;

                    // Skip if this combination is already scheduled
                    if (existingKeys.contains(key)) {
                        continue;
                    }

                    Schedule schedule = Schedule.builder()
                            .screen(screen)
                            .movie(movie)
                            .date(currentDate)
                            .showTime(currentTime)
                            .build();

                    newSchedules.add(schedule);
                    existingKeys.add(key);

                    // Move showtime forward
                    currentTime = currentTime.plusMinutes(movie.getDuration()).plusMinutes(30);
                    showsCount++;
                }
            }
        }

        if (!newSchedules.isEmpty()) {
            scheduleRepository.saveAll(newSchedules);
            existingSchedules.addAll(newSchedules);
        }

        return existingSchedules;
    }

    /**
     * Public APIs
     */
    public List<GroupedScheduleResponse> getOrGenerateScheduleForUser() {
        return groupSchedules(updateSchedulesIfNeeded());
    }

    public List<GroupedScheduleResponse> getMyScheduleForProducer(User producer) {
        if (producer.getRole() != Role.PRODUCER) {
            throw new RuntimeException("Only producers can access this");
        }
        updateSchedulesIfNeeded();
        return groupSchedules(scheduleRepository.findByMovieProducerId(producer.getId()));
    }

    public List<GroupedScheduleResponse> getMyScheduleForOwner(User owner) {
        if (owner.getRole() != Role.THEATER_OWNER) {
            throw new RuntimeException("Only owners can access this");
        }
        updateSchedulesIfNeeded();
        return groupSchedules(scheduleRepository.findByScreenTheaterOwnerId(owner.getId()));
    }

    /**
     * Convert schedules into GroupedScheduleResponse DTO.
     */
    private List<GroupedScheduleResponse> groupSchedules(List<Schedule> schedules) {
        return schedules.stream()
                .collect(Collectors.groupingBy(Schedule::getDate))
                .entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Schedule> daySchedules = entry.getValue();

                    Map<Long, List<Schedule>> theaterGroups =
                            daySchedules.stream().collect(Collectors.groupingBy(s -> s.getScreen().getTheater().getId()));

                    List<GroupedScheduleResponse.TheaterScheduleDTO> theaters = theaterGroups.entrySet().stream()
                            .map(theaterEntry -> {
                                Long theaterId = theaterEntry.getKey();
                                String theaterName = theaterEntry.getValue().get(0).getScreen().getTheater().getName();

                                Map<Long, List<Schedule>> screenGroups =
                                        theaterEntry.getValue().stream().collect(Collectors.groupingBy(s -> s.getScreen().getId()));

                                List<GroupedScheduleResponse.ScreenScheduleDTO> screens = screenGroups.entrySet().stream()
                                        .map(screenEntry -> {
                                            Long screenId = screenEntry.getKey();
                                            String screenName = screenEntry.getValue().get(0).getScreen().getName();

                                            List<GroupedScheduleResponse.ShowDTO> shows = screenEntry.getValue().stream()
                                                    .map(s -> GroupedScheduleResponse.ShowDTO.builder()
                                                            .showTime(s.getShowTime())
                                                            .movieId(s.getMovie().getId())
                                                            .movieTitle(s.getMovie().getTitle())
                                                            .producerName(
                                                                    s.getMovie().getProducer() != null
                                                                            ? s.getMovie().getProducer().getName()
                                                                            : "Unknown"
                                                            )
                                                            .build())
                                                    .toList();

                                            return GroupedScheduleResponse.ScreenScheduleDTO.builder()
                                                    .screenId(screenId)
                                                    .screenName(screenName)
                                                    .shows(shows)
                                                    .build();
                                        })
                                        .toList();

                                return GroupedScheduleResponse.TheaterScheduleDTO.builder()
                                        .theaterId(theaterId)
                                        .theaterName(theaterName)
                                        .screens(screens)
                                        .build();
                            })
                            .toList();

                    return GroupedScheduleResponse.builder()
                            .date(date)
                            .dayOfWeek(DayOfWeek.from(date))
                            .theaters(theaters)
                            .build();
                })
                .sorted(Comparator.comparing(GroupedScheduleResponse::getDate))
                .toList();
    }
}
