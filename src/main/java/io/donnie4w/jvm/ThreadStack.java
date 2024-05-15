package io.donnie4w.jvm;


/**
 *  donnie4w <donnie4w@gmail.com>
 *  https://github.com/donnie4w/jvmtut
 *
 * -Xss 或 -XX:ThreadStackSize 参数不是用来设置堆内存分配的，而是用来设置每个线程的栈大小。
 * 栈内存主要用于存储局部变量、方法调用信息等，是线程私有的内存区域
 *
 *  java -Xss88k   io.donnie4w.jvm.ThreadStack
 *
 */

public class ThreadStack {

    public static void main(String[] args) throws InterruptedException {
        // 设置不同的线程栈大小进行测试
        testStackSize("Default Stack Size", -1); // 使用默认栈大小
        testStackSize("Large Stack Size", 1024*1024); // 设置较大的栈大小
        Thread.sleep(10000);
    }

    private static void testStackSize(String testName, int stackSizeInKb) {
        Thread thread = null;
        try {
            if (stackSizeInKb > 0) {
                // 使用自定义的栈大小创建线程
                thread = new Thread(null, () -> performDeepRecursion(), testName, stackSizeInKb);
            } else {
                // 使用默认栈大小创建线程
                thread = new Thread(() -> performDeepRecursion(), testName);
            }
            thread.start();
            thread.join(); // 等待线程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void performDeepRecursion() {
        try {
            System.out.println(Thread.currentThread().getName() + " starts with stack size of " + Thread.currentThread().getStackTrace().length + " frames.");
            performDeepRecursion(); // 递归调用
        } catch (StackOverflowError soe) {
            System.err.println(Thread.currentThread().getName() + " encountered StackOverflowError at recursion depth of " + Thread.currentThread().getStackTrace().length);
        }
    }
}