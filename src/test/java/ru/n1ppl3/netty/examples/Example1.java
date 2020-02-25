package ru.n1ppl3.netty.examples;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;


class Example1 {
    private static final Logger logger = LoggerFactory.getLogger(Example1.class);

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
                logger.info("INIT_CHANNEL: {}", ch);
            }
        });

        ChannelFuture channelFuture = bootstrap.connect(cassandra);
        logger.info("1. {}", channelFuture);

        CountDownLatch countDownLatch = new CountDownLatch(1);

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) {
                if (channelFuture.isSuccess()) {
                    logger.info("{} Successfully connected!", channelFuture);
                } else {
                    channelFuture.cause().printStackTrace();
                }
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();
        logger.info("2. {}", channelFuture);
    }
}
