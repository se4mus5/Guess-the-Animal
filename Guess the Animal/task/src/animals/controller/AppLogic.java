package animals.controller;

import animals.language.BinaryChoice;
import animals.entity.KnowledgeTree;
import animals.entity.KnowledgeTreeNode;
import animals.ui.TextUserInterface;

import java.util.logging.*;

import static animals.helper.CentralLogger.logger;

public class AppLogic {
    private final TextUserInterface textUserInterface;

    public AppLogic() {
        this.textUserInterface = new TextUserInterface();
    }

    public void gamePlayWorkflow() {
        logger.log(Level.INFO, "========================== App started ==========================");
        textUserInterface.greet();
        textUserInterface.promptForFavoriteAnimal();
        String favoriteAnimalName = textUserInterface.getInput();
        logger.log(Level.INFO, "Favorite animal entered.");
        KnowledgeTree knowledgeTree = new KnowledgeTree(new KnowledgeTreeNode(favoriteAnimalName));

        textUserInterface.promptPostEntry();
        textUserInterface.promptRules();
        textUserInterface.getInput();

        BinaryChoice doesFactApply;
        BinaryChoice doesFactQuestionApply = BinaryChoice.UNDETERMINED;

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
                    doesFactApply = textUserInterface.getBinaryInput();

                    KnowledgeTreeNode appliesToAnimal, doesNotApplyToAnimal;
                    KnowledgeTreeNode distinguishingFact = new KnowledgeTreeNode(distinguishingFactEntered);
                    logger.log(Level.FINE, "## DIAG ## fact: " + distinguishingFact.getData()); // can't use getFact: no children set
                    logger.log(Level.FINE, "## DIAG ## does fact apply? " + doesFactApply);
                    if (doesFactApply == BinaryChoice.YES) {
                        appliesToAnimal = userThoughtAnimal;
                        doesNotApplyToAnimal = guessedAnimal;
                    } else {
                        appliesToAnimal = guessedAnimal;
                        doesNotApplyToAnimal = userThoughtAnimal;
                    }
                    distinguishingFact.setLeftChild(doesNotApplyToAnimal);
                    distinguishingFact.setRightChild(appliesToAnimal);
                    textUserInterface.displayLearningSummary(distinguishingFact, appliesToAnimal, doesNotApplyToAnimal);
                    logger.log(Level.FINE, "## DIAG ## new node: " + distinguishingFact);

                    // maintain tree
                    if (doesFactQuestionApply == BinaryChoice.YES) {
                        knowledgeTree.offerRight(distinguishingFact);
                    } else {
                        knowledgeTree.offerLeft(distinguishingFact);
                    }
                    logger.log(Level.INFO, "New node added to knowledge graph.");
                } else if (didComputerGuessCorrectly == BinaryChoice.YES) {
                    logger.log(Level.INFO, String.format("Correctly guessed the animal: %s", guessedAnimal.getAnimalNameWithArticle()));
                    textUserInterface.sayGoodbye();
                }
            } else if (knowledgeTree.getCurrentNode().isFact()) {
                textUserInterface.promptRaiseAnimalFactBinaryQuestion(knowledgeTree.getCurrentNode());
                doesFactQuestionApply = textUserInterface.getBinaryInput();
                logger.log(Level.FINE, String.format("## DIAG ## Q: %s? A: %s", knowledgeTree.getCurrentNode().getFact(true), doesFactQuestionApply));
                if (doesFactQuestionApply == BinaryChoice.YES) {
                    knowledgeTree.moveRight();
                } else if (doesFactQuestionApply == BinaryChoice.NO) {
                    knowledgeTree.moveLeft();
                }
                continue;
            }

            textUserInterface.promptPlayAgain();
            BinaryChoice playAgainResponse = textUserInterface.getBinaryInput();
            if (playAgainResponse == BinaryChoice.NO) {
                logger.log(Level.INFO, "User quit the game.");
                break;
            }

            textUserInterface.promptRules();
            textUserInterface.getInput();

            knowledgeTree.resetCurrentNodeToRoot(); // game flow should traverse from root
        }

        textUserInterface.sayGoodbye();
        logger.log(Level.INFO, "App terminated normally.");
    }
}
