package cat.katie.createtechnicaltweaks.mixin.stock_keeper.categories;

import cat.katie.createtechnicaltweaks.duck.IFilterScreenOrMenu;
import cat.katie.createtechnicaltweaks.features.stock_keeper.categories.DummySlot;
import cat.katie.createtechnicaltweaks.features.stock_keeper.categories.VirtualFilterHack;
import com.simibubi.create.content.logistics.filter.AbstractFilterMenu;
import com.simibubi.create.foundation.gui.menu.HeldItemGhostItemMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFilterMenu.class)
public abstract class AbstractFilterMenuMixin extends HeldItemGhostItemMenu implements IFilterScreenOrMenu {
    @Unique
    private boolean ctt$virtual;

    protected AbstractFilterMenuMixin(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    @Override
    @Unique
    public void ctt$makeVirtual() {
        ctt$virtual = true;
    }

    @Override
    @Unique
    public boolean ctt$isVirtual() {
        return ctt$virtual;
    }

    @Inject(
            method = "addSlots",
            at = @At("HEAD")
    )
    private void injectDummySlots(CallbackInfo ci) {
        ctt$virtual = VirtualFilterHack.IS_VIRTUAL;

        if (ctt$virtual) {
            addSlot(new DummySlot(0));
        }
    }
}
