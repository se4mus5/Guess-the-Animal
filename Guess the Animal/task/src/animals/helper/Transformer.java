package animals.helper;

public class Transformer {
    /**
     * This method transforms a statement about a property of an animal into a question
     * @param animalFact Statement about a properties of an animal
     * @return Animal fact statement rephrased as question
     */
    public static String transformAnimalFactStatementIntoQuestion(String animalFact) {
        StringBuilder sb = new StringBuilder(animalFact);

        if (animalFact.startsWith("can ")) { // can say meow -> can IT say meow
            sb.insert("can ".length(), "it ");
        } else if (animalFact.startsWith("has ")) { // has stripes -> DOES IT HAVE stripes
            sb.replace(0, "has ".length(), "does it have ");
        } else if (animalFact.startsWith("is ")) { // is ferocious - is IT ferocious
            sb.insert("is ".length(), "it ");
        }

        if (animalFact.endsWith(".")) {
            sb.replace(sb.length() - 1, sb.length(), "?");
        } else {
            sb.append("?");
        }
        sb.replace(0,1, sb.substring(0,1).toUpperCase());
        return  sb.toString();
    }
}
