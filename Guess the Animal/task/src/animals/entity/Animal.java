package animals.entity;

import java.util.Set;
// TODO refactor class for regexp

public class Animal {
    private final String article;
    private final String name;

    public Animal(String name) {
        if (name.startsWith("a ")) {
            this.article = "a";
            this.name = name.substring("a ".length());
        } else if (name.startsWith("an ")) {
            this.article = "an";
            this.name = name.substring("an ".length());
        } else if (name.startsWith("the ")) {
            this.article = determineArticle(name.substring("the ".length()));
            this.name = name.substring("the ".length());
        } else {
            this.article = determineArticle(name);
            this.name = name;
        }
    }

    static String determineArticle(String animalName) {
        Set<Character> VOWELS = Set.of('a', 'i', 'e', 'o', 'u');
        char firstCharOfAnimalName = animalName.charAt(0);
        return VOWELS.contains(firstCharOfAnimalName) ? "an" : "a";
    }

    @Override
    public String toString() {
        return article + " " + name;
    }
}
