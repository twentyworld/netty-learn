package com.learn;

import java.util.concurrent.*;

/**
 * Created by teemper on 2017/12/10, 20:18.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public class ThreadPoolExecutorHandler {

    private ExecutorService service;

    public ThreadPoolExecutorHandler(int maxPool, int queueSize) {
        service = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPool,120L, TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(queueSize));
    }
    public void execute(Runnable task){
        service.execute(task);
    }
}
