package com.dorin.pad.lab2.nodes;

import com.dorin.pad.lab2.MultiCastReceiver;
import com.dorin.pad.lab2.models.Employee;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketException;

public class Node1 {
    private final static Logger LOGGER = Logger.getLogger(Node1.class);

    public static void main(String[] args) {
        try {
            MultiCastReceiver multiCastReceiver = new MultiCastReceiver(8881,
                    new Employee("dragos", "lupei", 80000));

            multiCastReceiver.listen();
        } catch (SocketException e) {
            LOGGER.error("IO Exception on multicast receiver");
        } catch (IOException e) {
            LOGGER.error("IOException occurred on listen");
        }

    }
}
