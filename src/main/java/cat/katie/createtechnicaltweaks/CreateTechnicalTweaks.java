package cat.katie.createtechnicaltweaks;

import cat.katie.createtechnicaltweaks.features.PermaGoggles;
import cat.katie.createtechnicaltweaks.features.contraption_debug.GantryCarriageMovingInteraction;
import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;

@Mod(CreateTechnicalTweaks.ID)
public class CreateTechnicalTweaks {
    public static final String ID = "create_technical_tweaks";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateTechnicalTweaks(ModContainer container) {
        IEventBus modBus = container.getEventBus();
        assert modBus != null;

        PermaGoggles.setup();
        modBus.addListener(CreateTechnicalTweaks::init);
        AllConfigs.register(container);

        if (FMLLoader.getDist() == Dist.CLIENT) {
            CTTClient.onCtorClient(modBus);
        }
    }

    public static void init(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            MovingInteractionBehaviour existingBehaviour = MovingInteractionBehaviour.REGISTRY.get(AllBlocks.GANTRY_CARRIAGE.get());

            if (existingBehaviour == null) {
                MovingInteractionBehaviour.REGISTRY.register(AllBlocks.GANTRY_CARRIAGE.get(), new GantryCarriageMovingInteraction());
            } else {
                LOGGER.warn("A different mod already registered a gantry interaction behaviour - ours will be skipped");
            }
        });
    }
}