package me.sizableshrimp.sharrows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.sizableshrimp.sharrows.item.SharrowItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SharrowItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ItemStack ARROW_STACK = new ItemStack(Items.ARROW);

    public SharrowItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        ItemTransforms.TransformType renderTransformType = transformType;
        transformType = transformType.fallback();
        if (transformType == null)
            transformType = renderTransformType;

        if (transformType == ItemTransforms.TransformType.GROUND) {
            poseStack.translate(0.5D, 0.25D, 0.5D);
        } else {
            poseStack.translate(0.5D, 0.5D, 0.5D);
        }
        boolean leftHand = transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND
                || transformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND;

        poseStack.pushPose();

        poseStack.pushPose();
        ItemStack carryingStack = SharrowItem.getCarryingStack(stack);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        renderCarryingStack(itemRenderer, carryingStack, transformType, renderTransformType, poseStack, buffer, packedLight, packedOverlay, leftHand);
        poseStack.popPose();

        if (transformType == ItemTransforms.TransformType.GUI) {
            poseStack.translate(0, 0, 5);
        }
        ARROW_STACK.setCount(stack.getCount());
        if (!ARROW_STACK.isEmpty()) {
            BakedModel model = itemRenderer.getModel(ARROW_STACK, null, null, 0);
            itemRenderer.render(ARROW_STACK, renderTransformType, leftHand, poseStack, buffer, packedLight, packedOverlay, model);
        }

        poseStack.popPose();
        poseStack.popPose();
    }

    private void renderCarryingStack(ItemRenderer itemRenderer, ItemStack carryingStack, ItemTransforms.TransformType transformType,
            ItemTransforms.TransformType renderTransformType, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight, int packedOverlay, boolean leftHand) {
        if (carryingStack.isEmpty())
            return;

        BakedModel model = itemRenderer.getModel(carryingStack, null, null, 0);
        boolean isGui3d = model.isGui3d();
        boolean firstPerson = renderTransformType.firstPerson();

        if (firstPerson && leftHand) {
            poseStack.translate(isGui3d ? -0.1D : 0, 0.1D, 0.05D);
        } else if (firstPerson /* right hand */) {
            poseStack.translate(isGui3d ? 0.1D : 0, 0.1D, 0.05D);
        }

        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0, 0.01D, 0); // Z fighting fix

        if (transformType == ItemTransforms.TransformType.GROUND && !isGui3d) {
            poseStack.mulPose(Vector3f.YP.rotationDegrees(90));
        }

        itemRenderer.render(carryingStack, renderTransformType, leftHand, poseStack, buffer, packedLight, packedOverlay, model);
    }
}
