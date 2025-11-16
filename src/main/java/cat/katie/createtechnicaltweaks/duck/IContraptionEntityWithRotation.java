package cat.katie.createtechnicaltweaks.duck;

import org.joml.Matrix4f;

/**
 * Duck interface implemented by contraption entities with custom rotation handling, so that we can reverse their
 * rotation for text billboarding.
 */
public interface IContraptionEntityWithRotation {
    Matrix4f ctt$inverseRotationMatrix(float partialTicks);
}
