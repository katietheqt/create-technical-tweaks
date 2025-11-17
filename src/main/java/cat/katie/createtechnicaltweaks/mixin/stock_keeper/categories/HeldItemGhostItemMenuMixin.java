package cat.katie.createtechnicaltweaks.mixin.stock_keeper.categories;

import cat.katie.createtechnicaltweaks.duck.IFilterScreenOrMenu;
import com.simibubi.create.foundation.gui.menu.HeldItemGhostItemMenu;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeldItemGhostItemMenu.class)
public class HeldItemGhostItemMenuMixin {
    @Inject(
            method = "stillValid",
            at = @At("HEAD"),
            cancellable = true
    )
    private void virtualAlwaysValid(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof IFilterScreenOrMenu virt && virt.ctt$isVirtual()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "isInSlot",
            at = @At("HEAD"),
            cancellable = true
    )
    private void virtualNeverInSlot(int index, CallbackInfoReturnable<Boolean> cir) {
        if (this instanceof IFilterScreenOrMenu virt && virt.ctt$isVirtual()) {
            cir.setReturnValue(false);
        }
    }
}
