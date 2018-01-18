package com;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by teemper on 2018/1/18, 22:52.
 *
 * @auther Zed.
 * copy as you like, but with these words.
 * from win.
 */
public class DiscardServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
//        // Discard the received data silently.
//        ((ByteBuf) msg).release(); // (3)

        ByteBuf byteBuf = (ByteBuf) msg;
        while(byteBuf.isReadable()) {
            System.out.println(byteBuf.readByte());
            System.out.flush();
        }

        ReferenceCountUtil.release(msg);


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
