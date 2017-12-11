package com.dorin.pad.lab2.nodes;

import com.dorin.pad.lab2.models.Employee;
import com.dorin.pad.lab2.models.MetaInformation;
import com.dorin.pad.lab2.models.ProxyCommand;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;

public class Node {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private Employee employee;
    private UnicastNode unicastNode;
    private MulticastNodeImpl multicastNode;
    private MetaInformation metaInformation;

    public Node(int unicastPort, MetaInformation metaInformation, Employee employee) throws IOException {
        unicastNode = new UnicastNode(unicastPort);

        // configuration
        int mcPort = 12345;
        String mcIPStr = "230.1.1.1";
        multicastNode = new MulticastNodeImpl(mcIPStr, mcPort);

        this.employee = employee;
        this.metaInformation = metaInformation;
    }

    public void run() throws IOException {
        boolean isStopped = false;
        while (!isStopped) {
            unicastNode.setAddress(multicastNode.getProxyAddress());
            unicastNode.setPort(multicastNode.getProxyPort());

            ProxyCommand commandFromProxy  = multicastNode.readFromProxy();
            switch (commandFromProxy) {
                case GIVE_META_INFO:
                    unicastNode.sendToProxy(metaInformation);
                    break;
                case YOU_ARE_MAVEN:
                    multicastNode.close();
                    // open TCP connection
                    break;
                case GIVE_DATA:
                    // send data throw TCP_Node connection
                    break;
                case EXIT:
                    isStopped = true;
                    multicastNode.close();
                    break;
                default:
                    LOGGER.error("No such command from proxy");
                    break;
            }

        }
    }


}
