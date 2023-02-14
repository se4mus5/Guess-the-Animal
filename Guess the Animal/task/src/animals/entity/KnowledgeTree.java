package animals.entity;

import animals.helper.PersistenceFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
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
        String fileName = "animals." + persistenceFormat.name().toLowerCase();
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

    public static KnowledgeTree constructFromSerialized(PersistenceFormat persistenceFormat) {
        String fileName = "animals." + persistenceFormat.name().toLowerCase();
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
}
