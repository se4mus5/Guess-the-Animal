package animals.controller;

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
import java.util.logging.*;

import static animals.helper.CentralLogger.logger;

public class AppLogic {
    private final TextUserInterface textUserInterface;
    @Parameter(names = {"-type"})
    private PersistenceFormat persistenceFormat = PersistenceFormat.JSON;

    public AppLogic(String[] args) {
        JCommander.newBuilder()
                .addObject(this)
                .build()
                .parse(args);
        this.textUserInterface = new TextUserInterface();
    }

    public void gamePlayWorkflow() { // TODO modularize monolithic flow

        logger.log(Level.INFO, "========================== App started ==========================");
        textUserInterface.greet();
        KnowledgeTree knowledgeTree = assembleKnowledgeTree();

        BinaryChoice doesEnteredFactApplyToAnimal; // drives leftChild/rightChild configuration of distinguishingFactNode
        BinaryChoice doesKnowledgeTreeFactApplyToAnimal = BinaryChoice.UNDETERMINED; // drives shape of knowledgeTree

        textUserInterface.displayRules();
        textUserInterface.getInput();
        while (true) {
            if (knowledgeTree.getCurrentNode().isAnimal()) {
                KnowledgeTreeNode guessedAnimal = knowledgeTree.getCurrentNode();
                textUserInterface.promptGuessAnimal(guessedAnimal);

                BinaryChoice didComputerGuessCorrectly = textUserInterface.getBinaryInput();
                if (didComputerGuessCorrectly == BinaryChoice.NO) {
                    textUserInterface.promptGiveUp();
                    String userThoughtAnimalName = textUserInterface.getInput();
                    KnowledgeTreeNode userThoughtAnimal = new KnowledgeTreeNode(userThoughtAnimalName);
                    logger.log(Level.INFO, String.format("Failed to guess the animal, guess: %s, actual animal: %s",
                            guessedAnimal.getAnimalName(), userThoughtAnimal.getAnimalName()));
                    textUserInterface.promptEnterAnimalFactOf(guessedAnimal, userThoughtAnimal);
                    String distinguishingFactEntered = textUserInterface.getDistinguishingFactInput(guessedAnimal, userThoughtAnimal);
                    textUserInterface.promptDoesPropertyApplyToAnimal(userThoughtAnimal);
                    doesEnteredFactApplyToAnimal = textUserInterface.getBinaryInput();

                    KnowledgeTreeNode appliesToAnimal, doesNotApplyToAnimal;
                    KnowledgeTreeNode distinguishingFactNode = new KnowledgeTreeNode(distinguishingFactEntered);
                    logger.log(Level.FINE, "## DIAG ## fact: " + distinguishingFactNode.getData()); // can't use getFact: no children set
                    logger.log(Level.FINE, "## DIAG ## does fact apply? " + doesEnteredFactApplyToAnimal);

                    // configure knowledgeTreeNode
                    if (doesEnteredFactApplyToAnimal == BinaryChoice.YES) {
                        appliesToAnimal = userThoughtAnimal;
                        doesNotApplyToAnimal = guessedAnimal;
                    } else {
                        appliesToAnimal = guessedAnimal;
                        doesNotApplyToAnimal = userThoughtAnimal;
                    }
                    distinguishingFactNode.setLeftChild(doesNotApplyToAnimal);
                    distinguishingFactNode.setRightChild(appliesToAnimal);
                    textUserInterface.displayLearningSummary(distinguishingFactNode, appliesToAnimal, doesNotApplyToAnimal);
                    logger.log(Level.FINE, "## DIAG ## new node: " + distinguishingFactNode);

                    // maintain knowledgeTree
                    if (doesKnowledgeTreeFactApplyToAnimal == BinaryChoice.YES) {
                        knowledgeTree.offerRight(distinguishingFactNode);
                    } else {
                        knowledgeTree.offerLeft(distinguishingFactNode);
                    }
                    logger.log(Level.INFO, "New node added to knowledge graph.");
                } else if (didComputerGuessCorrectly == BinaryChoice.YES) {
                    logger.log(Level.INFO, String.format("Correctly guessed the animal: %s", guessedAnimal.getAnimalNameWithArticle()));
                    textUserInterface.sayGoodbye();
                }
            } else if (knowledgeTree.getCurrentNode().isFact()) {
                textUserInterface.promptRaiseAnimalFactBinaryQuestion(knowledgeTree.getCurrentNode());
                doesKnowledgeTreeFactApplyToAnimal = textUserInterface.getBinaryInput();
                logger.log(Level.FINE, String.format("## DIAG ## Q: %s? A: %s", knowledgeTree.getCurrentNode().getFact(true), doesKnowledgeTreeFactApplyToAnimal));
                if (doesKnowledgeTreeFactApplyToAnimal == BinaryChoice.YES) {
                    knowledgeTree.moveRight();
                } else if (doesKnowledgeTreeFactApplyToAnimal == BinaryChoice.NO) {
                    knowledgeTree.moveLeft();
                }
                continue;
            }

            textUserInterface.promptPostEntry();
            textUserInterface.promptPlayAgain();
            BinaryChoice playAgainResponse = textUserInterface.getBinaryInput();
            if (playAgainResponse == BinaryChoice.NO) {
                logger.log(Level.INFO, "User quit the game.");
                break;
            }

            knowledgeTree.resetCurrentNodeToRoot(); // game flow should traverse from root

            textUserInterface.displayRules();
            textUserInterface.getInput();
        }

        knowledgeTree.serialize(persistenceFormat);
        logger.log(Level.INFO, "Serialized knowledge tree.");
        textUserInterface.sayGoodbye();
        logger.log(Level.INFO, "App terminated normally.");
    }

    /**
     * Create knowledge tree.
     * First try to restore state from serialized form (JSON, YAML, XML).
     * If serialized file does not exist, as the user for input and create a knowledge tree with a single node.
     * @return
     */
    public KnowledgeTree assembleKnowledgeTree() {
        KnowledgeTree knowledgeTree;
        Path dataFilePath = Paths.get("animals." + persistenceFormat.name().toLowerCase());
        if (Files.exists(dataFilePath)) {
            knowledgeTree = KnowledgeTree.constructFromSerialized(persistenceFormat);
        } else {
            textUserInterface.promptForFavoriteAnimal();
            String favoriteAnimalName = textUserInterface.getInput();
            knowledgeTree = new KnowledgeTree(new KnowledgeTreeNode(favoriteAnimalName));
            logger.log(Level.INFO, "Favorite animal entered.");
        }
        return knowledgeTree;
    }
}
