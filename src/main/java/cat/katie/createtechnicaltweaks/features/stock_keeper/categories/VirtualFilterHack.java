package cat.katie.createtechnicaltweaks.features.stock_keeper.categories;

import cat.katie.createtechnicaltweaks.mixin.accessors.AbstractContainerMenuAccessor;
import com.simibubi.create.content.logistics.filter.AbstractFilterMenu;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperCategoryScreen;
import com.simibubi.create.foundation.gui.menu.GhostItemSubmitPacket;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.Minecraft;

public class VirtualFilterHack {
    public static boolean IS_VIRTUAL = false;

    public static void onClose(AbstractFilterMenu menu, StockKeeperCategoryScreen parent) {
        menu.removed(menu.player);

        CatnipServices.NETWORK.sendToServer(new GhostItemSubmitPacket(menu.contentHolder, 0));
        ((AbstractContainerMenuAccessor) parent.getMenu()).ctt$setStateId(menu.getStateId());
        menu.player.containerMenu = parent.getMenu();
        Minecraft.getInstance().popGuiLayer();
    }
}
