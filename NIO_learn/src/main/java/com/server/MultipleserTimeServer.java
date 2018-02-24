package com.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by teemper on 2018/2/24, 16:55.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public class MultipleserTimeServer implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;

    public MultipleserTimeServer(int port) {
        try {
            selector = Selector.open();
            //打开，用于监听客户端的连接，是所有客户端连接的父Channel
            serverSocketChannel = ServerSocketChannel.open();
            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            //绑定监听端口
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            //将ServerSocketChannel注册到Selector上，监听Accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println(port);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void stop() {
        this.stop = true;
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
        System.out.println("client coming");
        //多路复用器在线程run方法中无线轮询准备就绪的key
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;

                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try{
                        handleInput(key);

                    }catch (Exception e) {
                        if (key!=null) {
                            key.cancel();
                            if (key.channel()!=null)
                                key.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleInput(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()) {
            if (selectionKey.isAcceptable()) {
                System.out.println("connect");
                //多路复用器监听到新的客户端的接入，处理请求
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);


                System.out.println(selectionKey.isReadable());
                if (selectionKey.isReadable()) {
                    SocketChannel socketChannel1 = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int readBuffer = socketChannel1.read(buffer);

                    System.out.println("read context");
                    if (readBuffer > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        String context = new String(bytes, "UTF-8");
                        System.out.println(context);
                        System.out.println(System.currentTimeMillis());
                        write(socketChannel1, System.currentTimeMillis() + "");
                    } else if (readBuffer < 0) {
                        selectionKey.cancel();
                        socketChannel1.close();
                    }
                }
            }
        }
    }


    private void write(SocketChannel socketChannel, String response) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(response.length());
        buffer.put(response.getBytes());
        buffer.flip();
        socketChannel.write(buffer);

    }
}
