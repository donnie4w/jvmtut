package io.donnie4w.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * donnie4w <donnie4w@gmail.com>
 * https://github.com/donnie4w/jvmtut
 *
 * 基于直接内存传输消息的服务端
 */
public class DirectMemoryServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new ServerHandler()); // 添加自定义的业务处理器
                        }
                    });

            ChannelFuture f = b.bind(8080).sync();
            System.out.println("Server started on port 8080.");
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    // 自定义服务器端处理器
    static class ServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            String receivedMsg = (String) msg; // 假设消息为字符串类型
            System.out.println("Server received: " + receivedMsg);
            // 向客户端发送确认消息
            ByteBuf directBuffer = PooledByteBufAllocator.DEFAULT.directBuffer();
            directBuffer.writeBytes(("Server received your message: " + receivedMsg + "\n").getBytes());
            ctx.writeAndFlush(directBuffer);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}