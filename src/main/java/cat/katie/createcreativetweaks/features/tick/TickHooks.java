package cat.katie.createcreativetweaks.features.tick;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class TickHooks {
    private static final String TICK_RATE_MANAGER = Type.getInternalName(TickRateManager.class);

    /**
     * Inserts a jump that will be taken if the server is frozen this tick.
     *
     * @param it the iterator to insert to
     * @return the label that will be jumped to
     */
    public static LabelNode insertSkipTickJump(ListIterator<AbstractInsnNode> it) {
        return insertTickStateJump(it, Opcodes.IFEQ);
    }

    /**
     * Inserts a jump that will be taken if the server is running normally.
     *
     * @param it the iterator to insert to
     * @return the label that will be jumped to
     */
    public static LabelNode insertRunningNormallyJump(ListIterator<AbstractInsnNode> it) {
        return insertTickStateJump(it, Opcodes.IFNE);
    }

    private static LabelNode insertTickStateJump(ListIterator<AbstractInsnNode> it, int jumpOpcode) {
        LabelNode label = new LabelNode();
        it.add(new FieldInsnNode(Opcodes.GETSTATIC, TICK_RATE_MANAGER, "INSTANCE", "L" + TICK_RATE_MANAGER + ";"));
        it.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, TICK_RATE_MANAGER, "runsNormally", "()Z"));
        it.add(new JumpInsnNode(Opcodes.IFEQ, label));

        return label;
    }
}
