package cat.katie.createtechnicaltweaks.features.contraption_debug.state;

public enum DisplayState {
    FRONTIER_ACTORS_STORAGES,
    ACTORS_STORAGES,
    COLLISION_BOXES,
    DISABLED;

    private static final DisplayState[] VALUES = DisplayState.values();

    public DisplayState next() {
        if (this == DISABLED) {
            throw new IllegalStateException("cannot advance past DISABLED state");
        }

        return VALUES[ordinal() + 1];
    }

    public boolean showFrontierOrdering() {
        return this == FRONTIER_ACTORS_STORAGES;
    }

    public boolean showStorageOrder() {
        return this != COLLISION_BOXES;
    }

    public boolean showActorOrder() {
        return this != COLLISION_BOXES;
    }

    public boolean showAnchorPoint() {
        return this != COLLISION_BOXES;
    }

    public boolean showCollisionBoxes() {
        return this == COLLISION_BOXES;
    }
}
