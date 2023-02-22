package animals.helper;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Parser {
    public static BinaryChoice parseBinaryChoiceAnswer(String answer) {
        ResourceBundle messageResource = ResourceBundle.getBundle("messages");
        String normalizedAnswer = answer.endsWith(".") || answer.endsWith("!")
                ? answer.substring(0, answer.length() - 1).toLowerCase() : answer.toLowerCase();

        if (Arrays.asList(messageResource.getString("yes").split("\\f"))
                .contains(normalizedAnswer.toLowerCase()))
            return BinaryChoice.YES;
        else if (Arrays.asList(messageResource.getString("no").split("\\f"))
                .contains(normalizedAnswer.toLowerCase()))
            return BinaryChoice.NO;
        else
            return BinaryChoice.UNDETERMINED;
    }

    public static String parsePeriodOfDay() {
        ResourceBundle messageResource = ResourceBundle.getBundle("messages");
        LocalTime localTime = LocalTime.now();

        if (localTime.isBefore(LocalTime.parse(messageResource.getString("early.time.before"))))
            return messageResource.getString("greeting.early");
        else if (localTime.isBefore(LocalTime.parse(messageResource.getString("morning.time.before"))))
            return messageResource.getString("greeting.morning");
        else if (localTime.isBefore(LocalTime.parse(messageResource.getString("afternoon.time.before"))))
            return messageResource.getString("greeting.afternoon");
        else if (localTime.isBefore(LocalTime.parse(messageResource.getString("night.time.before"))))
            return messageResource.getString("greeting.evening");
        else
            return messageResource.getString("greeting.night");
    }
}
