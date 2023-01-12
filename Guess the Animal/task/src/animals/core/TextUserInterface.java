package animals.core;

import animals.entity.Animal;

import java.util.Random;
import java.util.Scanner;

import static animals.language.Literal.*;
import static animals.language.Transformer.transformAnimalFactStatementIntoQuestion;
import static animals.util.StaticHelper.getPeriodOfDay;

public class TextUserInterface {
    Scanner scan;
    Random rng;

    public TextUserInterface() {
        this.scan = new Scanner(System.in);
        rng = new Random();
    }

    public void greet() {
        System.out.println(GREETINGS.get(getPeriodOfDay()));
        System.out.println();
    }

    public void sayGoodbye() {
        System.out.println(GOODBYE.get(rng.nextInt(GOODBYE.size() - 1)));
    }

    String getInput() {
        return scan.nextLine().trim().toLowerCase();
    }

    void animalEntryPrompt(int animalSequence) {
        if (animalSequence < 1 || animalSequence > 2) {
            throw new IllegalArgumentException("Valid parameter range: 1-2");
        }
        String[] animalSeqWord = new String[] {"INVALID", "first", "second"};
        System.out.printf("Enter the %s animal:\n", animalSeqWord[animalSequence]);
    }
    
    void animalBinaryChoiceInput(Animal animal) {
        // System.out.printf("Is it %s?\n", animal);
        System.out.printf("Is this correct for %s?\n", animal);
    }

    void unclearYesNoPrompt() {
        System.out.println(CLARIFICATION.get(rng.nextInt(CLARIFICATION.size() - 1)));
    }

    void factPrompt(Animal firstAnimal, Animal secondAnimal) {
        System.out.printf("""
                Specify a fact that distinguishes %s from %s.
                The sentence should be of the format: 'It can/has/is ...'.
                
                """, firstAnimal, secondAnimal);
    }

    void factPromptGuidance() {
        System.out.println("""
                The examples of a statement:
                 - It can fly
                 - It has horn
                 - It is a mammal
                """);
    }

    void printFactsLearned(Animal firstAnimal, Animal secondAnimal) {
        System.out.println("I learned the following facts about animals:");
        System.out.printf("- The %s %s.\n", firstAnimal.getName(), firstAnimal.getProperty());
        System.out.printf("- The %s %s.\n", secondAnimal.getName(), secondAnimal.getProperty());
        System.out.println("I can distinguish these animals by asking the question:");
        Animal animalWithApplyingProperty = firstAnimal.isPropertyApplies() ? firstAnimal : secondAnimal;
        String animalProperty = animalWithApplyingProperty.getProperty();
        System.out.println(transformAnimalFactStatementIntoQuestion(animalProperty));

    }

}
