package animals.helper;

public class Transformer {
    /**
     * This method transforms a statement about a property of an animal into a question
     * @param animalFact Statement about a properties of an animal, expected format: as printed by KnowledgeTreeNode.getFact(true)
     * @return Animal fact statement rephrased as question
     */
    // TODO needs locale-dependent behavior
    public static String transformAnimalFactStatementIntoQuestion(String animalFact) {
        StringBuilder sb = new StringBuilder(animalFact);

        if (System.getProperty("user.language").equals("eo")) { // TODO if works, simplify
            if (animalFact.startsWith("povas ")) { // can say meow -> can IT say meow
                sb.insert(0, "ĉu ĝi ");
            } else if (animalFact.startsWith("havas ")) { // has stripes -> DOES IT HAVE stripes
                sb.insert(0,"ĉu ĝi ");
            } else if (animalFact.startsWith("estas ")) { // is ferocious - is IT ferocious
                sb.insert(0,"ĉu ĝi ");
            }

            if (animalFact.endsWith(".")) {
                sb.replace(sb.length() - 1, sb.length(), "?");
            } else {
                sb.append("?");
            }
            sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
            return sb.toString();
        } else {
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
            sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
            return sb.toString();
        }
    }
}
