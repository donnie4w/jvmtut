package io.donnie4w.jvm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 *  donnie4w <donnie4w@gmail.com>
 *  https://github.com/donnie4w/jvmtut
 */
public class DynamicClassLoader {

    public static ClassLoader ClassLoader(String classname) throws ClassNotFoundException {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            protected Class<?> findClass(String name) {
                byte[] bytecode = generateClassBytes(classname);
                return defineClass(name, bytecode, 0, bytecode.length);
            }
        };
        classLoader.loadClass(classname);
        return classLoader;
    }

    private static byte[] generateClassBytes(String classname) {
        ClassWriter cw = new ClassWriter(0);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, classname, null, "java/lang/Object", null);
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        cw.visitEnd();
        return cw.toByteArray();
    }
}
