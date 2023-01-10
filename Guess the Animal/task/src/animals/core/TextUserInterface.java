package animals.core;

import animals.entity.Animal;

import java.util.Random;
import java.util.Scanner;

import static animals.language.Literal.*;
import static animals.util.StaticHelper.getPeriodOfDay;

// TODO refactor prompts once the requirements become more concrete in subsequent stages
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

    void animalEntryPrompt() {
        System.out.println("Enter an animal:");
    }
    
    void animalYesNoPrompt(Animal animal) {
        System.out.printf("Is it %s?\n", animal);
    }

    void unclearYesNoPrompt() {
        System.out.println(CLARIFICATION.get(rng.nextInt(CLARIFICATION.size() - 1)));
    }

    void reinforceAnswer(String answer) {
        System.out.printf("You answered: %s\n", answer);
        System.out.println();
    }

}
