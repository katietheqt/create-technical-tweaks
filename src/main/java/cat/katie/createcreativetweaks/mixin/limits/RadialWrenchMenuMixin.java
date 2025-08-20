package cat.katie.createcreativetweaks.mixin.limits;

import cat.katie.createcreativetweaks.infrastructure.config.AllConfigs;
import cat.katie.createcreativetweaks.util.TextUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.wrench.RadialWrenchMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;
import java.util.stream.Stream;

@Mixin(value = RadialWrenchMenu.class, remap = false)
public class RadialWrenchMenuMixin {
    @Shadow @Final public static Map<Property<?>, String> VALID_PROPERTIES;

    @WrapOperation(
            method = "tryCreateFor",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z",
                    remap = false
            )
    )
    private static boolean bypassBlockBlacklist(Set<ResourceLocation> instance, Object location, Operation<Boolean> original) {
        if (!AllConfigs.client().wrenchDebugStick.get()) {
            return original.call(instance, location);
        }

        return false;
    }

    @WrapOperation(
            method = "tryCreateFor",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;toList()Ljava/util/List;",
                    remap = false
            )
    )
    private static List<Map.Entry<Property<?>, String>> allowAllProperties(
            Stream<Map.Entry<Property<?>, String>> instance, Operation<List<Map.Entry<Property<?>, String>>> original,
            @Local(argsOnly = true) BlockState state
    ) {
        if (!AllConfigs.client().wrenchDebugStick.get()) {
            return original.call(instance);
        }

        Collection<Property<?>> properties = state.getProperties();
        Map<Property<?>, String> namedProperties = new HashMap<>(properties.size());

        for (Map.Entry<Property<?>, String> entry : VALID_PROPERTIES.entrySet()) {
            if (!state.hasProperty(entry.getKey())) {
                continue;
            }

            namedProperties.put(entry.getKey(), entry.getValue());
        }

        for (Property<?> property: properties) {
            String name = property.getName();
            String prettyName = TextUtils.snakeCaseToPrettyName(name);
            namedProperties.putIfAbsent(property, prettyName);
        }

        return new ArrayList<>(namedProperties.entrySet());
    }
}
