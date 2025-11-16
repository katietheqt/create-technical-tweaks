package cat.katie.createtechnicaltweaks.features.contraption_order;

import cat.katie.createtechnicaltweaks.duck.ContraptionDuck;
import cat.katie.createtechnicaltweaks.features.contraption_order.state.OrderState;
import cat.katie.createtechnicaltweaks.features.contraption_order.state.UnassembledOrderState;
import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import cat.katie.createtechnicaltweaks.infrastructure.config.CClient;
import cat.katie.createtechnicaltweaks.mixin.accessors.ContraptionAccessor;
import cat.katie.createtechnicaltweaks.mixin.accessors.MountedStorageManagerAccessor;
import cat.katie.createtechnicaltweaks.infrastructure.rendering.CTTColors;
import cat.katie.createtechnicaltweaks.infrastructure.rendering.CustomRenderTypes;
import cat.katie.createtechnicaltweaks.util.CTTLang;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorage;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.MountedStorageManager;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.theme.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.MutablePair;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class ContraptionOrderRenderer {
    private static final float FONT_SCALE = 0.015F;

    private final Map<BlockPos, LangBuilder> blockText = new HashMap<>();

    private final OrderState state;

    private ContraptionOrderRenderer(OrderState state) {
        this.state = state;
    }

    /**
     * Appends some text to the info for the given block.
     *
     * @param pos        the contraption-local position of the block
     * @param newBuilder the builder to append
     */
    private void getOrAppend(BlockPos pos, LangBuilder newBuilder) {
        if (!blockText.containsKey(pos)) {
            blockText.put(pos, newBuilder);
            return;
        }

        LangBuilder existingBuilder = blockText.get(pos);
        existingBuilder.add(CTTLang.text(" "));
        existingBuilder.add(newBuilder);
        blockText.put(pos, existingBuilder);
    }

    private void addExceptionData() {
        if (!(state instanceof UnassembledOrderState unassembledState)) {
            return;
        }

        AssemblyException lastException = unassembledState.lastException();

        if (lastException == null || !lastException.hasPosition()) {
            return;
        }

        BlockPos worldPos = lastException.getPosition();
        BlockPos localPos = ((ContraptionAccessor) state.contraption()).ctt$toLocalPos(worldPos);

        getOrAppend(localPos, CTTLang.text("!").color(CTTColors.RED).style(ChatFormatting.BOLD));
    }

    public void addActorOrder() {
        int color = AllConfigs.client().actorTextColor.get();

        int index = 0;
        for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> pair : state.contraption().getActors()) {
            StructureTemplate.StructureBlockInfo info = pair.left;

            getOrAppend(info.pos(), CTTLang.number(index).color(color));
            index++;
        }
    }

    public void labelBlockPositionsInOrder(Set<BlockPos> positions, int color) {
        int index = 0;
        for (BlockPos pos : positions) {
            getOrAppend(pos, CTTLang.number(index).color(color));
            index++;
        }
    }

    public void addFrontierOrdering() {
        ClientContraptionExtra orderState = ((ContraptionDuck) state.contraption()).ctt$getClientExtra();

        if (orderState == null) {
            return;
        }

        Color color = new Color(AllConfigs.client().frontierTextColor.get());

        int idx = 0;
        for (Iterator<BlockPos> it = orderState.assemblyOrder().iterator(); it.hasNext(); idx++) {
            BlockPos localPos = it.next();
            BlockPos source = orderState.blockToSourcePos().get(localPos);

            getOrAppend(localPos, CTTLang.number(idx).color(source == null ? color.darker() : color));
        }
    }

    public void gatherData() {
        CClient config = AllConfigs.client();

        addExceptionData();

        if (state.displayState().showFrontierOrdering()) {
            if (((ContraptionDuck) state.contraption()).ctt$getClientExtra() != null) {
                addFrontierOrdering();
            }
        }

        if (state.displayState().showActorOrder()) {
            addActorOrder();
        }

        if (state.displayState().showStorageOrder()) {
            MountedStorageManager storage = state.contraption().getStorage();
            MountedStorageManagerAccessor storageAccessor = (MountedStorageManagerAccessor) storage;

            Map<BlockPos, MountedItemStorage> itemsBuilder = storageAccessor.ctt$getItemsBuilder();

            int itemStorageColor = config.itemStorageTextColor.get();
            int fluidStorageColor = config.fluidStorageTextColor.get();

            // manager is already initialized (assembled contraption) - use the initialized fields
            if (itemsBuilder == null) {
                labelBlockPositionsInOrder(storage.getAllItemStorages().keySet(), itemStorageColor);
                labelBlockPositionsInOrder(storage.getFluids().storages.keySet(), fluidStorageColor);
            } else {
                labelBlockPositionsInOrder(itemsBuilder.keySet(), itemStorageColor);
                labelBlockPositionsInOrder(storageAccessor.ctt$getFluidsBuilder().keySet(), fluidStorageColor);
            }
        }

        if (!blockText.containsKey(BlockPos.ZERO)) {
            // mark the anchor if it isn't already marked
            getOrAppend(BlockPos.ZERO, CTTLang.text("*").color(config.anchorPointTextColor.get()));
        }
    }

    public static void renderOverlay(PoseStack stack, MultiBufferSource.BufferSource buffers, Camera camera, OrderState state, float partialTicks) {
        ContraptionOrderRenderer renderer = new ContraptionOrderRenderer(state);
        renderer.gatherData();
        renderer.render(stack, buffers, camera, partialTicks);
    }

    public void render(PoseStack stack, MultiBufferSource.BufferSource buffers, Camera camera, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;

        if (!camera.isInitialized()) {
            return;
        }

        // TODO: unsure of the consequences of ending the batch here
        buffers.endBatch();

        // translate origin to world space
        Vector3f camPos = camera.getPosition().toVector3f();

        stack.pushPose();
        stack.translate(-camPos.x, -camPos.y, -camPos.z);
        stack.translate(0.5F, 0.5F, 0.5F);

        // setup render position
        state.setupRenderTransform(stack, partialTicks);

        if (state.displayState().showFrontierOrdering()) {
            maybeDrawFrontierLines(stack, buffers);
        }

        for (Map.Entry<BlockPos, LangBuilder> entry : blockText.entrySet()) {
            stack.pushPose();

            BlockPos pos = entry.getKey();
            stack.translate(pos.getX(), pos.getY(), pos.getZ());

            LangBuilder builder = entry.getValue();
            drawString(builder.component(), -0.7F, font, stack, buffers, camera, partialTicks);

            stack.popPose();
        }

        stack.popPose();
    }

    /**
     * Draws lines between blocks to show the frontier order visually.
     */
    private void maybeDrawFrontierLines(PoseStack stack, MultiBufferSource buffers) {
        ClientContraptionExtra orderState = ((ContraptionDuck) state.contraption()).ctt$getClientExtra();

        if (orderState == null) {
            return;
        }

        // entries in this list are in world space
        VertexConsumer consumer = buffers.getBuffer(CustomRenderTypes.THROUGH_WALL_LINES);

        int color = AllConfigs.client().frontierOrderLineColor.get();

        for (Map.Entry<BlockPos, BlockPos> entry : orderState.blockToSourcePos().entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }

            BlockPos pos = entry.getKey(); // local

            if (!state.contraption().getBlocks().containsKey(pos)) {
                continue;
            }

            Vector3f posCenter = Vec3.atLowerCornerOf(pos).toVector3f();

            Vector3f sourceCenter = Vec3.atLowerCornerOf(entry.getValue()).toVector3f();
            Vector3f normal = new Vector3f(posCenter).sub(sourceCenter).normalize();

            consumer
                    .addVertex(stack.last(), posCenter.x, posCenter.y, posCenter.z)
                    .setColor(color)
                    .setNormal(stack.last(), normal.x, normal.y, normal.z);

            consumer
                    .addVertex(stack.last(), sourceCenter.x, sourceCenter.y, sourceCenter.z)
                    .setColor(color)
                    .setNormal(stack.last(), normal.x, normal.y, normal.z);
        }
    }

    private void drawString(Component text, float yOff, Font font, PoseStack stack, MultiBufferSource buffers, Camera camera, float partialTicks) {
        stack.pushPose();

        stack.mulPose(state.inverseRotationMatrix(partialTicks));
        stack.mulPose(new Quaternionf().rotationYXZ((float) (-Math.PI / 180.0) * camera.getYRot(), (float) (Math.PI / 180.0) * camera.getXRot(), 0.0F));
        stack.scale(-FONT_SCALE, -FONT_SCALE, FONT_SCALE);

        float textX = (float) (-font.width(text)) / 2.0F;
        font.drawInBatch(text, textX, yOff, 0xFFFFFFFF, false, stack.last().pose(), buffers, Font.DisplayMode.SEE_THROUGH, 0, 15728880);

        stack.popPose();
    }
}
