package cat.katie.createtechnicaltweaks.mixin.stock_keeper.categories;

import cat.katie.createtechnicaltweaks.duck.IFilterScreenOrMenu;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.simibubi.create.content.logistics.filter.AbstractFilterScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFilterScreen.class)
public abstract class AbstractFilterScreenMixin extends Screen implements IFilterScreenOrMenu {
    @Unique
    private boolean ctt$virtual;

    protected AbstractFilterScreenMixin(Component title) {
        super(title);
    }

    @WrapWithCondition(
            method = "containerTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;closeContainer()V"
            )
    )
    private boolean allowNonMainhandTargetItem(Player instance) {
        return !ctt$virtual;
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

    @Redirect(
            method = "lambda$init$1()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;closeContainer()V"
            )
    )
    private void useCorrectCloseMethod(LocalPlayer instance) {
        onClose();
    }
}
