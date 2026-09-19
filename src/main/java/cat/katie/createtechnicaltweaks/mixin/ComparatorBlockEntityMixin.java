package cat.katie.createtechnicaltweaks.mixin;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ComparatorBlockEntity.class)
public class ComparatorBlockEntityMixin implements IHaveGoggleInformation {
    @Shadow
    private int output;

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (AllConfigs.client().showComparatorSignal.get()) {
            CreateLang.translate("tooltip.analogStrength", this.output).forGoggles(tooltip);
            return true;
        } else {
            return false;
        }
    }
}
