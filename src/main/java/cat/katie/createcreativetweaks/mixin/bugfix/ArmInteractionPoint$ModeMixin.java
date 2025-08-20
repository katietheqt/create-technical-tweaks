package cat.katie.createcreativetweaks.mixin.bugfix;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = ArmInteractionPoint.Mode.class, remap = false)
public class ArmInteractionPoint$ModeMixin {
    // The language string for mechanical arm interaction points has an extra `create.` at the start
    @ModifyVariable(
            method = "<init>",
            at = @At("HEAD"),
            argsOnly = true,
            index = 3
    )
    private static String fixLanguageStringBug(String value) {
        return value.replaceFirst("^create\\.", "");
    }
}
