package netty;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * donnie4w <donnie4w@gmail.com>
 * https://github.com/donnie4w/jvmtut
 *
 *  读取数据到多个缓冲区
 */
public class ScatteringReadExample {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("./jvmtut/test1.txt", "r");
        FileChannel channel = file.getChannel();

        ByteBuffer body1 = ByteBuffer.allocate(10);
        ByteBuffer body2 = ByteBuffer.allocate(20);

        ByteBuffer[] buffers = {body1, body2};

        long bytesRead = channel.read(buffers);
        System.out.println("Bytes read: " + bytesRead);

        for (ByteBuffer buffer : buffers) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
        }

        // 清理资源
        channel.close();
        file.close();
    }
}