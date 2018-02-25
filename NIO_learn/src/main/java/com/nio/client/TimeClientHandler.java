package com.nio.client;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by teemper on 2018/2/24, 17:48.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public class TimeClientHandler implements Runnable {
    
    Logger logger = Logger.getLogger(TimeClientHandler.class);
    
    
    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeClientHandler(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        stop = false;
    }
//
//    public void stop() {
//        stop = true;
//    }
//
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

        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

       logger.info("print");

        while(!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey selectionKey ;

                while(iterator.hasNext()){
                    selectionKey = iterator.next();
                    iterator.remove();
                    try{
                        handleInput(selectionKey);
                    }catch (IOException e){
                        e.printStackTrace();
                        if (selectionKey!=null){
                            selectionKey.cancel();
                            if (selectionKey.channel()!=null) {
                                selectionKey.channel().close();
                            }
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (selector!=null) {
            try{
                selector.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleInput(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()) {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

            if (selectionKey.isConnectable() && socketChannel.finishConnect()) {
                socketChannel.register(selector, SelectionKey.OP_READ);
                write(socketChannel);
            }
        }

        if (selectionKey.isReadable()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int readBytes = socketChannel.read(buffer);
            if (readBytes>0) {
                buffer.flip();
                byte[] context = new byte[buffer.remaining()];
                buffer.get(context);

               logger.info(new String(context,"UTF-8"));
            }
            else if (readBytes<0) {
                selectionKey.cancel();
                socketChannel.close();
            }


        }

    }


    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            write(socketChannel);
           logger.info("do connected and write.");
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
           logger.info("do connect.");
        }

    }

    private void write(SocketChannel socketChannel) throws IOException {
        byte[] context = "hello server".getBytes();

        ByteBuffer byteBuffer = ByteBuffer.allocate(context.length);
        byteBuffer.put(context);
        byteBuffer.flip();

        socketChannel.write(byteBuffer);

        if (!byteBuffer.hasRemaining())
           logger.info("send successful.");

    }
}
