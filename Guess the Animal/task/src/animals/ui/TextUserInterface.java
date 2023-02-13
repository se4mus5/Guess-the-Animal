package animals.ui;

import animals.language.BinaryChoice;
import animals.entity.KnowledgeTreeNode;

import java.util.Random;
import java.util.Scanner;

import static animals.helper.Parser.parseBinaryChoiceAnswer;
import static animals.language.Literals.*;
import static animals.helper.Transformer.transformAnimalFactStatementIntoQuestion;
import static animals.helper.Parser.parsePeriodOfDay;

public class TextUserInterface {
    Scanner scan;
    Random rng;

    public TextUserInterface() {
        this.scan = new Scanner(System.in);
        rng = new Random();
    }

    public void greet() {
        System.out.println(GREETINGS.get(parsePeriodOfDay()));
        System.out.println();
    }

    public void promptForFavoriteAnimal() {
        System.out.println("""
                I want to learn about animals.
                Which animal do you like most?""");
    }

    public void promptPostEntry() {
        System.out.println("Wonderful! I've learned so much about animals!");
    }

    public void promptRules() {
        System.out.println("Let's play a game! You think of an animal, and I guess it. Press enter when you're ready.");
    }

    public void promptGiveUp() {
        System.out.println("I give up. What animal do you have in mind?");
    }

    public void promptRaiseAnimalFactBinaryQuestion(KnowledgeTreeNode animalFact) {
        System.out.println(transformAnimalFactStatementIntoQuestion(animalFact.getFact(true)));
    }

    public void promptEnterAnimalFactOf(KnowledgeTreeNode guessedAnimal, KnowledgeTreeNode userThoughtAnimal) {
        System.out.printf("""
                Specify a fact that distinguishes %s from %s.
                The sentence should satisfy one of the following templates:
                - It can ...
                - It has ...
                - It is a/an ...
                """, guessedAnimal.getAnimalNameWithArticle(), userThoughtAnimal.getAnimalNameWithArticle());
    }

    public void sayGoodbye() {
        System.out.println(GOODBYE.get(rng.nextInt(GOODBYE.size() - 1)));
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
        while (!distinguishingFact.matches("^(it can |it has |it is ).*$")) {
            promptEnterAnimalFactOf(guessedAnimal, userThoughtAnimal);
            distinguishingFact = getInput();
        }
        return distinguishingFact;
    }

    public void promptGuessAnimal(KnowledgeTreeNode animal) {
        System.out.printf("Is it %s?\n", animal.getAnimalNameWithArticle());
    }
    
    public void promptDoesPropertyApplyToAnimal(KnowledgeTreeNode animal) {
        System.out.printf("Is this correct for %s?\n", animal.getAnimalNameWithArticle());
    }

    public void promptUnclearYesNo() {
        System.out.println(CLARIFICATION.get(rng.nextInt(CLARIFICATION.size() - 1)));
    }

    public void displayLearningSummary(KnowledgeTreeNode distinguishingFact, KnowledgeTreeNode appliesToAnimal, KnowledgeTreeNode doesNotApplyToAnimal) {
        System.out.printf("""
                I have learned the following facts about animals:
                 - The %s %s.
                 - The %s %s.
                I can distinguish these animals by asking the question:
                 - %s
                Nice! I've learned so much about animals!
                
                """, appliesToAnimal.getAnimalName(), distinguishingFact.getFact(true),
                    doesNotApplyToAnimal.getAnimalName(), distinguishingFact.getFact(false),
                transformAnimalFactStatementIntoQuestion(distinguishingFact.getFact(true)));
    }

    public void promptPlayAgain() {
        System.out.println("Would you like to play again?");
    }
}
