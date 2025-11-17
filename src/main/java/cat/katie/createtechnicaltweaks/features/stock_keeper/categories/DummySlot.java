package cat.katie.createtechnicaltweaks.features.stock_keeper.categories;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class DummySlot extends SlotItemHandler {
    public DummySlot(int index) {
        super(new ItemStackHandler(), index, -2000, -2000);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean mayPickup(@NotNull Player playerIn) {
        return false;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }
}
