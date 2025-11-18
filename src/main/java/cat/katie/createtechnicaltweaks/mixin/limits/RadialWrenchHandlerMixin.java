package cat.katie.createtechnicaltweaks.mixin.limits;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.wrench.RadialWrenchHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RadialWrenchHandler.class)
public class RadialWrenchHandlerMixin {
    @WrapOperation(
            method = "onKeyInput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;getMainHandItem()Lnet/minecraft/world/item/ItemStack;"
            )
    )
    private static ItemStack alwaysAllowUsingMenu(LocalPlayer instance, Operation<ItemStack> original) {
        if (AllConfigs.client().rotateWithoutWrench.get()) {
            return new ItemStack(AllItems.WRENCH.get());
        }

        return original.call(instance);
    }
}
