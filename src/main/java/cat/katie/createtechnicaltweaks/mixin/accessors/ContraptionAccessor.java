package cat.katie.createtechnicaltweaks.mixin.accessors;

import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = Contraption.class, remap = false)
public interface ContraptionAccessor {
    @Invoker("toLocalPos")
    BlockPos ctt$toLocalPos(BlockPos globalPos);
}
