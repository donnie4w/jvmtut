package io.donnie4w.jvm;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


/***
 *  donnie4w <donnie4w@gmail.com>
 *  https://github.com/donnie4w/jvmtut
 *
 *  java -XX:MaxDirectMemorySize=10M  io.donnie4w.jvm.MaxDirectMemory
 */

public class MaxDirectMemory {

    private static final int ALLOCATION_SIZE = 1024 * 1024; // 每次分配1MB
    private static final AtomicLong totalAllocated = new AtomicLong(0); // 记录总分配量
    private static final Unsafe unsafe; // 声明Unsafe实例

    static {
        try {

            // 获取Unsafe类中的一个名为"theUnsafe"的私有静态字段
            // Unsafe类提供了对JVM底层操作的能力，如直接内存访问等，通常不建议直接使用
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");

            // 设置该字段为可访问，即使它是私有的
            theUnsafe.setAccessible(true);

            // 通过反射获取该字段的值，从而得到Unsafe类的一个实例
            // 这是获取Unsafe实例的标准（尽管是非官方推荐的）方式
            unsafe = (Unsafe) theUnsafe.get(null);

        } catch (Exception e) {
            // 如果在获取Unsafe实例过程中出现任何异常（如反射访问权限问题）
            // 抛出运行时异常，附带原始异常信息，以确保问题不会被静默忽略
            throw new RuntimeException("获取Unsafe实例失败", e);
        }
    }

    public static void main(String[] args) throws Exception {
        // 打印初始直接内存使用情况（这里仅为示意，实际直接内存使用量不易直接获取）
        printDirectMemoryUsage();

        try {
            // 尝试分配直到发生内存溢出
            List<ByteBuffer> list = new ArrayList<>();
            while (true) {
                ByteBuffer buffer = ByteBuffer.allocateDirect(ALLOCATION_SIZE);
                totalAllocated.addAndGet(ALLOCATION_SIZE);
                list.add(buffer);
            }
        } catch (OutOfMemoryError e) {
            System.err.println("直接内存分配达到限制，发生OutOfMemoryError.");
        }
        // 再次尝试打印直接内存使用情况
        printDirectMemoryUsage();
    }

    /**
     * 打印直接内存使用的示意方法。注意：此方法并不能真正反映出直接内存的使用情况，
     * 因为直接获取直接内存使用量在JVM中通常是不可行的。这里仅用于演示。
     */
    private static void printDirectMemoryUsage() {
        // 以下代码仅为示意，并不能准确反映直接内存使用量
        long directMemoryUsed = totalAllocated.get();
        System.out.printf("当前已记录直接内存分配量: %d MB%n", directMemoryUsed / (1024 * 1024));
    }
}