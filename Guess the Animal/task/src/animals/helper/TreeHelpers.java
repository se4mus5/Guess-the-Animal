package animals.helper;

import animals.entity.KnowledgeTreeNode;

public class TreeHelpers {
    public static void traversePreOrder(KnowledgeTreeNode knowledgeTreeNode) {
        System.out.println(knowledgeTreeNode.getData());
        if (knowledgeTreeNode.getLeftChild() != null) {
            traversePreOrder(knowledgeTreeNode.getLeftChild());
        }
        if (knowledgeTreeNode.getRightChild() != null) {
            traversePreOrder(knowledgeTreeNode.getRightChild());
        }
    }
}
