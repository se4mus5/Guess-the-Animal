package animals.helper;

import animals.entity.KnowledgeTreeNode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Level;

import static animals.helper.CentralLogger.logger;
import static animals.helper.Transformer.transformAnimalFactStatementIntoQuestion;

public class TreeHelpers {

    /**
     * Collect data stored in leaf KnowledgeTreeNodes only. Implementation is recursive and directly mutates the collector data structure.
     * @param node KnowledgeTreeNode root of subtree to begin the search on.
     * @param collector List used to collect the data from leaf KnowledgeTreeNodes.
     */
    public static void collectLeafData(KnowledgeTreeNode node, List<String> collector) {
        if(node.isAnimal()) {
            collector.add(node.getAnimalName());
        }
        if (node.getLeftChild() != null) {
            collectLeafData(node.getLeftChild(), collector);
        }
        if (node.getRightChild() != null) {
            collectLeafData(node.getRightChild(), collector);
        }
    }

    /**
     * Get path to a leaf identified by its leaf data. Implementation is recursive and directly mutates the collector data structure.
     * @param leafData Data stored in the leaf this method is searching for.
     * @param node KnowledgeTreeNode root of subtree to begin the search on.
     * @param collector Stack used to collect the data from KnowledgeTreeNodes on the path to the KnowledgeTreeNode holding leafData.
     */
    public static void getPathToLeaf(String leafData, KnowledgeTreeNode node, Deque<String> collector) { // use a stack for keeping track of path
        logger.log(Level.FINE, String.format("## DIAG ## Pathfinding - visiting KnowledgeTreeNode : %s\n", node.getData()));

        if (node.isAnimal()) {
            if (node.getAnimalName().equals(leafData)) {
                // if the desired leafData is found, add it as terminator to prevent further modifications (remove in the caller)
                collector.push(leafData);
                logger.log(Level.FINE, String.format("## DIAG ## Pathfinding - desired leaf found : %s\n", leafData));
            }
        }

        if (node.getLeftChild() != null) {
            collector.push(node.getFact(false)); // left/right child of branches determines whether fact applies
            getPathToLeaf(leafData, node.getLeftChild(), collector);
        }
        if (node.getRightChild() != null) {
            collector.push(node.getFact(true));
            getPathToLeaf(leafData, node.getRightChild(), collector);
        }

        if (!collector.peek().equals(leafData)) { // if stack has leafData on top, it becomes immutable
            // if desired leafData not found in branch, pop on the way back to remove irrelevant part of path
            String pathSection = collector.pop();
            logger.log(Level.FINE, String.format("## DIAG ## Pathfinding - removing irrelevant branch section :%s\n", pathSection));
        }
    }

    public static void traversePreorder(KnowledgeTreeNode node, List<String> collector, int level) {
        String padding = " ".repeat(level);
        if (node.isFact()) {
            collector.add(padding + transformAnimalFactStatementIntoQuestion(node.getFact(true)));
        } else {
            collector.add(padding + node.getAnimalNameWithArticle());
        }

        level++;
        if (node.getRightChild() != null) {
            traversePreorder(node.getRightChild(), collector, level);
        }

        if (node.getLeftChild() != null) {
            traversePreorder(node.getLeftChild(), collector, level);
        }
    }

    // minimalistic implementation of knowledge tree stats using recursion
    // low-pri refactor: implement in KnowledgeTree, use counter variables + encapsulate internals
    public static int countNodes(KnowledgeTreeNode node) {
        return countNodes(node, false);
    }

    /**
     * Counts all nodes of a KnowledgeTree, including root node.
     * @param node Root of the KnowledgeKneeNode.
     * @param leavesOnly If true, only leaf nodes will be counted.
     * @return int representing node count
     */
    public static int countNodes(KnowledgeTreeNode node, boolean leavesOnly) {
        if (node == null) {
            return 0;
        }

        int nodeCountingValue = 1;
        if (leavesOnly) {
            if (!node.isLeaf()) {
                nodeCountingValue = 0;
            }
        }

        return nodeCountingValue + countNodes(node.getLeftChild(), leavesOnly) + countNodes(node.getRightChild(), leavesOnly);
    }

    public static List<Integer> depthOfTreeAtEachLeaf(KnowledgeTreeNode node) {
        List<Integer> depthOfTreeAtEachLeaf = new ArrayList<>();
        depthOfTreeAtEachLeafHelper(node, depthOfTreeAtEachLeaf, 0);
        return depthOfTreeAtEachLeaf;
    }

    public static int depthOfTreeAtEachLeafHelper(KnowledgeTreeNode node, List<Integer> depthOfTreeAtEachLeaf, int currentDepth) {
        if (node == null) {
            return 0;
        }

        if (node.isLeaf()) {
            depthOfTreeAtEachLeaf.add(currentDepth);
            return currentDepth;
        } else {
            return depthOfTreeAtEachLeafHelper(node.getLeftChild(), depthOfTreeAtEachLeaf, currentDepth + 1)
                    + depthOfTreeAtEachLeafHelper(node.getRightChild(), depthOfTreeAtEachLeaf, currentDepth + 1);
        }
    }
}
