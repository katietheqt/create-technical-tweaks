package cat.katie.createtechnicaltweaks.mixin.stock_keeper.categories;

import cat.katie.createtechnicaltweaks.CreateTechnicalTweaks;
import cat.katie.createtechnicaltweaks.features.stock_keeper.categories.VirtualAttributeFilterScreen;
import cat.katie.createtechnicaltweaks.features.stock_keeper.categories.VirtualFilterScreen;
import cat.katie.createtechnicaltweaks.features.stock_keeper.categories.VirtualPackageFilterScreen;
import cat.katie.createtechnicaltweaks.infrastructure.config.AllConfigs;
import cat.katie.createtechnicaltweaks.util.CTTLang;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.logistics.filter.*;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperCategoryMenu;
import com.simibubi.create.content.logistics.stockTicker.StockKeeperCategoryScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.IconButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StockKeeperCategoryScreen.class)
public abstract class StockKeeperCategoryScreenMixin extends AbstractSimiContainerScreen<StockKeeperCategoryMenu> {
    @Unique
    private static final ResourceLocation CTT$CUSTOM_BACKGROUND_LOCATION = ResourceLocation.fromNamespaceAndPath(CreateTechnicalTweaks.ID, "textures/gui/stock_keeper_categories.png");

    @Shadow
    private EditBox editorEditBox;
    @Unique
    private IconButton ctt$editorEdit;

    public StockKeeperCategoryScreenMixin(StockKeeperCategoryMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @ModifyConstant(
            method = "startEditing",
            constant = @Constant(intValue = 28, ordinal = 1)
    )
    private int removeCustomNameLimit(int constant) {
        if (AllConfigs.client().enhancedCategoryEditUI.get()) {
            return 32767;
        }

        return constant;
    }

    @WrapOperation(
            method = "renderBg",
            at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/gui/AllGuiTextures;render(Lnet/minecraft/client/gui/GuiGraphics;II)V")
    )
    private void dontRenderOriginalBackground(AllGuiTextures instance, GuiGraphics graphics, int x, int y, Operation<Void> original) {
        if (ctt$editorEdit != null && instance == AllGuiTextures.STOCK_KEEPER_CATEGORY_EDIT) {
            graphics.blit(CTT$CUSTOM_BACKGROUND_LOCATION, x, y, 32, 0, 193, 38);
        } else {
            original.call(instance, graphics, x, y);
        }
    }

    @Inject(
            method = "stopEditing",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/logistics/stockTicker/StockKeeperCategoryScreen;removeWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)V",
                    ordinal = 0
            )
    )
    private void removeEditFilterButtonWhenFinishedEditing(CallbackInfo ci) {
        ctt$removeEditButton();
    }

    @Inject(
            method = "containerTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/EditBox;getValue()Ljava/lang/String;"
            )
    )
    private void onContainerTick(CallbackInfo ci) {
        ctt$updateEditButtonState();
    }

    @Inject(
            method = "startEditing",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/logistics/stockTicker/StockKeeperCategoryScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                    ordinal = 0
            )
    )
    private void addEditButtonOnOpen(int index, CallbackInfo ci) {
        ctt$updateEditButtonState();
    }

    @Unique
    private void ctt$editButtonCallback() {
        ItemStack stack = menu.proxyInventory.getStackInSlot(0);
        AbstractFilterScreen<?> innerScreen = ctt$makeVirtualFilterScreen(stack);

        if (innerScreen != null) {
            this.minecraft.player.containerMenu = innerScreen.getMenu();
            Minecraft.getInstance().pushGuiLayer(innerScreen);
        }
    }

    @Unique
    private void ctt$updateEditButtonState() {
        if (!AllConfigs.client().enhancedCategoryEditUI.get()) {
            return;
        }

        ItemStack editedStack = menu.proxyInventory.getStackInSlot(0);
        AbstractFilterScreen<?> screen = ctt$makeVirtualFilterScreen(editedStack);

        if (screen == null) {
            ctt$removeEditButton();
        } else if (ctt$editorEdit == null) {
            ctt$editorEdit = new IconButton(leftPos + 16 + 18 + 1, topPos + 23, AllIcons.I_ADD); // TODO: pencil icon
            ctt$editorEdit.setToolTip(CTTLang.translate("gui.stock_keeper.edit_categories").component());
            ctt$editorEdit.withCallback(this::ctt$editButtonCallback);
            addRenderableWidget(ctt$editorEdit);

            editorEditBox.setX(editorEditBox.getX() + 18 + 1);
            editorEditBox.setWidth(editorEditBox.getWidth() - 18 - 1);
        }
    }

    @Unique
    private AbstractFilterScreen<?> ctt$makeVirtualFilterScreen(ItemStack stack) {
        if (stack.getItem() instanceof ListFilterItem) {
            return VirtualFilterScreen.create((StockKeeperCategoryScreen) (Object) this);
        } else if (stack.getItem() instanceof AttributeFilterItem) {
            return VirtualAttributeFilterScreen.create((StockKeeperCategoryScreen) (Object) this);
        } else if (stack.getItem() instanceof PackageFilterItem) {
            return VirtualPackageFilterScreen.create((StockKeeperCategoryScreen) (Object) this);
        } else {
            return null;
        }
    }

    @Unique
    private void ctt$removeEditButton() {
        if (ctt$editorEdit != null) {
            removeWidget(ctt$editorEdit);
            ctt$editorEdit = null;

            editorEditBox.setX(editorEditBox.getX() - 18 - 1);
            editorEditBox.setWidth(editorEditBox.getWidth() + 18 + 1);
        }
    }
}
