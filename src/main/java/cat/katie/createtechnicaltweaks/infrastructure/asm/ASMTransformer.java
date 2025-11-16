package cat.katie.createtechnicaltweaks.infrastructure.asm;

import org.objectweb.asm.tree.ClassNode;

public interface ASMTransformer {
    default void preTransform(ClassNode clazz) {}
    default void postTransform(ClassNode clazz) {}
}
