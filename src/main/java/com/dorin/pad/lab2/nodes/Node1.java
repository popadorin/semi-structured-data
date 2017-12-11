package com.dorin.pad.lab2.nodes;

import com.dorin.pad.lab2.models.Employee;
import com.dorin.pad.lab2.models.MetaInformation;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketException;

public class Node1 {
    private final static Logger LOGGER = Logger.getLogger(Node1.class);

    public static void main(String[] args) {
        try {
            Node node = new Node(8881,
                    new MetaInformation(3),
                    new Employee("dragos", "lupei", 80000));

            node.run();
        } catch (SocketException e) {
            LOGGER.error("Already used port");
        } catch (IOException e) {
            LOGGER.error("IOException occurred on listen");
        } catch (ClassNotFoundException e) {
            LOGGER.error("ClassNotFoundException on: " + e.getMessage());
        }

    }
}
