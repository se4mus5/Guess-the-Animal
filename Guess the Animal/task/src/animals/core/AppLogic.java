package animals.core;

import animals.entity.Animal;
import animals.language.BinaryChoice;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

import static animals.language.Parser.parseBinaryChoiceAnswer;

public class AppLogic {
    private final TextUserInterface textUserInterface;

    public AppLogic() {
        this.textUserInterface = new TextUserInterface();
    }

    public void start() {
        // set up logging
        Logger logger = Logger.getLogger("Guess The Animal Application");
        File parentDir = new File(System.getProperty("user.dir") + "/log");
        parentDir.mkdirs();
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(System.getProperty("user.dir") + "/log/GuessTheAnimalApp.log", true);
        } catch (IOException e) {
            System.out.println("Unable to create application logfile:");
            e.printStackTrace();
        }
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);

        logger.log(Level.INFO, "App started.");
        textUserInterface.greet();

        textUserInterface.animalEntryPrompt(1);
        String firstAnimalName = textUserInterface.getInput();
        textUserInterface.animalEntryPrompt(2);
        String secondAnimalName = textUserInterface.getInput();
        logger.log(Level.INFO, "Animal data entered.");

        Animal firstAnimal = new Animal(firstAnimalName);
        Animal secondAnimal = new Animal(secondAnimalName);
        logger.log(Level.INFO, "Animal objects created.");

        textUserInterface.factPrompt(firstAnimal, secondAnimal);
        String distinguishingFact = textUserInterface.getInput();

        while (!distinguishingFact.matches("^(it can |it has |it is ).*$")) {
            logger.log(Level.INFO, "User entered incorrectly formatted distinguishing property.");
            textUserInterface.factPromptGuidance();
            textUserInterface.factPrompt(firstAnimal, secondAnimal);
            distinguishingFact = textUserInterface.getInput();
        }

        textUserInterface.animalBinaryChoiceInput(secondAnimal);
        String appliesToSecondAnimalInput = textUserInterface.getInput();
        BinaryChoice appliesToSecondAnimalAnswer = parseBinaryChoiceAnswer(appliesToSecondAnimalInput);
        while (appliesToSecondAnimalAnswer == BinaryChoice.UNDETERMINED) {
            logger.log(Level.INFO, "User entered ambiguous answer for binary choice question.");
            textUserInterface.unclearYesNoPrompt();
            appliesToSecondAnimalInput = textUserInterface.getInput();
            appliesToSecondAnimalAnswer = parseBinaryChoiceAnswer(appliesToSecondAnimalInput);
        }

        if (appliesToSecondAnimalAnswer == BinaryChoice.YES) {
            firstAnimal.setProperty(distinguishingFact, false);
            secondAnimal.setProperty(distinguishingFact, true);
        } else {
            firstAnimal.setProperty(distinguishingFact, true);
            secondAnimal.setProperty(distinguishingFact, false);
        }

        textUserInterface.printFactsLearned(firstAnimal, secondAnimal);
        logger.log(Level.INFO, "Animal facts printed.");

        textUserInterface.sayGoodbye();
        logger.log(Level.INFO, "App terminated normally.");
        fileHandler.close();
    }
}
