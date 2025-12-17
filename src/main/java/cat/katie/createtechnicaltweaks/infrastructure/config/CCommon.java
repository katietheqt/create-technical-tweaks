package cat.katie.createtechnicaltweaks.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class CCommon extends ConfigBase {
    @SuppressWarnings("unused")
    public final ConfigGroup contraptions = group(1, "contraption", "Configure contraption behaviour");
    public final ConfigBool dontMoveEntitiesOnSave = b(false, "dontMoveEntitiesOnContraptionSave",
            "Doesn't call setPosRaw on contraption save. Behaviour may be broken");
    public final ConfigBool dontDoTickingIfFrozen = b(true, "dontDoTickingIfFrozen",
            "Prevents Create custom tick stuff from happening if time is frozen with /tick freeze (e.g. contraption collision)");

    @Override
    public @NotNull String getName() {
        return "common";
    }
}
