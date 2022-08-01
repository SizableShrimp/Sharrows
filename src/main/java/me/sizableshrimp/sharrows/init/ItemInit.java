package me.sizableshrimp.sharrows.init;

import me.sizableshrimp.sharrows.SharrowsMod;
import me.sizableshrimp.sharrows.item.SharrowItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SharrowsMod.MODID);

    public static final RegistryObject<Item> SHARROW = ITEMS.register("sharrow", () -> new SharrowItem(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
}
