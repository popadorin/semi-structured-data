package com.dorin.pad.lab2.nodes;

import com.dorin.pad.lab2.MultiCastReceiver;
import com.dorin.pad.lab2.models.Employee;
import org.apache.log4j.Logger;

import java.io.IOException;

public class Node2 {
    private final static Logger LOGGER = Logger.getLogger(Node1.class);

    public static void main(String[] args) {
        try {
            MultiCastReceiver multiCastReceiver = new MultiCastReceiver(8882,
                    new Employee("dorin", "Popa", 10000));

            multiCastReceiver.listen();
        } catch (IOException e) {
            LOGGER.error("IO Exception on multicast receiver");
        }

    }
}
