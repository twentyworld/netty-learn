package com.learn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by teemper on 2017/12/10, 19:52.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public class TimeClient {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        try {
            socket = new Socket("127.0.0.1",8091);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(),true);
            printWriter.println(System.currentTimeMillis());
            //System.out.println(bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            printWriter.close();
            socket.close();
        }
    }
}
