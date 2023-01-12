package animals.language;

public class Parser {
    public static BinaryChoice parseBinaryChoiceAnswer(String answer) {
        String normalizedAnswer = answer.endsWith(".") || answer.endsWith("!")
                ? answer.substring(0, answer.length() - 1).toLowerCase() : answer.toLowerCase();

        if (Literal.YES.contains(normalizedAnswer))
            return BinaryChoice.YES;
        else if (Literal.NO.contains(normalizedAnswer))
            return BinaryChoice.NO;
        else
            return BinaryChoice.UNDETERMINED;
    }
}
