package cat.katie.createtechnicaltweaks.mixin.contraption_order.contraption;

import cat.katie.createtechnicaltweaks.duck.ContraptionDuck;
import cat.katie.createtechnicaltweaks.features.contraption_debug.ClientContraptionExtra;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.piston.PistonContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(value = PistonContraption.class, remap = false)
public abstract class PistonContraptionMixin implements ContraptionDuck {
    @WrapOperation(
            method = "collectExtensions",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private Object includePistonExtensionsInOrderList(Map<BlockPos, StructureTemplate.StructureBlockInfo> instance, Object pos, Object info, Operation<Object> original) {
        Object ret = original.call(instance, pos, info);
        ClientContraptionExtra state = ctt$getClientExtra();

        if (state != null && ret == null) {
            state.assemblyOrder().add((BlockPos) pos);
        }

        return ret;
    }
}
