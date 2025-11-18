package cat.katie.createtechnicaltweaks.infrastructure.config;

import cat.katie.createtechnicaltweaks.features.PermaGoggles;
import cat.katie.createtechnicaltweaks.infrastructure.rendering.CTTColors;
import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.config.ui.ConfigAnnotations;
import net.createmod.catnip.theme.Color;

import javax.annotation.Nonnull;

public class CClient extends ConfigBase {
    public final ConfigEnum<PermaGoggles.PermaGogglesState> permaGoggles = e(PermaGoggles.PermaGogglesState.CREATIVE, "permaGoggles",
            "Whether to always treat the client as if they have goggles",
            "- `DISABLED` uses vanilla Create behaviour (player must be wearing goggles)",
            "- `CREATIVE` treats the player as always wearing goggles in creative / spectator",
            "- `ALWAYS` treats the player as always wearing goggles"
    );

    // no group
    public final ConfigBool clickThroughContraptions = b(false, "clickThroughContraptions",
            "Allows clicking through normal blocks on contraptions");

    // rendering
    @SuppressWarnings("unused")
    public final ConfigGroup rendering = group(1, "rendering", "Configure rendering tweaks");
    public final ConfigBool renderOffhandExtendoGrip = b(true, "renderOffhandExtendoGrip",
            "Disables rendering of the first-person extendo-grip model");
    public final ConfigBool showToolboxTooltip = b(true, "showToolboxTooltip",
            "Adds a tooltip to toolboxes to view their contents"
    );

    // limit evasion
    @SuppressWarnings("unused")
    public final ConfigGroup limits = group(1, "limits", "Configure client-side bypasses of various limits");
    public final ConfigBool allowIllegalCogPlacement = b(false, "allowIllegalCogPlacement",
            "Allows cog placement in all positions",
            "This is possible without modding in many ways (contraptions, schematics, etc.)"
    );
    public final ConfigBool uncapClipboards = b(false, "uncapClipboards",
            "Allows editing clipboards with excessively long text",
            "This is possible without modding using a resource pack or via schematics"
    );
    public final ConfigBool uncapPackageAddresses = b(false, "uncapPackageAddresses",
            "Removes the length and glob limits of package filters",
            "This is possible without modding using clipboards"
    );
    public final ConfigBool disableMechanicalArmRangeChecks = b(false, "disableArmRangeChecks",
            "Removes the range checks on mechanical arms",
            "This is possible without modding via schematics"
    );
    public final ConfigBool rotateWithoutWrench = b(false, "rotateWithoutWrench", "Allows using the radial wrench menu without a wrench");
    public final ConfigBool wrenchDebugStick = b(false, "wrenchDebugStick",
            "Wildly extends the abilities of the radial wrench menu, allowing modification of any state of" +
                    " a block (similar to a debug stick)",
            "This is NOT possible without modding, and has mostly dubious uses"
    );

    // limits > glue boxes
    @SuppressWarnings("unused")
    public final ConfigGroup glue = group(2, "glue", "Super-Glue");
    public final ConfigBool alwaysShowHoveredGlue = b(true, "alwaysShowHoveredGlue",
            "Always shows the outline of hovered glue, even if it's outside of the maximum punching range");
    public final ConfigBool extraGluePunchingRange = b(false, "extraGluePunchingRange",
            "Increases your punching range for glue to 30 blocks");

    // contraption order rendering
    @SuppressWarnings("unused")
    public final ConfigGroup contraptionOrder = group(1, "contraptionOrder", "Configure contraption order visualization");

    public final ConfigInt frontierOrderLineColor = i(CTTColors.TRANSLUCENT_GRAY.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "frontierOrderLineColor",
            "The color to use for the frontier order lines",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    public final ConfigInt frontierTextColor = i(Color.SPRING_GREEN.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "frontierTextColor",
            "The color to use for the frontier text labels",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    public final ConfigInt actorTextColor = i(CTTColors.GOLD.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "actorTextColor",
            "The color to use for the text labels on actors",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    public final ConfigInt itemStorageTextColor = i(CTTColors.GRAY.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "itemStorageTextColor",
            "The color to use for the text labels on item storages",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    public final ConfigInt fluidStorageTextColor = i(CTTColors.BLUE.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "fluidStorageTextColor",
            "The color to use for the text labels on fluid storages",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    public final ConfigInt anchorPointTextColor = i(CTTColors.LIGHT_PURPLE.getRGB(), Integer.MIN_VALUE, Integer.MAX_VALUE, "anchorPosTextColor",
            "The color to use for the text labels on the anchor position",
            "[in Hex: #AaRrGgBb]", ConfigAnnotations.IntDisplay.HEX.asComment()
    );

    // qol
    @SuppressWarnings("unused")
    public final ConfigGroup qol = group(1, "qol", "Configure miscellaneous QoL stuff");
    public final ConfigBool placeArmsNormallyWhenShifting = b(false, "placeArmsNormallyWhenShifting", "Places mechanical arms like normal blocks when crouching, instead of always selecting an inventory");

    // qol > stock keeper features
    @SuppressWarnings("unused")
    public final ConfigGroup stockKeeper = group(2, "extendedStockKeeper", "Configure extended stock keeper behaviour");
    public final ConfigBool enhancedCategoryEditUI = b(true, "enhancedCategoryEditUI", "Enhanced the stock ticker category editing UI");



    @Override
    @Nonnull
    public String getName() {
        return "client";
    }
}
