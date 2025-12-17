package cat.katie.createtechnicaltweaks.mixin;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.content.equipment.armor.BaseArmorItem;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseArmorItem.class)
public class BaseArmorItemMixin {
    @ModifyExpressionValue(
            method = "getArmorTexture",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/simibubi/create/content/equipment/armor/BaseArmorItem;textureLoc:Lnet/minecraft/resources/ResourceLocation;",
                    opcode = Opcodes.GETFIELD
            )
    )
    private ResourceLocation disableScubaHelmetTexture(ResourceLocation original) {
        if (!AllConfigs.client().divingHelmetTexture.get()) {
            String originalString = original.toString();

            if (originalString.equals("create:netherite_diving")) {
                return ResourceLocation.parse("minecraft:netherite");
            }
        }

        return original;
    }
}
