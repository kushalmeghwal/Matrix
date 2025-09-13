package com.kushal.matrix.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class GroupedScheduleResponse {

    private LocalDate date;
    private DayOfWeek dayOfWeek;
    private List<TheaterScheduleDTO> theaters;

    @Data
    @AllArgsConstructor
    @Builder
    public static class TheaterScheduleDTO {
        private Long theaterId;
        private String theaterName;
        private List<ScreenScheduleDTO> screens;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class ScreenScheduleDTO {
        private Long screenId;
        private String screenName;
        private List<ShowDTO> shows;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class ShowDTO {
        private LocalTime showTime;
        private Long movieId;
        private String movieTitle;
        private String producerName;
    }
}
