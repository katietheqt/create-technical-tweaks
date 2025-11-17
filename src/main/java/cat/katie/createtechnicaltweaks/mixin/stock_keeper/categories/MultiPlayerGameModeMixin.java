package cat.katie.createtechnicaltweaks.mixin.stock_keeper.categories;

import cat.katie.createtechnicaltweaks.duck.IFilterScreenOrMenu;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @Inject(
            method = "handleInventoryMouseClick",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V"
            )
    )
    private void dontSendVirtualFilterPackets(int containerId, int slotId, int mouseButton, ClickType clickType, Player player, CallbackInfo ci, @Local AbstractContainerMenu menu) {
        if (player.containerMenu instanceof IFilterScreenOrMenu virt && virt.ctt$isVirtual()) {
            if (slotId == 0 || slotId > 37) {
                ci.cancel();
            }
        }
    }

    @ModifyArg(
            method = "handleInventoryMouseClick",
            index = 0,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;clicked(IILnet/minecraft/world/inventory/ClickType;Lnet/minecraft/world/entity/player/Player;)V"
            )
    )
    private int virtualSlotOffset(int slotId, @Local AbstractContainerMenu menu) {
        if (menu instanceof IFilterScreenOrMenu virt && virt.ctt$isVirtual()) {
            return slotId - 1;
        }

        return slotId;
    }
}
