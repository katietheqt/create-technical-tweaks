package cat.katie.createtechnicaltweaks.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class CServer extends ConfigBase {
    public final ConfigBool unlimitedGlueBoxes = b(false, "unlimitedGlueBoxes",
            "Enables unlimited-size glue boxes with the glue item");

    @Override
    public @NotNull String getName() {
        return "server";
    }
}
