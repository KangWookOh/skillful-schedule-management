package com.sparta.schedulemanagement.Dto.Schedule;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class WeatherResponseDto {


    @JsonProperty("date")
    private String date;

    @JsonProperty("weather")
    private String weather;


}
