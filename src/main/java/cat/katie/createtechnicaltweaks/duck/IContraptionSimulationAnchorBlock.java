package cat.katie.createtechnicaltweaks.duck;

import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Duck interface implemented by blocks that can simulate contraption assembly.
 */
public interface IContraptionSimulationAnchorBlock {
    Contraption ctt$createContraption(BlockState state);
}
