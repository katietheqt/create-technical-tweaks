package cat.katie.createtechnicaltweaks.mixin;

import cat.katie.createtechnicaltweaks.infrastructure.asm.ASM;
import cat.katie.createtechnicaltweaks.infrastructure.asm.ASMTransformer;
import com.mojang.logging.LogUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.util.Annotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class CTTMixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @SuppressWarnings("unchecked")
    private static void applyAsm(ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo, BiConsumer<ASMTransformer, ClassNode> consumer) {
        ClassNode mixinNode = mixinInfo.getClassNode(ClassReader.SKIP_CODE);
        AnnotationNode asmAnnotation = Annotations.getInvisible(mixinNode, ASM.class);

        if (asmAnnotation == null) {
            return;
        }

        List<Type> transformerClasses = Annotations.getValue(asmAnnotation);

        for (Type transformerType: transformerClasses) {
            if (transformerType.getSort() != Type.OBJECT) {
                throw new IllegalStateException("ASM annotation on " + mixinClassName + " has non-object transformer!");
            }

            String clazzName = transformerType.getInternalName().replaceAll("/", ".");
            Class<? extends ASMTransformer> transformerClazz;

            try {
                transformerClazz = (Class<? extends ASMTransformer>) Class.forName(clazzName);
            } catch (ClassNotFoundException e) {
                LOGGER.error("could not load target class for @ASM on {}", mixinClassName, e);
                continue;
            }

            ASMTransformer transformer = null;

            try {
                Constructor<? extends ASMTransformer> ctor = transformerClazz.getConstructor();
                transformer = ctor.newInstance();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                LOGGER.error("@ASM transformer {} missing public no-arg constructor", transformerClazz.getName(), e);
            } catch (InvocationTargetException e) {
                LOGGER.error("Constructor threw in @ASM transformer {}", transformerClazz.getName(), e);
            }

            if (transformer == null) {
                continue;
            }

            try {
                consumer.accept(transformer, targetClass);
            } catch (Exception e) {
                LOGGER.error("@ASM transformer {} threw!", transformerClazz.getName(), e);
            }
        }
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        applyAsm(targetClass, mixinClassName, mixinInfo, ASMTransformer::preTransform);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        applyAsm(targetClass, mixinClassName, mixinInfo, ASMTransformer::postTransform);
    }
}
