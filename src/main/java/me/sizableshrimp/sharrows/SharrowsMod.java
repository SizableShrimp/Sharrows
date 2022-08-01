package me.sizableshrimp.sharrows;

import me.sizableshrimp.sharrows.init.EntityInit;
import me.sizableshrimp.sharrows.init.ItemInit;
import me.sizableshrimp.sharrows.init.RecipeSerializerInit;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SharrowsMod.MODID)
public class SharrowsMod {
    public static final String MODID = "sharrows";

    public SharrowsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(modEventBus);
        EntityInit.ENTITY_TYPES.register(modEventBus);
        RecipeSerializerInit.RECIPE_SERIALIZERS.register(modEventBus);
    }
}
