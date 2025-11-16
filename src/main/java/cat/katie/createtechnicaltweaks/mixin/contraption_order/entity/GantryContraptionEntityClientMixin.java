package cat.katie.createtechnicaltweaks.mixin.contraption_order.entity;

import cat.katie.createtechnicaltweaks.duck.IContraptionEntityWithRotation;
import com.simibubi.create.content.contraptions.gantry.GantryContraptionEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = GantryContraptionEntity.class, remap = false)
public class GantryContraptionEntityClientMixin implements IContraptionEntityWithRotation {
    @Override
    public Matrix4f ctt$inverseRotationMatrix(float partialTicks) {
        return new Matrix4f();
    }
}
