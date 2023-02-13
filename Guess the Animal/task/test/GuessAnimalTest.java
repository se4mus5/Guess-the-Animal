import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.io.IOException;

public class GuessAnimalTest extends StageTest<String> {
    final String[] script = new String[]{
            "the-first-question",
            "positive-answers",
            "negative-answers",
            "unclear-answers",
            "guessing-game"
    };

    @DynamicTest(data = "script")
    CheckResult runScripts(final String script) throws IOException {
        return new Scenario(script).check();
    }

}