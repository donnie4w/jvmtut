package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

/**
 * donnie4w <donnie4w@gmail.com>
 * https://github.com/donnie4w/jvmtut
 *
 * CompositeByteBuf允许你将多个ByteBuf对象组合成一个逻辑上的单个缓冲区
 */

public class CompositeBufferExample {
    public static void main(String[] args) {
        // 创建两个ByteBuf
        ByteBuf buf1 = Unpooled.buffer(4);
        buf1.writeBytes(new byte[]{1, 2, 3, 4});

        ByteBuf buf2 = Unpooled.buffer(4);
        buf2.writeBytes(new byte[]{5, 6, 7, 8});

        // 创建CompositeByteBuf
        CompositeByteBuf composite = Unpooled.compositeBuffer();

        // 将两个ByteBuf添加到组合缓冲区
        composite.addComponents(true, buf1, buf2);

        // 打印组合缓冲区的数据
        printBuffer(composite);

        // 清理资源
        composite.release();
    }

    private static void printBuffer(ByteBuf buffer) {
        StringBuilder sb = new StringBuilder();
        for (int i = buffer.readerIndex(); i < buffer.writerIndex(); i++) {
            sb.append(buffer.getByte(i)).append(",");
        }
        System.out.println("Combined buffer as bytes: [" + sb.deleteCharAt(sb.length() - 1) + "]");
    }
}