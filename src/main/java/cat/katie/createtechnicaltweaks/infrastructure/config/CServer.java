package cat.katie.createtechnicaltweaks.infrastructure.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class CServer extends ConfigBase {
    public final ConfigBool teleportNoPassengers = b(false, "teleportNoPassengers", "Makes teleports not teleport entity passengers");

    @SuppressWarnings("unused")
    public final ConfigGroup limits = group(1, "limits", "Configure limit bypasses requiring server support");

    @SuppressWarnings("unused")
    public final ConfigGroup glue = group(2, "glue", "Super-Glue");
    public final ConfigBool unlimitedGlueBoxes = b(false, "unlimitedGlueBoxes",
            "Enables unlimited-size glue boxes with the glue item");

    @SuppressWarnings("unused")
    public final ConfigGroup contraptions = group(1, "contraptions", "Configure contraption-related settings");
    public final ConfigBool syncMinecartsEveryTick = b(false, "syncMinecartsEveryTick",
            "Syncs minecart positions to the client every tick",
            "Ported from MendedMinecarts by 2No2Name and Inspector Talon"
    );

    @Override
    public @NotNull String getName() {
        return "server";
    }
}
