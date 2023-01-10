package animals.language;

import org.junit.Test;

import static animals.language.Parser.*;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void parseYesNoYesSinglePunctuation() {
        assertEquals(BinaryChoice.YES, parseYesNo("Yes!"));
    }

    @Test
    public void parseYesNoYesMultiPunctuation() {
        assertEquals(BinaryChoice.UNDETERMINED, parseYesNo("Yes..."));
    }

    @Test
    public void parseYesNoNo() {
        assertEquals(BinaryChoice.NO, parseYesNo("Nope"));
    }

    @Test
    public void parseYesNoUndetermined() {
        assertEquals(BinaryChoice.UNDETERMINED, parseYesNo("Cat!"));
    }

    @Test
    public void parseYesNoNoPunctuation() {
        assertEquals(BinaryChoice.NO, parseYesNo("No way!"));
    }
}