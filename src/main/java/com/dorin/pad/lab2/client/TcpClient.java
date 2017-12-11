package com.dorin.pad.lab2.client;

import java.io.IOException;

public interface TcpClient {
    void sendToProxy(byte[] obj) throws IOException;
    byte[] readFromProxy() throws IOException, ClassNotFoundException;
    void close() throws IOException;
}
