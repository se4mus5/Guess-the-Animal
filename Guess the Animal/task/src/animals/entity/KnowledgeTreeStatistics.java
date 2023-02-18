package animals.entity;

import animals.helper.TreeHelpers;

import java.util.List;

import static animals.helper.TreeHelpers.countNodes;

public class KnowledgeTreeStatistics {
    private final String rootNodeData;
    private final int numOfNodes;
    private final int numOfAnimals;
    private final int height;
    private final int minDepth;
    private final double avgDepth;

    public KnowledgeTreeStatistics(KnowledgeTreeNode root) {
        rootNodeData = root.getData();
        numOfNodes = countNodes(root);
        numOfAnimals = countNodes(root, true);
        List<Integer> depthOfTreeAtEachLeaf = TreeHelpers.depthOfTreeAtEachLeaf(root)
                .stream()
                .sorted()
                .toList();
        height = depthOfTreeAtEachLeaf.get(depthOfTreeAtEachLeaf.size() - 1); // max = last after above sort
        minDepth = depthOfTreeAtEachLeaf.get(0);
        avgDepth = depthOfTreeAtEachLeaf.stream().mapToInt(Integer::valueOf).average().getAsDouble();
    }

    public String getRootNodeData() {
        return rootNodeData;
    }

    public int getNumOfNodes() {
        return numOfNodes;
    }

    public int getNumOfAnimals() {
        return numOfAnimals;
    }

    public int getNumOfStatements() {
        return numOfNodes - numOfAnimals;
    }

    public int getHeight() {
        return height;
    }

    public int getMinDepth() {
        return minDepth;
    }

    public double getAvgDepth() {
        return avgDepth;
    }
}
