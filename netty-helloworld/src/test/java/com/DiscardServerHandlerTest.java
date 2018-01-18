package com; 

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* DiscardServerHandler Tester. 
* 
* @author <Authors name> 
* @since <pre>һ�� 18, 2018</pre> 
* @version 1.0 
*/ 
public class DiscardServerHandlerTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: channelRead(ChannelHandlerContext ctx, Object msg) 
* 
*/ 
@Test
public void testChannelRead() throws Exception { 

    new DiscurdServer(8080).run();
} 

/** 
* 
* Method: exceptionCaught(ChannelHandlerContext ctx, Throwable cause) 
* 
*/ 
@Test
public void testExceptionCaught() throws Exception { 
//TODO: Test goes here... 
} 


}

class DiscurdServer {
    private int port;

    public DiscurdServer(int port ){
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup,workGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new DiscardServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true);

        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        channelFuture.channel().closeFuture().sync();



        workGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();


    }








}
