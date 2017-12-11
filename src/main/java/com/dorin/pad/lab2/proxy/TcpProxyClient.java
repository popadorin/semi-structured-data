package com.dorin.pad.lab2.proxy;

import java.io.IOException;

public interface TcpProxyClient {
    byte[] readFromClient() throws IOException, ClassNotFoundException;
    void sendToClient(byte[] object) throws IOException;
    void close();
}
