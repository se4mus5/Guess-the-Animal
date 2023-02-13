package animals.entity;

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

    public boolean isEmpty() {
        return root == null;
    }

    public KnowledgeTreeNode getCurrentNode() {
        return currentNode;
    }

    public KnowledgeTreeNode getPreviousNode() {
        return previousNode;
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
}
