package com.learn;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by teemper on 2017/12/10, 20:11.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public class ThreadPoolTimeServer {
    public static void main(String[] args) {
        int port = 8090;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            Socket socket = null;
            ThreadPoolExecutorHandler handler = new ThreadPoolExecutorHandler(50,10000);
            while(true){
                socket = serverSocket.accept();
                handler.execute(new TimeServerHandler(socket));
                //new Thread(new TimeServerHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
