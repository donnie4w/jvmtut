package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * donnie4w <donnie4w@gmail.com>
 * https://github.com/donnie4w/jvmtut
 *
 * 基于直接内存传输消息的客户端
 */
public class DirectMemoryClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new StringDecoder()); // 添加解码器
                            ch.pipeline().addLast(new ClientHandler()); // 添加自定义的客户端处理器
                        }
                    });

            ChannelFuture f = b.connect("localhost", 8080).sync();
            System.out.println("Connected to server.");

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    // 自定义客户端处理器
    static class ClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            // 连接建立后，发送消息
            ByteBuf directBuffer = PooledByteBufAllocator.DEFAULT.directBuffer();
            directBuffer.writeBytes("Hello, Server! This is a message from Direct Memory!".getBytes());
            ctx.writeAndFlush(directBuffer);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            String receivedMsg = (String) msg; // 假设消息为字符串类型
            System.out.println("Client received: " + receivedMsg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}