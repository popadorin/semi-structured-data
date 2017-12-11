package com.dorin.pad.lab2.proxy;

import java.io.IOException;

public interface TcpProxyNode {
    void sendToNode(byte[] obj) throws IOException;
    byte[] readFromNode() throws IOException, ClassNotFoundException;
    void close() throws IOException;
}
