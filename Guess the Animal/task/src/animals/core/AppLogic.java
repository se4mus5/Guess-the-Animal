package animals.core;

import animals.entity.Animal;
import animals.language.BinaryChoice;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

import static animals.language.Parser.parseYesNo;

public class AppLogic {
    private final TextUserInterface textUserInterface;

    public AppLogic() {
        this.textUserInterface = new TextUserInterface();
    }

    public void start() {
        // set up logging
        Logger logger = Logger.getLogger(this.getClass().getName());
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
        textUserInterface.animalEntryPrompt();
        String animalName = textUserInterface.getInput();
        Animal animal = new Animal(animalName);
        textUserInterface.animalYesNoPrompt(animal);
        String yesNoInput = textUserInterface.getInput();
        BinaryChoice yesNoAnswer = parseYesNo(yesNoInput);
        while (yesNoAnswer == BinaryChoice.UNDETERMINED) {
            logger.log(Level.INFO, "User entered ambiguous data.");
            textUserInterface.unclearYesNoPrompt();
            yesNoInput = textUserInterface.getInput();
            yesNoAnswer = parseYesNo(yesNoInput);
        }
        textUserInterface.reinforceAnswer(yesNoAnswer.toString());
        textUserInterface.sayGoodbye();
        logger.log(Level.INFO, "App terminated normally.");
        fileHandler.close();
    }
}
