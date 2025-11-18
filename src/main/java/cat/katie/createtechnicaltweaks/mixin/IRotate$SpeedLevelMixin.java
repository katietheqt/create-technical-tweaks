package cat.katie.createtechnicaltweaks.mixin;

import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.text.NumberFormat;
import java.util.Locale;

@Mixin(IRotate.SpeedLevel.class)
public class IRotate$SpeedLevelMixin {
    @Unique
    private static final NumberFormat CTT$FORMAT = Util.make(NumberFormat.getNumberInstance(Locale.ROOT), it -> {
        it.setMaximumFractionDigits(9);
        it.setMinimumFractionDigits(0);
        it.setGroupingUsed(true);
    });

    @WrapOperation(
            method = "getFormattedSpeedText",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;abs(F)F"
            )
    )
    private static float showRpmDirection(float speed, Operation<Float> original) {
        if (AllConfigs.client().showFullPrecisionRpmValues.get()) {
            return speed;
        }

        return original.call(speed);
    }

    @WrapOperation(
            method = "getFormattedSpeedText",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/foundation/utility/CreateLang;number(D)Lnet/createmod/catnip/lang/LangBuilder;"
            )
    )
    private static LangBuilder showFullPrecisionRpm(double speed, Operation<LangBuilder> original) {
        if (!AllConfigs.client().showFullPrecisionRpmValues.get()) {
            return original.call(speed);
        }

        String formatted = CTT$FORMAT.format(speed).replace("\u00A0", " ");
        return CreateLang.builder().text(formatted);
    }
}
