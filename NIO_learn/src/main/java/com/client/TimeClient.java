package com.client;

import java.io.IOException;

/**
 * Created by teemper on 2018/2/24, 17:46.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        try {
            new Thread(new TimeClientHandler("127.0.0.1",port),"TimeClient").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
