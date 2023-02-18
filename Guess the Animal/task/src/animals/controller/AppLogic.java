package animals.controller;

import animals.entity.KnowledgeTreeStatistics;
import animals.helper.PersistenceFormat;
import animals.language.BinaryChoice;
import animals.entity.KnowledgeTree;
import animals.entity.KnowledgeTreeNode;
import animals.ui.TextUserInterface;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

import static animals.helper.CentralLogger.logger;
import static animals.helper.TreeHelpers.*;

public class AppLogic {
    private final TextUserInterface ui;

    private BinaryChoice doesEnteredFactApplyToAnimal; // drives leftChild/rightChild configuration of distinguishingFactNode
    private BinaryChoice doesKnowledgeTreeFactApplyToAnimal; // drives shape of knowledgeTree
    private KnowledgeTree knowledgeTree;
    private KnowledgeTreeNode distinguishingFactNode;
    private KnowledgeTreeNode guessedAnimal;
    private KnowledgeTreeNode userThoughtAnimal;

    @Parameter(names = {"-type"})
    private PersistenceFormat persistenceFormat = PersistenceFormat.JSON;

    public AppLogic(String[] args) {
        JCommander.newBuilder()
                .addObject(this)
                .build()
                .parse(args);
        this.ui = new TextUserInterface();

        doesEnteredFactApplyToAnimal = BinaryChoice.UNDETERMINED;
        doesKnowledgeTreeFactApplyToAnimal = BinaryChoice.UNDETERMINED;
        knowledgeTree = new KnowledgeTree();
    }

    public void mainWorkflow() {

        logger.log(Level.INFO, "========================== App started ==========================");
        ui.printGreeting();
        knowledgeTree = assembleKnowledgeTree();

        // TODO use or remove
        //System.out.println("## DIAG ## testing node count: " + countNodes(knowledgeTree.getRoot(), true));
        //System.out.println("## DIAG ## testing node depth: "
        //        + Arrays.toString(depthOfTreeAtEachLeaf(knowledgeTree.getRoot()).toArray()));

        ui.printWelcome();
        while(true) {
            ui.printMainMenu();
            try {
                int menuChoice = Integer.parseInt(ui.getInput());

                switch (menuChoice) {
                    case 1 -> playGuessingGame();
                    case 2 -> listAnimals();
                    case 3 -> findAnimal();
                    case 4 -> calculateStatistics();
                    case 5 -> displayKnowledgeTree();
                    case 0 -> {
                        ui.sayGoodbye();
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please select a **number** from the menu choices.");
            }
        }
    }

    private void calculateStatistics() {
        KnowledgeTreeStatistics stats = new KnowledgeTreeStatistics(knowledgeTree.getRoot());
        ui.printStats(stats);
    }

    private void listAnimals() {
        ui.printAnimals(knowledgeTree.getDataFromAllLeaves());
    }

    private void findAnimal() {
        ui.promptEnterAnimal();
        String animalName = ui.getInput();
        ui.printAnimalFacts(animalName, knowledgeTree.findAnimal(animalName));
    }

    private void displayKnowledgeTree() {
        List<String> knowledgeTreeData = new ArrayList<>();
        traversePreorder(knowledgeTree.getRoot(), knowledgeTreeData, 0);
        ui.printKnowledgeTree(knowledgeTreeData);
    }

    private void playGuessingGame() {
        logger.log(Level.INFO, "User selected guessing game.");
        ui.printRules();
        ui.getInput();
        while (true) {
            if (knowledgeTree.getCurrentNode().isAnimal()) {
                guessedAnimal = knowledgeTree.getCurrentNode();
                ui.promptGuessAnimal(guessedAnimal);

                BinaryChoice didComputerGuessCorrectly = ui.getBinaryInput();
                if (didComputerGuessCorrectly == BinaryChoice.NO) {
                    incorrectGuessWorkflow();
                    configureDistinguishingFactNode();
                    maintainKnowledgeTree();
                } else if (didComputerGuessCorrectly == BinaryChoice.YES) {
                    logger.log(Level.INFO, String.format("Correctly guessed the animal: %s", guessedAnimal.getAnimalNameWithArticle()));
                    ui.sayGoodbye();
                }
            } else if (knowledgeTree.getCurrentNode().isFact()) {
                ui.promptRaiseAnimalFactBinaryQuestion(knowledgeTree.getCurrentNode());
                doesKnowledgeTreeFactApplyToAnimal = ui.getBinaryInput();
                logger.log(Level.FINE, String.format("## DIAG ## Q: %s? A: %s", knowledgeTree.getCurrentNode().getFact(true), doesKnowledgeTreeFactApplyToAnimal));
                if (doesKnowledgeTreeFactApplyToAnimal == BinaryChoice.YES) {
                    knowledgeTree.moveRight();
                } else if (doesKnowledgeTreeFactApplyToAnimal == BinaryChoice.NO) {
                    knowledgeTree.moveLeft();
                }
                continue;
            }

            ui.promptPostEntry();
            ui.promptPlayAgain();
            BinaryChoice playAgainResponse = ui.getBinaryInput();
            if (playAgainResponse == BinaryChoice.NO) {
                logger.log(Level.INFO, "User quit the guessing game.");
                break;
            }

            knowledgeTree.resetCurrentNodeToRoot(); // game flow should traverse from root
            ui.printRules();
            ui.getInput();
        }

        knowledgeTree.serialize(persistenceFormat);
        logger.log(Level.INFO, "Serialized knowledge tree.");
    }

    /**
     * Create knowledge tree.
     * First try to restore state from serialized form (JSON, YAML, XML).
     * If serialized file does not exist, as the user for input and create a knowledge tree with a single node.
     * @return the assembled KnowledgeTree object
     */
    private KnowledgeTree assembleKnowledgeTree() {
        KnowledgeTree knowledgeTree;
        Path dataFilePath = Paths.get("animals." + persistenceFormat.name().toLowerCase());
        if (Files.exists(dataFilePath)) {
            knowledgeTree = KnowledgeTree.constructFromSerialized(persistenceFormat);
        } else {
            ui.promptForFavoriteAnimal();
            String favoriteAnimalName = ui.getInput();
            knowledgeTree = new KnowledgeTree(new KnowledgeTreeNode(favoriteAnimalName));
            logger.log(Level.INFO, "Favorite animal entered.");
        }
        return knowledgeTree;
    }

    /**
     * Controls flow of dialog with user for a failed computer guess.
     */
    private void incorrectGuessWorkflow() {
        ui.promptGiveUp();
        String userThoughtAnimalName = ui.getInput();
        userThoughtAnimal = new KnowledgeTreeNode(userThoughtAnimalName);
        logger.log(Level.INFO, String.format("Failed to guess the animal, guess: %s, actual animal: %s",
                guessedAnimal.getAnimalName(), userThoughtAnimal.getAnimalName()));
        ui.promptEnterAnimalFactOf(guessedAnimal, userThoughtAnimal);
        String distinguishingFactEntered = ui.getDistinguishingFactInput(guessedAnimal, userThoughtAnimal);
        distinguishingFactNode = new KnowledgeTreeNode(distinguishingFactEntered);
        ui.promptDoesPropertyApplyToAnimal(userThoughtAnimal);
        doesEnteredFactApplyToAnimal = ui.getBinaryInput();
    }

    /**
     * Attaches child nodes in the correct left-right configuration to the KnowledgeTreeNode that represents the distinguished
     * fact between the two children.
     * Also invokes printing - TODO subject to low-pri refactoring
     */
    private void configureDistinguishingFactNode() {
        KnowledgeTreeNode appliesToAnimal, doesNotApplyToAnimal;
        logger.log(Level.FINE, "## DIAG ## fact: " + distinguishingFactNode.getData()); // can't use getFact: no children set
        logger.log(Level.FINE, "## DIAG ## does fact apply? " + doesEnteredFactApplyToAnimal);

        if (doesEnteredFactApplyToAnimal == BinaryChoice.YES) {
            appliesToAnimal = userThoughtAnimal;
            doesNotApplyToAnimal = guessedAnimal;
        } else {
            appliesToAnimal = guessedAnimal;
            doesNotApplyToAnimal = userThoughtAnimal;
        }
        distinguishingFactNode.setLeftChild(doesNotApplyToAnimal);
        distinguishingFactNode.setRightChild(appliesToAnimal);

        //structurally, this printout doesn't really fit here, but does depend on the variables defined in this method
        ui.displayLearningSummary(distinguishingFactNode, appliesToAnimal, doesNotApplyToAnimal);
        logger.log(Level.FINE, "## DIAG ## new node: " + distinguishingFactNode);
    }

    private void maintainKnowledgeTree() {
        if (doesKnowledgeTreeFactApplyToAnimal == BinaryChoice.YES) {
            knowledgeTree.offerRight(distinguishingFactNode);
        } else {
            knowledgeTree.offerLeft(distinguishingFactNode);
        }
        logger.log(Level.INFO, "New node added to knowledge graph.");
    }
}
