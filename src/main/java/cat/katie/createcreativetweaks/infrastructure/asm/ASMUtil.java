package cat.katie.createcreativetweaks.infrastructure.asm;

import cpw.mods.modlauncher.api.INameMappingService;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

import java.util.ListIterator;
import java.util.function.Predicate;

public class ASMUtil {
    /**
     * Seeks forwards in an iterator until a predicate passes. The cursor is left on the instruction after the one that
     * was found (such that {@code next()} returns the next instruction).
     *
     * @param it the iterator to seek
     * @param name a name used to construct an error if the matcher fails
     * @param predicate the predicate to check
     * @return the instruction that was matched
     */
    public static AbstractInsnNode seekForward(ListIterator<AbstractInsnNode> it, String name, Predicate<AbstractInsnNode> predicate) {
        while (it.hasNext()) {
            AbstractInsnNode insn = it.next();

            if (predicate.test(insn)) {
                return insn;
            }
        }

        throw new IllegalStateException("failed to find " + name);
    }

    public static AbstractInsnNode seekLdc(ListIterator<AbstractInsnNode> it, Object cst) {
        return seekForward(it, "ldc '" + cst + "'", insn -> insn instanceof LdcInsnNode ldc && ldc.cst.equals(cst));
    }

    public static AbstractInsnNode seekBackward(ListIterator<AbstractInsnNode> it, String name, Predicate<AbstractInsnNode> predicate) {
        while (it.hasPrevious()) {
            AbstractInsnNode insn = it.previous();

            if (predicate.test(insn)) {
                return insn;
            }
        }

        throw new IllegalStateException("failed to find " + name);
    }

    public static AbstractInsnNode seekLdcBackward(ListIterator<AbstractInsnNode> it, Object cst) {
        return seekBackward(it, "ldc '" + cst + "'", insn -> insn instanceof LdcInsnNode ldc && ldc.cst.equals(cst));
    }

    public static String mapMethod(int srgId) {
        String obfuscated = "m_" + srgId + "_";
        return ObfuscationReflectionHelper.remapName(INameMappingService.Domain.METHOD, obfuscated);
    }
}
