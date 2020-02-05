package ru.n1ppl3.netty.examples;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;


class Example1 {

    static InetSocketAddress cassandra = new InetSocketAddress("localhost", 9042);


    @Test
    void example1() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        // 3 обязательных поля
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {

            }
        });

        ChannelFuture channelFuture = bootstrap.connect(cassandra);
        System.out.println(channelFuture);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println(channelFuture + " Successfully connected!");
                } else {
                    channelFuture.cause().printStackTrace();
                }
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        System.out.println(channelFuture);
    }
}
