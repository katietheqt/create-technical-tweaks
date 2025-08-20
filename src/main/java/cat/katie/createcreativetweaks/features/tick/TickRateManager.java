package cat.katie.createcreativetweaks.features.tick;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public enum TickRateManager {
    INSTANCE;

    private FreezeState freezeState = FreezeState.NORMAL;
    private int ticksToStep = 0;

    public void freeze() {
        if (freezeState.isFrozen()) {
            throw new IllegalStateException("Cannot freeze a frozen server");
        }

        freezeState = FreezeState.DEEPLY_FROZEN;
    }

    public void unfreeze() {
        if (!freezeState.isFrozen()) {
            throw new IllegalStateException("Cannot unfreeze a server that isn't frozen");
        }

        freezeState = FreezeState.NORMAL;
    }

    public void step(int count) {
        if (!freezeState.isFrozen()) {
            throw new IllegalStateException("Cannot step a server that isn't frozen");
        }

        ticksToStep += count;
    }

    public boolean isFrozen() {
        return freezeState.isFrozen();
    }

    public boolean isDeeplyFrozen() {
        return freezeState.isDeeplyFrozen();
    }

    public boolean runsNormally() {
        return !freezeState.isFrozen() || ticksToStep > 0;
    }

    public void onTickEnd() {
        if (ticksToStep > 0) {
            ticksToStep--;
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    public boolean shouldTickEntity(Entity entity) {
        if (runsNormally()) {
            return true;
        }

        if (entity instanceof Player) {
            return true;
        }

        if (!freezeState.isDeeplyFrozen() && entity.getControllingPassenger() instanceof Player) {
            return true;
        }

        return false;
    }

    private enum FreezeState {
        NORMAL,
        FROZEN,
        DEEPLY_FROZEN;

        public boolean isFrozen() {
            return this != NORMAL;
        }

        public boolean isDeeplyFrozen() {
            return this == DEEPLY_FROZEN;
        }
    }
}
