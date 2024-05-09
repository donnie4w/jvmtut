package io.donnie4w.jvm;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
/**
 *  donnie4w <donnie4w@gmail.com>
 *  https://github.com/donnie4w/jvmtut
 *
 *  java -XX:MetaspaceSize=50m -XX:MaxMetaspaceSize=50m  -classpath ./classes;./repository/org/ow2/asm/asm/9.4/asm-9.4.jar io.donnie4w.jvm.MaxMetaspace
 */
public class MaxMetaspace {
    public static void main(String[] args) throws Exception {
        try {
            List<ClassLoader> loaders = new ArrayList<>();
            int counter = 1;
            while (true) {
                loaders.add(DynamicClassLoader.ClassLoader("GeneratedClass"+(counter++))) ;
                Thread.sleep(1);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}

