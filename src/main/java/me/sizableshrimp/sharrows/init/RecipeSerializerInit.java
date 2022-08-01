package me.sizableshrimp.sharrows.init;

import me.sizableshrimp.sharrows.SharrowsMod;
import me.sizableshrimp.sharrows.recipe.SharrowRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializerInit {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SharrowsMod.MODID);

    public static final RegistryObject<SimpleRecipeSerializer<SharrowRecipe>> SHARROW_CRAFTING = RECIPE_SERIALIZERS.register("sharrow_crafting", () -> new SimpleRecipeSerializer<>(SharrowRecipe::new));
}
