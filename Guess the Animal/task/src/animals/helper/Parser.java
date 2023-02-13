package animals.helper;

import animals.language.BinaryChoice;
import animals.language.DayPeriod;
import animals.language.Literals;

import java.time.LocalTime;

public class Parser {
    public static BinaryChoice parseBinaryChoiceAnswer(String answer) {
        String normalizedAnswer = answer.endsWith(".") || answer.endsWith("!")
                ? answer.substring(0, answer.length() - 1).toLowerCase() : answer.toLowerCase();

        if (Literals.YES.contains(normalizedAnswer.toLowerCase()))
            return BinaryChoice.YES;
        else if (Literals.NO.contains(normalizedAnswer.toLowerCase()))
            return BinaryChoice.NO;
        else
            return BinaryChoice.UNDETERMINED;
    }

    public static DayPeriod parsePeriodOfDay() {
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
