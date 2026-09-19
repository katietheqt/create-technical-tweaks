package cat.katie.createtechnicaltweaks.mixin.limits;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import cat.katie.createtechnicaltweaks.infrastructure.config.CClient;
import net.minecraft.nbt.NbtAccounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(NbtAccounter.class)
public class NbtAccounterMixin {
    @ModifyConstant(
            method = {"create","unlimitedHeap"},
            constant = @Constant(intValue = 512)
    )
    private static int increaseLimit(int constant) {
        CClient config = AllConfigs.client();
        if (config != null && config.specification.isLoaded() && AllConfigs.client().disableNbtDepthLimit.get()) {
            return Integer.MAX_VALUE;
        }

        return constant;
    }
}
