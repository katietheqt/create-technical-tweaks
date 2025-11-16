package cat.katie.createtechnicaltweaks.duck;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;

/**
 * Duck interface implemented by contraption anchors that can be clicked while a contraption is attached.
 */
public interface IMovingContraptionAnchorBlockEntity {
    AbstractContraptionEntity ctt$getExistingContraptionEntity();
}
