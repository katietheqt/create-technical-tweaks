package cat.katie.createtechnicaltweaks.duck;

import cat.katie.createtechnicaltweaks.features.contraption_order.ClientContraptionExtra;

public interface ContraptionDuck {
    /**
     * Sets the extra state. This being non-null changes contraption behaviour to client-side behaviour so this MUST NOT
     * be called on server contraptions.
     */
    void ctt$setClientExtra(ClientContraptionExtra clientExtra);

    ClientContraptionExtra ctt$getClientExtra();
}
