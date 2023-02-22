package animals.ui;

import animals.entity.KnowledgeTreeStatistics;
import animals.helper.BinaryChoice;
import animals.entity.KnowledgeTreeNode;

import java.text.MessageFormat;
import java.util.*;

import static animals.helper.Parser.parseBinaryChoiceAnswer;
import static animals.helper.Transformer.transformAnimalFactStatementIntoQuestion;
import static animals.helper.Parser.parsePeriodOfDay;

public class TextUserInterface {
    private final Scanner scan;
    private final Random rng;
    private final ResourceBundle messageResource;

    public TextUserInterface() {
        this.scan = new Scanner(System.in);
        rng = new Random();
        messageResource = ResourceBundle.getBundle("messages");
    }

    public void printGreeting() {
        System.out.println(parsePeriodOfDay());
    }

    public void printWelcome() {
        System.out.println();
        System.out.println(messageResource.getString("welcome"));
        //System.out.println("Welcome to the animal expert system!");
        System.out.println();
    }

    public void promptForFavoriteAnimal() {
        System.out.println();
        System.out.println(messageResource.getString("animal.wantLearn"));
        System.out.println(messageResource.getString("animal.askFavorite"));
    }

    public void printAnimals(List<String> animalList) {
        //System.out.println("Here are the animals I know:");
        System.out.println(messageResource.getString("tree.list.animals"));
        Collections.sort(animalList); // tests expect the output in alphabetical order
        for (String animal : animalList) {
            System.out.printf(" - %s\n", animal);
        }
    }

    public void printAnimalFacts(String animal, List<String> animalFacts) {
        if (!animalFacts.isEmpty()) {
            // tests expect the output in reverse order, but list is passed as immutable
            List<String> newAnimalFacts = new ArrayList<>(animalFacts);
            Collections.reverse(newAnimalFacts);
            //System.out.printf("Facts about the %s:\n", animal);
            System.out.println(MessageFormat.format(messageResource.getString("tree.search.facts"),
                    animal));
            for (String fact : newAnimalFacts) {
                // System.out.printf(" - It %s\n", fact);
                System.out.printf(messageResource.getString("tree.search.printf"), fact);
            }
        } else {
            //System.out.printf("No facts about the %s.\n", animal);
            System.out.println(MessageFormat.format(messageResource.getString("tree.search.noFacts"),
                    animal));
        }
    }

    public void printKnowledgeTree(List<String> knowledgeTreeData) {
        System.out.println();
        knowledgeTreeData.forEach(System.out::println);
        System.out.println();
    }

    public void promptPostEntry() {
        //System.out.println("I've learned so much about animals!");
        System.out.println(messageResource.getString("animal.learnedMuch"));
    }

    public void printRules() {
        //System.out.println("Let's play a game! You think of an animal, and I guess it. Press enter when you're ready.");
        System.out.println(messageResource.getString("game.letsPlay"));
        System.out.println(messageResource.getString("game.think"));
        System.out.println(messageResource.getString("game.enter"));
    }

    public void promptGiveUp() {
        //System.out.println("I give up. What animal do you have in mind?");
        System.out.println(messageResource.getString("game.giveUp"));
    }

    public void promptRaiseAnimalFactBinaryQuestion(KnowledgeTreeNode animalFact) {
        System.out.println(transformAnimalFactStatementIntoQuestion(animalFact.getFact(true)));
    }

    public void promptEnterAnimalFactOf(KnowledgeTreeNode guessedAnimal, KnowledgeTreeNode userThoughtAnimal) {
        System.out.println(MessageFormat.format(messageResource.getString("statement.prompt"),
                guessedAnimal.getAnimalNameWithArticle(), userThoughtAnimal.getAnimalNameWithArticle()));
    }

    public void sayGoodbye() {
        //System.out.println(GOODBYE.get(rng.nextInt(GOODBYE.size() - 1)));
        String[] farewellMessages = messageResource.getString("farewell").split("\\f");
        System.out.println(farewellMessages[rng.nextInt(farewellMessages.length - 1)]);
    }

    /**
     * The getInput method reads a line of input, removes training and ending whitespace and converts to lowercase
     * @return void
     */
    public String getInput() {
        return scan.nextLine().trim().toLowerCase();
    }

    public BinaryChoice getBinaryInput() {
        String binaryInput = getInput();
        BinaryChoice parsedBinaryInput = parseBinaryChoiceAnswer(binaryInput);
        while (parsedBinaryInput == BinaryChoice.UNDETERMINED) {
            promptUnclearYesNo();
            binaryInput = getInput();
            parsedBinaryInput = parseBinaryChoiceAnswer(binaryInput);
        }
        return parsedBinaryInput;
    }

    public String getDistinguishingFactInput(KnowledgeTreeNode guessedAnimal, KnowledgeTreeNode userThoughtAnimal) {
        String distinguishingFact = getInput();
        String regex = System.getProperty("user.language").equals("eo") ?
                "^(ĝi ).*$" : "^(it can |it has |it is ).*$";
        while (!distinguishingFact.matches(regex)) {
            promptEnterAnimalFactOf(guessedAnimal, userThoughtAnimal);
            distinguishingFact = getInput();
        }
        return distinguishingFact;
    }

    public void promptGuessAnimal(KnowledgeTreeNode animal) {
        //System.out.printf("Is it %s?\n", animal.getAnimalNameWithArticle());
        System.out.println(MessageFormat.format(messageResource.getString("game.ask"),
                animal.getAnimalNameWithArticle()));
    }

    public void promptEnterAnimal() {
        //System.out.println("Enter the animal:");
        System.out.println(messageResource.getString("animal.prompt"));
    }
    
    public void promptDoesPropertyApplyToAnimal(KnowledgeTreeNode animal) {
        //System.out.printf("Is the statement correct for %s?\n", animal.getAnimalNameWithArticle());
        System.out.println(MessageFormat.format(messageResource.getString("game.isCorrect"),
                animal.getAnimalNameWithArticle()));
    }

    public void promptUnclearYesNo() {
        //System.out.println(CLARIFICATION.get(rng.nextInt(CLARIFICATION.size() - 1)));
        String[] clarificationMessages = messageResource.getString("ask.again").split("\\f");
        System.out.println(clarificationMessages[rng.nextInt(clarificationMessages.length - 1)]);
    }

    public void displayLearningSummary(KnowledgeTreeNode distinguishingFact, KnowledgeTreeNode appliesToAnimal, KnowledgeTreeNode doesNotApplyToAnimal) {
        System.out.println(messageResource.getString("game.learned"));
        System.out.println(MessageFormat.format(messageResource.getString("game.fact"),
                appliesToAnimal.getAnimalName(), distinguishingFact.getFact(true)));
        System.out.println(MessageFormat.format(messageResource.getString("game.fact"),
                doesNotApplyToAnimal.getAnimalName(), distinguishingFact.getFact(false)));
        System.out.println(messageResource.getString("game.distinguish"));
        System.out.println(" - " + transformAnimalFactStatementIntoQuestion(distinguishingFact.getFact(true)));
        String[] nice = messageResource.getString("animal.nice").split("\\f");
        System.out.println(nice[rng.nextInt(nice.length - 1)]);
    }

    public void promptPlayAgain() {
        //System.out.println("Would you like to play again?");
        String[] playAgainMessages = messageResource.getString("game.again").split("\\f");
        System.out.println(playAgainMessages[rng.nextInt(playAgainMessages.length - 1)]);
    }

    public void printMainMenu() {
        System.out.println(messageResource.getString("menu.property.title"));
        System.out.println();
        System.out.println("1. " + messageResource.getString("menu.entry.play"));
        System.out.println("2. " + messageResource.getString("menu.entry.list"));
        System.out.println("3. " + messageResource.getString("menu.entry.search"));
        System.out.println("4. " + messageResource.getString("menu.entry.statistics"));
        System.out.println("5. " + messageResource.getString("menu.entry.print"));
        System.out.println("0. " + messageResource.getString("menu.property.exit"));
    }

    public void printMenuSelectionError() {
        System.out.println(MessageFormat.format(messageResource.getString("menu.property.error"), 5)); // 5 = number of menu items, low-pri TODO make this dynamic
    }

    public void printStats(KnowledgeTreeStatistics stats) {
        System.out.println(messageResource.getString("tree.stats.title"));
        System.out.println();
        System.out.println(MessageFormat.format(messageResource.getString("tree.stats.root"),
                stats.getRootNodeData()));
        System.out.println(MessageFormat.format(messageResource.getString("tree.stats.nodes"),
                stats.getNumOfNodes()));
        System.out.println(MessageFormat.format(messageResource.getString("tree.stats.animals"),
                stats.getNumOfAnimals()));
        System.out.println(MessageFormat.format(messageResource.getString("tree.stats.statements"),
                stats.getNumOfStatements()));
        System.out.println(MessageFormat.format(messageResource.getString("tree.stats.height"),
                stats.getHeight()));
        System.out.println(MessageFormat.format(messageResource.getString("tree.stats.minimum"),
                stats.getMinDepth()));
        System.out.println(MessageFormat.format(messageResource.getString("tree.stats.average"),
                stats.getAvgDepth()));
    }
}
