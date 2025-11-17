package cat.katie.createtechnicaltweaks.features.stock_keeper.categories;

import cat.katie.createtechnicaltweaks.duck.IFilterScreenOrMenu;
import cat.katie.createtechnicaltweaks.mixin.accessors.AbstractContainerMenuAccessor;
import com.simibubi.create.content.logistics.filter.FilterMenu;
import com.simibubi.create.content.logistics.filter.FilterScreen;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperCategoryMenu;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperCategoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class VirtualFilterScreen extends FilterScreen {
    private final StockKeeperCategoryScreen parent;

    public static VirtualFilterScreen create(StockKeeperCategoryScreen parent) {
        StockKeeperCategoryMenu parentMenu = parent.getMenu();
        VirtualFilterHack.IS_VIRTUAL = true;

        try {
            FilterMenu filterMenu = FilterMenu.create(parentMenu.containerId, parentMenu.playerInventory, parentMenu.proxyInventory.getStackInSlot(0));
            ((AbstractContainerMenuAccessor) filterMenu).ctt$setStateId(parentMenu.getStateId());
            return new VirtualFilterScreen(filterMenu, filterMenu.playerInventory, Component.empty(), parent);
        } finally {
            VirtualFilterHack.IS_VIRTUAL = false;
        }
    }

    private VirtualFilterScreen(FilterMenu menu, Inventory inv, Component title, StockKeeperCategoryScreen parent) {
        super(menu, inv, title);
        ((IFilterScreenOrMenu) this).ctt$makeVirtual();

        this.parent = parent;
    }

    @Override
    public void onClose() {
        VirtualFilterHack.onClose(menu, parent);
    }
}
