package animals.entity;

import animals.helper.PersistenceFormat;
import animals.helper.TreeHelpers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;

import static animals.helper.CentralLogger.logger;

public class KnowledgeTree {
    private KnowledgeTreeNode root;
    private KnowledgeTreeNode currentNode;
    private KnowledgeTreeNode previousNode;

    public KnowledgeTree(KnowledgeTreeNode rootNode) {
        root = rootNode;
        currentNode = rootNode;
        logger.log(Level.FINE, "## DIAG ## Knowledge Tree created, currently holds no nodes: incorrect use will result in NPE!");
    }

    public KnowledgeTree() {} // used for deserialization workflow only

    public boolean isEmpty() {
        return root == null;
    }

    public KnowledgeTreeNode getCurrentNode() {
        return currentNode;
    }

    public KnowledgeTreeNode getPreviousNode() {
        return previousNode;
    }

    public KnowledgeTreeNode getRoot() {
        return root;
    }

    public void offerLeft(KnowledgeTreeNode knowledgeTreeNode) {
        if (previousNode == null) {
            root = knowledgeTreeNode;
            currentNode = knowledgeTreeNode;
            previousNode = currentNode;
        } else {
            previousNode.setLeftChild(knowledgeTreeNode);
            previousNode = currentNode;
        }
    }

    public void offerRight(KnowledgeTreeNode knowledgeTreeNode) {
        if (previousNode == null) {
            root = knowledgeTreeNode;
            currentNode = knowledgeTreeNode;
            previousNode = currentNode;
        } else {
            previousNode.setRightChild(knowledgeTreeNode);
            previousNode = currentNode;
        }
    }

    public void resetCurrentNodeToRoot() {
        currentNode = root;
        previousNode = null;
        logger.log(Level.FINE, "## DIAG ## Knowledge tree was reset to root.");
    }

    public void moveLeft() {
        previousNode = currentNode;
        logger.log(Level.FINE, "## DIAG ## Moving to the left node: " + currentNode.getLeftChild());
        currentNode = currentNode.getLeftChild();
    }

    public void moveRight() {
        previousNode = currentNode;
        logger.log(Level.FINE, "## DIAG ## Moving to the right node: " + currentNode.getRightChild());
        currentNode = currentNode.getRightChild();
    }

    public void serialize(PersistenceFormat persistenceFormat) {
        String internationalSuffix = System.getProperty("user.language").equals("eo") ? "_eo" : "";
        String fileName = "animals" + internationalSuffix + "." + persistenceFormat.name().toLowerCase();
        ObjectMapper objectMapper = new JsonMapper();

        switch (persistenceFormat) {
            case YAML -> objectMapper = new YAMLMapper();
            case XML -> objectMapper = new XmlMapper();
        }

        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean doesExistSerialized(PersistenceFormat persistenceFormat) {
        String internationalSuffix = System.getProperty("user.language").equals("eo") ? "_eo" : "";
        Path dataFilePath = Paths.get("animals" + internationalSuffix + "."
                + persistenceFormat.name().toLowerCase());
        return Files.exists(dataFilePath);
    }

    public static KnowledgeTree constructFromSerialized(PersistenceFormat persistenceFormat) {
        String internationalSuffix = System.getProperty("user.language").equals("eo") ? "_eo" : "";
        String fileName = "animals" + internationalSuffix + "." + persistenceFormat.name().toLowerCase();
        ObjectMapper objectMapper = new JsonMapper();

        switch (persistenceFormat) {
            case YAML -> objectMapper = new YAMLMapper();
            case XML -> objectMapper = new XmlMapper();
        }

        try {
            KnowledgeTree knowledgeTree = new KnowledgeTree();
            knowledgeTree.root = objectMapper.readValue(new File(fileName), KnowledgeTreeNode.class);
            knowledgeTree.currentNode = knowledgeTree.root;
            knowledgeTree.previousNode = null;
            return knowledgeTree;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRoot(KnowledgeTreeNode root) {
        this.root = root;
    }

    public void setCurrentNode(KnowledgeTreeNode currentNode) {
        this.currentNode = currentNode;
    }

    public void setPreviousNode(KnowledgeTreeNode previousNode) {
        this.previousNode = previousNode;
    }

    @Override
    public String toString() {
        return "KnowledgeTree{" +
                "root=" + root +
                ", currentNode=" + currentNode +
                ", previousNode=" + previousNode +
                '}';
    }

    public List<String> getDataFromAllLeaves() {
        List<String> dataFromLeaves = new ArrayList<>();
        TreeHelpers.collectLeafData(root, dataFromLeaves);
        return dataFromLeaves;
    }

    public List<String> findAnimal(String animalName) {
        Deque<String> pathToAnimal = new ArrayDeque<>();
        TreeHelpers.getPathToLeaf(animalName, root, pathToAnimal);
        if (!pathToAnimal.isEmpty()) {
            pathToAnimal.pop(); // needed to remove the terminator used by the recursive getPathToLeaf
        }
        return pathToAnimal.stream().toList();
    }

}
