package cat.katie.createtechnicaltweaks.features.stock_keeper.categories;

import cat.katie.createtechnicaltweaks.duck.IFilterScreenOrMenu;
import cat.katie.createtechnicaltweaks.mixin.accessors.AbstractContainerMenuAccessor;
import com.simibubi.create.content.logistics.filter.PackageFilterMenu;
import com.simibubi.create.content.logistics.filter.PackageFilterScreen;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperCategoryMenu;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperCategoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class VirtualPackageFilterScreen extends PackageFilterScreen {
    private final StockKeeperCategoryScreen parent;

    public static VirtualPackageFilterScreen create(StockKeeperCategoryScreen parent) {
        StockKeeperCategoryMenu parentMenu = parent.getMenu();
        VirtualFilterHack.IS_VIRTUAL = true;

        try {
            PackageFilterMenu filterMenu = PackageFilterMenu.create(parentMenu.containerId, parentMenu.playerInventory, parentMenu.proxyInventory.getStackInSlot(0));
            ((AbstractContainerMenuAccessor) filterMenu).ctt$setStateId(parentMenu.getStateId());
            return new VirtualPackageFilterScreen(filterMenu, filterMenu.playerInventory, Component.empty(), parent);
        } finally {
            VirtualFilterHack.IS_VIRTUAL = false;
        }
    }

    private VirtualPackageFilterScreen(PackageFilterMenu menu, Inventory inv, Component title, StockKeeperCategoryScreen parent) {
        super(menu, inv, title);
        ((IFilterScreenOrMenu) this).ctt$makeVirtual();

        this.parent = parent;
    }

    @Override
    public void onClose() {
        VirtualFilterHack.onClose(menu, parent);
    }
}
