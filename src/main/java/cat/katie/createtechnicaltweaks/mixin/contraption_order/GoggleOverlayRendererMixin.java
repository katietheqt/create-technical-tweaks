package cat.katie.createtechnicaltweaks.mixin.contraption_order;

import cat.katie.createtechnicaltweaks.features.contraption_debug.ContraptionDebug;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.equipment.goggles.GoggleOverlayRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(value = GoggleOverlayRenderer.class, remap = false)
public class GoggleOverlayRendererMixin {
    @WrapOperation(
            method = "renderOverlay",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/contraptions/IDisplayAssemblyExceptions;addExceptionToTooltip(Ljava/util/List;)Z"
            )
    )
    private static boolean addClientDisplayAssemblyExceptions(IDisplayAssemblyExceptions instance, List<Component> tooltip, Operation<Boolean> original, @Local(name = "pos") BlockPos pos) {
        boolean added = original.call(instance, tooltip);

        if (!tooltip.isEmpty()) {
            tooltip.add(CommonComponents.EMPTY);
        }

        if (ContraptionDebug.INSTANCE.addGogglesTooltip(tooltip, pos)) {
            added = true;
        } else if (!tooltip.isEmpty()) {
            tooltip.removeLast();
        }

        return added;
    }
}
