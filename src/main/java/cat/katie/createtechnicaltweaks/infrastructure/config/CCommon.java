package cat.katie.createtechnicaltweaks.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class CCommon extends ConfigBase {
    public final ConfigBool dontMoveEntitiesOnSave = b(false, "dontMoveEntitiesOnContraptionSave",
            "Doesn't call setPosRaw on contraption save. Behaviour may be broken");

    @Override
    public @NotNull String getName() {
        return "common";
    }
}
