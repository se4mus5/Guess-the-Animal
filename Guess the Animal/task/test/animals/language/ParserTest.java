package animals.language;

import org.junit.Test;

import static animals.language.Parser.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void parseYesNoYesSinglePunctuation() {
        assertEquals(BinaryChoice.YES, parseBinaryChoiceAnswer("Yes!"));
    }

    @Test
    public void parseYesNoYesMultiPunctuation() {
        assertEquals(BinaryChoice.UNDETERMINED, parseBinaryChoiceAnswer("Yes..."));
    }

    @Test
    public void parseYesNoNo() {
        assertEquals(BinaryChoice.NO, parseBinaryChoiceAnswer("Nope"));
    }

    @Test
    public void parseYesNoUndetermined() {
        assertEquals(BinaryChoice.UNDETERMINED, parseBinaryChoiceAnswer("Cat!"));
    }

    @Test
    public void parseYesNoNoPunctuation() {
        assertEquals(BinaryChoice.NO, parseBinaryChoiceAnswer("No way!"));
    }
}