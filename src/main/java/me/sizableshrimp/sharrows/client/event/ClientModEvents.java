package me.sizableshrimp.sharrows.client.event;

import me.sizableshrimp.sharrows.SharrowsMod;
import me.sizableshrimp.sharrows.client.renderer.SharrowRenderer;
import me.sizableshrimp.sharrows.client.tooltip.ClientCarryingStackTooltip;
import me.sizableshrimp.sharrows.init.EntityInit;
import me.sizableshrimp.sharrows.tooltip.CarryingStackTooltip;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SharrowsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.SHARROW.get(), SharrowRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterClientTooltipFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(CarryingStackTooltip.class, ClientCarryingStackTooltip::new);
    }
}
