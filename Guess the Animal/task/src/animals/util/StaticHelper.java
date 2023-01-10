package animals.util;

import java.time.LocalTime;

public class StaticHelper {
    public static DayPeriod getPeriodOfDay() {
        LocalTime localTime = LocalTime.now();

        if (localTime.isBefore(LocalTime.of(5, 0)))
            return DayPeriod.DAWN;
        else if (localTime.isBefore(LocalTime.of(12, 0)))
            return DayPeriod.MORNING;
        else if (localTime.isBefore(LocalTime.of(6, 0)))
            return DayPeriod.AFTERNOON;
        else if (localTime.isBefore(LocalTime.of(22, 0)))
            return DayPeriod.EVENING;
        else
            return DayPeriod.NIGHT;
    }
}
