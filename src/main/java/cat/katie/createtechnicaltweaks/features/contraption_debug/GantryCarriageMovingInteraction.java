package cat.katie.createtechnicaltweaks.features.contraption_debug;

import cat.katie.createtechnicaltweaks.features.contraption_debug.state.AssembledDebugState;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

/**
 * Custom moving interaction for gantry carriages to visualize their attached contraptions.
 */
public class GantryCarriageMovingInteraction extends MovingInteractionBehaviour {
    @Override
    public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity entity) {
        if (!entity.level().isClientSide || !player.getItemInHand(activeHand).isEmpty() || !player.isShiftKeyDown()) {
            return false;
        }

        if (!localPos.equals(BlockPos.ZERO)) {
            // only allow clicking the main anchor
            return false;
        }

        if (!ContraptionDebug.INSTANCE.handleExistingMovingAnchorClick(entity)) {
            // if no anchor exists, add a new one
            ContraptionDebug.INSTANCE.addNewMovingState(new AssembledDebugState(entity, null));
        }

        return false; // don't send server packet
    }
}
