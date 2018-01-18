package com.learn;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Created by teemper on 2017/12/10, 19:40.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public class TimeServerHandler implements Runnable {
    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(),true);

            String line = null;
            while ((line=bufferedReader.readLine())!=null){
                //System.out.println("****************");
                System.out.println(line);

                String time = "QUERY TIME".equalsIgnoreCase(line)? String.valueOf(System.currentTimeMillis()) :"bad request";
                printWriter.println(time);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
