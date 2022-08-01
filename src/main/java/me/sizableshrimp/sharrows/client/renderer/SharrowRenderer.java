package me.sizableshrimp.sharrows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import me.sizableshrimp.sharrows.entity.Sharrow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

public class SharrowRenderer extends EntityRenderer<Sharrow> {
    private final ItemRenderer itemRenderer;

    public SharrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(Sharrow sharrow, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // See ArrowRenderer#render
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTick, sharrow.yRotO, sharrow.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTick, sharrow.xRotO, sharrow.getXRot())));
        float f9 = (float) sharrow.shakeTime - partialTick;
        if (f9 > 0.0F) {
            float f10 = -Mth.sin(f9 * 3.0F) * f9;
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(f10));
        }

        ItemStack carryingStack = sharrow.getCarryingStack();
        if (!carryingStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.25D, -0.1D, 0);
            poseStack.scale(0.75F, 0.75F, 0.75F);
            this.itemRenderer.renderStatic(carryingStack, ItemTransforms.TransformType.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, 0);
            poseStack.popPose();
        }

        poseStack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        poseStack.scale(0.05625F, 0.05625F, 0.05625F);
        poseStack.translate(-4.0D, 0, 0);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(sharrow)));
        PoseStack.Pose lastPose = poseStack.last();
        Matrix4f pose = lastPose.pose();
        Matrix3f normals = lastPose.normal();

        // Fletchings
        this.vertex(pose, normals, vertexConsumer, -7, -2, -2, 0, 0.15625F, -1, 0, 0, packedLight);
        this.vertex(pose, normals, vertexConsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLight);
        this.vertex(pose, normals, vertexConsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLight);
        this.vertex(pose, normals, vertexConsumer, -7, 2, -2, 0, 0.3125F, -1, 0, 0, packedLight);
        this.vertex(pose, normals, vertexConsumer, -7, 2, -2, 0, 0.15625F, 1, 0, 0, packedLight);
        this.vertex(pose, normals, vertexConsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLight);
        this.vertex(pose, normals, vertexConsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLight);
        this.vertex(pose, normals, vertexConsumer, -7, -2, -2, 0, 0.3125F, 1, 0, 0, packedLight);

        // Shaft
        for (int i = 0; i < 4; ++i) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
            this.vertex(pose, normals, vertexConsumer, -8, -2, 0, 0, 0, 0, 1, 0, packedLight);
            this.vertex(pose, normals, vertexConsumer, 8, -2, 0, 0.5F, 0, 0, 1, 0, packedLight);
            this.vertex(pose, normals, vertexConsumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLight);
            this.vertex(pose, normals, vertexConsumer, -8, 2, 0, 0, 0.15625F, 0, 1, 0, packedLight);
        }

        poseStack.popPose();
        super.render(sharrow, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    public void vertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexConsumer, int offsetX, int offsetY, int offsetZ,
            float textureX, float textureY, int x, int z, int y, int packedLight) {
        vertexConsumer.vertex(matrix, offsetX, offsetY, offsetZ)
                .color(255, 255, 255, 255)
                .uv(textureX, textureY)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(packedLight)
                .normal(normals, x, y, z)
                .endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(Sharrow entity) {
        return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
    }
}
