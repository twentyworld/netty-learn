package com.aio.server;

/**
 * Created by teemper on 2018/2/26, 21:33.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public class Server {

    private static AsyncServerHandler serverHandle;
    volatile static long clientCount = 0;
    private static void start(){
        start(12345);
    }
    private static synchronized void start(int port){
        if(serverHandle!=null)
            return;
        serverHandle = new AsyncServerHandler(port);
        new Thread(serverHandle,"Server").start();
    }
    public static void main(String[] args){
        Server.start();
    }

}
