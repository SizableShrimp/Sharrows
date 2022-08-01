package me.sizableshrimp.sharrows.event;

import me.sizableshrimp.sharrows.SharrowsMod;
import me.sizableshrimp.sharrows.init.EntityInit;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SharrowsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.SHARROW.get(), TippableArrowRenderer::new);
    }
}
