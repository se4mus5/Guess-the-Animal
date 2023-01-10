package animals.language;

import animals.util.DayPeriod;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Literal {
    public static final Map<DayPeriod, String> GREETINGS = Map.of(
            DayPeriod.DAWN, "Hi, Early Bird!",
            DayPeriod.MORNING, "Good morning!",
            DayPeriod.AFTERNOON, "Good afternoon!",
            DayPeriod.EVENING, "Good evening!",
            DayPeriod.NIGHT, "Hi, Night Owl!"
    );

    public static final List<String> GOODBYE = List.of(
      "Bye!", "Goodbye!", "Sayonara!", "Hasta la vista!", "Ciao!", "Have a nice day!",
      "So long!", "Arrivederci!", "Adieu!", "Do svidanya!", "Farewell!", "See you soon!", "See you later!"
    );

    public static final Set<String> YES = Set.of(
            "y", "yes", "yeah", "yep", "sure", "right", "affirmative", "correct", "indeed",
            "you bet", "exactly", "you said it"
    );

    public static final Set<String> NO = Set.of(
            "n", "no", "no way", "nah", "nope", "negative", "I don't think so", "yeah no"
    );

    public static final List<String> CLARIFICATION = List.of(
            "I'm not sure I caught you: was it yes or no?",
            "Funny, I still don't understand, is it yes or no?",
            "Oh, it's too complicated for me: just tell me yes or no.",
            "Could you please simply say yes or no?",
            "Oh, no, don't try to confuse me: say yes or no."
    );
}
