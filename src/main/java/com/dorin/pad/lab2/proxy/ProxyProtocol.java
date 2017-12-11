package com.dorin.pad.lab2.proxy;

import com.dorin.pad.lab2.configurations.Configurations;
import com.dorin.pad.lab2.helper.NodesInfoHelper;
import com.dorin.pad.lab2.models.Employee;
import com.dorin.pad.lab2.models.NodeInfo;
import com.dorin.pad.lab2.models.ProxyCommand;
import com.dorin.pad.lab2.proxy.MulticastProxy;
import com.dorin.pad.lab2.proxy.TcpProxyNode;
import com.dorin.pad.lab2.proxy.TcpProxyNodeImpl;
import com.dorin.pad.lab2.proxy.UnicastProxy;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class ProxyProtocol {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private final Gson gson = new Gson();

    public Employee getEmployee(MulticastProxy multicastProxy, UnicastProxy unicastProxy)
            throws IOException, ClassNotFoundException {

        byte[] message = ProxyCommand.GIVE_META_INFO.name().getBytes();
        multicastProxy.sendToNodes(message);

        List<NodeInfo> nodeInfos = unicastProxy.readFromNodes(Configurations.timeoutForNodeDiscovery);
        nodeInfos.forEach(System.out::println);

        NodesInfoHelper helper = new NodesInfoHelper();
        // find the node with most valuable metaInfo (most numberofconnections);
        NodeInfo maven = helper.getMostValuableNode(nodeInfos);
        LOGGER.info("Maven: " + maven);
        // make TCP connection with that node
        unicastProxy.sendToNode(ProxyCommand.YOU_ARE_MAVEN, maven.getAddress(), maven.getPort());

        TcpProxyNode proxyNode = new TcpProxyNodeImpl("localhost", Configurations.nodeTcpPort);

        // say to that node to GIVE_DATA
        proxyNode.sendToNode(ProxyCommand.GIVE_DATA.name().getBytes());

        // get from from node tcp
        byte[] fromNode = proxyNode.readFromNode();
        Employee employee = gson.fromJson(new String(fromNode), Employee.class);

        // send that data to client
        return employee;
    }
}
