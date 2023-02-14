package animals.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;
import java.util.logging.Level;

import static animals.helper.CentralLogger.logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class KnowledgeTreeNode {
    private String data;
    private KnowledgeTreeNode leftChild;
    private KnowledgeTreeNode rightChild;

    public KnowledgeTreeNode(String data) {
        this.data = data;
        leftChild = null;
        rightChild = null;
        logger.log(Level.FINE, "## DIAG ## KnowledgeTreeNode created, data: " + data);
    }

    public KnowledgeTreeNode() {} // used for deserialization workflow only

    public KnowledgeTreeNode(String data, KnowledgeTreeNode leftChild, KnowledgeTreeNode rightChild) {
        this.data = data;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public String getData() {
        return data;
    }

    @JsonIgnore
    public boolean isLeaf() {
        return leftChild == null && rightChild == null;
    }

    @JsonIgnore
    public boolean isAnimal() {
        return isLeaf();
    }

    @JsonIgnore
    public boolean isFact() {
        return !isAnimal();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Node data : ").append(data);
        if (isFact()) {
            sb.append(", left node: ").append(getLeftChild().data);
            sb.append(", right node: ").append(getRightChild().data);
        }
        return sb.toString();
    }

    public KnowledgeTreeNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(KnowledgeTreeNode leftChild) {
        this.leftChild = leftChild;
        logger.log(Level.FINE, "## DIAG ## KnowledgeTreeNode left child set: " + leftChild.data);
    }

    public KnowledgeTreeNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(KnowledgeTreeNode rightChild) {
        this.rightChild = rightChild;
        logger.log(Level.FINE, "## DIAG ## KnowledgeTreeNode right child set: " + rightChild.data);
    }

    public static String determineArticle(String animalName) {
        Set<Character> VOWELS = Set.of('a', 'i', 'e', 'o', 'u');
        char firstCharOfAnimalName = animalName.charAt(0);
        return VOWELS.contains(firstCharOfAnimalName) ? "an" : "a";
    }

    @JsonIgnore
    public String getAnimalName() {
        if (isAnimal()) {
            if (data.startsWith("a ")) {
                return data.substring("a ".length());
            } else if (data.startsWith("an ")) {
                return data.substring("an ".length());
            } else if (data.startsWith("the ")) {
                return data.substring("the ".length());
            } else {
                return data;
            }
        } else {
            throw new RuntimeException("Not an animal node.");
        }
    }

    @JsonIgnore
    public String getAnimalNameWithArticle() {
        if (isAnimal()) {
            if (data.startsWith("a ") || data.startsWith("an ")) {
                return data;
            } else if (data.startsWith("the ")) {
                return String.format("%s %s", determineArticle(data.substring("the ".length())), data.substring("the ".length()));
            } else {
                return String.format("%s %s", determineArticle(data), data);
            }
        } else {
            throw new RuntimeException("Not an animal node.");
        }
    }

    @JsonIgnore
    public String getFact(boolean factApplies) {
        String tempData = String.valueOf(data);
        if (!isAnimal()) {
            if (!factApplies) {
                tempData = tempData.replace(" can ", " can't ");
                tempData = tempData.replace(" has ", " doesn't have ");
                tempData = tempData.replace(" is ", " isn't ");
            }
            tempData = tempData.replace("it ", "");
            tempData = tempData.replace("?", "");

            return tempData;
        } else {
            throw new RuntimeException("Not a fact node: " + this);
        }
    }
}
