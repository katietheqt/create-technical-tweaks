package cat.katie.createtechnicaltweaks;

import cat.katie.createtechnicaltweaks.infrastructure.rendering.CustomRenderTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class CTTClient {
    private CTTClient() {
        throw new UnsupportedOperationException("Cannot instantiate CTTClient");
    }

    public static void onCtorClient(IEventBus modEventBus) {
        CustomRenderTypes.init();

        modEventBus.addListener(CTTClient::clientInit);
    }

    private static void clientInit(FMLClientSetupEvent event) {

    }
}
