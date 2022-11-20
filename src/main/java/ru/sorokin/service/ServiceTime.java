package ru.sorokin.service;

import org.springframework.stereotype.Service;
import ru.sorokin.entity.time.VolumeWorkDayAndWeekEnd;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class ServiceTime {
    public VolumeWorkDayAndWeekEnd getWeekEndDay(LocalDate startDate, LocalDate endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar start = Calendar.getInstance();
            start.setTime(dateFormat.parse(String.valueOf(startDate)));
            Calendar end = Calendar.getInstance();
            end.setTime(dateFormat.parse(String.valueOf(endDate)));
            int workingDays = 0;
            List<LocalDate> weekEndDates = new ArrayList<>();
            while (!start.after(end)) {
                int day = start.get(Calendar.DAY_OF_WEEK);
                day = day + 2;
                if (day > 7) {
                    day = day - 7;
                }
                if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
                    workingDays++;
                } else {
                    weekEndDates.add(start.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                }
                start.add(Calendar.DATE, 1);
            }
            VolumeWorkDayAndWeekEnd weekEnd = new VolumeWorkDayAndWeekEnd();
            weekEnd.setWorkDay(workingDays);
            weekEnd.setWeekEnd(weekEndDates);
            return weekEnd;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
