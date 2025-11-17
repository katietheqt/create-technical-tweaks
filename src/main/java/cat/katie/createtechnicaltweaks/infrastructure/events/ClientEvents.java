package cat.katie.createtechnicaltweaks.infrastructure.events;

import cat.katie.createtechnicaltweaks.features.contraption_debug.ContraptionDebug;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onTick(ClientTickEvent.Post event) {
        ContraptionDebug.INSTANCE.tick();
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ContraptionDebug.INSTANCE.onRightClickBlock(event);
    }

    @SubscribeEvent
    public static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        ContraptionDebug.INSTANCE.onInteractEntity(event);
    }
}