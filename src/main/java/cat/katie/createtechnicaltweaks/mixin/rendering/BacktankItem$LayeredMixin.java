package cat.katie.createtechnicaltweaks.mixin.rendering;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.content.equipment.armor.BacktankItem;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BacktankItem.Layered.class)
public class BacktankItem$LayeredMixin {
    @ModifyExpressionValue(
            method = "getArmorTextureLocation",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/content/equipment/armor/BacktankItem$Layered;textureLoc:Lnet/minecraft/resources/ResourceLocation;",
                    opcode = Opcodes.GETFIELD
            )
    )
    private ResourceLocation disableBacktankTexture(ResourceLocation original) {
        String originalString = original.toString();

        if (!AllConfigs.client().backtankRendering.get() && originalString.equals("create:netherite_diving")) {
            return ResourceLocation.parse("minecraft:netherite");
        }

        return original;
    }
}
