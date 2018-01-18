package com.learn;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by teemper on 2017/12/10, 19:33.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8090;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            Socket socket = null;
            while(true){
                socket = serverSocket.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
