package com.dorin.pad.lab2.nodes;

import com.dorin.pad.lab2.models.Employee;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketException;

public class Node3 {
    private final static Logger LOGGER = Logger.getLogger(Node1.class);

    public static void main(String[] args) {
        try {
            MultiCastNode multiCastNode = new MultiCastNode(8883,
                    new Employee("vasile", "schidu", 70000));

            multiCastNode.listen();
        } catch (SocketException e) {
            LOGGER.error("Already used port");
        } catch (IOException e) {
            LOGGER.error("IOException occurred on listen");
        }
    }
}
