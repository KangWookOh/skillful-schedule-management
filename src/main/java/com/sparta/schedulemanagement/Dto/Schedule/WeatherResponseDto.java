package com.sparta.schedulemanagement.Dto.Schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherResponseDto {

    @JsonProperty("date")
    private String date;

    @JsonProperty("weather")
    private String weather;

    public WeatherResponseDto() {

    }
}
