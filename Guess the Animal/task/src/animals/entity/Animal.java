package animals.entity;

import java.util.Set;

public class Animal {
    private final String article;
    private final String name;

    private String property;
    private boolean propertyApplies;

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

        property = "has no known properties.";
        propertyApplies = true;
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

    public String getName() {
        return name;
    }

    public String getProperty() {
        if (!propertyApplies) {
            property = property.replace(" can ", " can't ");
            property = property.replace(" has ", " doesn't have ");
            property = property.replace(" is ", " isn't ");
        }
        property = property.replace("it ", "");
        property = property.replace("?", "");

        return property;
    }

    public void setProperty(String property, boolean propertyApplies) {
        this.property = property;
        this.propertyApplies = propertyApplies;
    }

    public boolean isPropertyApplies() {
        return propertyApplies;
    }
}
