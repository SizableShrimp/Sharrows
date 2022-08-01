package me.sizableshrimp.sharrows.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.sizableshrimp.sharrows.tooltip.CarryingStackTooltip;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

public record ClientCarryingStackTooltip(CarryingStackTooltip tooltip) implements ClientTooltipComponent {
    @Override
    public int getHeight() {
        return 26;
    }

    @Override
    public int getWidth(Font font) {
        return 20;
    }

    @Override
    public void renderImage(Font font, int mouseX, int mouseY, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        this.renderSlot(mouseX + 1, mouseY + 1, font, poseStack, itemRenderer, blitOffset);
    }

    private void blitSlot(PoseStack poseStack, int x, int y, int blitOffset) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, ClientBundleTooltip.TEXTURE_LOCATION);
        GuiComponent.blit(poseStack, x, y, blitOffset, 0, 0, 18, 20, 128, 128);
    }

    private void renderSlot(int x, int y, Font font, PoseStack poseStack, ItemRenderer itemRenderer, int blitOffset) {
        ItemStack carryingStack = this.tooltip.carryingStack();
        this.blitSlot(poseStack, x, y, blitOffset);
        itemRenderer.renderAndDecorateItem(carryingStack, x + 1, y + 1, 0);
        itemRenderer.renderGuiItemDecorations(font, carryingStack, x + 1, y + 1);
        // if (itemIndex == 0) {
        //     AbstractContainerScreen.renderSlotHighlight(poseStack, x + 1, y + 1, blitOffset);
        // }
    }
}
