package netty;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * donnie4w <donnie4w@gmail.com>
 * https://github.com/donnie4w/jvmtut
 * <p>
 * 聚集写是将多个缓冲区的数据写入到一个通道中
 */
public class GatheringWriteExample {
    public static void main(String[] args) throws Exception {
        RandomAccessFile fromFile = new RandomAccessFile("./jvmtut/test1.txt", "r");
        FileChannel fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("./jvmtut/test2.txt", "rw");
        FileChannel toChannel = toFile.getChannel();

        ByteBuffer body1 = ByteBuffer.allocate(10);
        ByteBuffer body2 = ByteBuffer.allocate(20);

        ByteBuffer[] buffers = {body1, body2};
        long bytesRead = fromChannel.read(buffers);
        System.out.println("Bytes read: " + bytesRead);
        for (ByteBuffer buffer : buffers) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            buffer.flip(); //将position（当前写入位置或读取位置）设置为0,从缓冲区的开始位置读取所有之前写入的数据
        }

        toChannel.write(buffers);
        // 清理资源
        fromChannel.close();
        toChannel.close();
        fromFile.close();
        toFile.close();
    }
}