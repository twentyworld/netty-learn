package com.server;

/**
 * Created by teemper on 2018/2/24, 16:53.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;

        MultipleserTimeServer timeServer = new MultipleserTimeServer(port);

        new Thread(timeServer, "NIO-multiplexer").start();
    }
}
