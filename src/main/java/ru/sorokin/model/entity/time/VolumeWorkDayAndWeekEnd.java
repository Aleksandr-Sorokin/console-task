package ru.sorokin.model.entity.time;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class VolumeWorkDayAndWeekEnd {
    private int workDay;
    private List<LocalDate> weekEnd;
}
