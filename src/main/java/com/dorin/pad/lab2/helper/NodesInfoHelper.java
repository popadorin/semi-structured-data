package com.dorin.pad.lab2.helper;

import com.dorin.pad.lab2.models.NodeInfo;

import java.util.List;

public class NodesInfoHelper {
    public NodeInfo getMostValuableNode(List<NodeInfo> nodeInfos) {
        NodeInfo mostValuableNode = nodeInfos.get(0);

        for (NodeInfo node : nodeInfos) {
            if (node.getMetaInformation().getNumberOfConnections() >
                    mostValuableNode.getMetaInformation().getNumberOfConnections()) {

                mostValuableNode = node;
            }
        }

        return mostValuableNode;
    }
}
