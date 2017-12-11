package com.dorin.pad.lab2.nodes;

import java.io.IOException;

public interface TcpNode {
    byte[] readFromProxy() throws IOException, ClassNotFoundException;
    void sendToProxy(byte[] object) throws IOException;
    void close();
}
