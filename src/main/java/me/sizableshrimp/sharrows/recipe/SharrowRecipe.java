package me.sizableshrimp.sharrows.recipe;

import me.sizableshrimp.sharrows.init.ItemInit;
import me.sizableshrimp.sharrows.init.RecipeSerializerInit;
import me.sizableshrimp.sharrows.item.SharrowItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class SharrowRecipe extends CustomRecipe {
    private static final Ingredient ARROW_INGREDIENT = Ingredient.of(Items.ARROW);

    public SharrowRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        boolean hasArrow = false;
        boolean hasNonArrow = false;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.isEmpty())
                continue;

            if (ARROW_INGREDIENT.test(itemStack)) {
                if (hasArrow)
                    return false; // We only allow one slot with arrows

                hasArrow = true;
            } else {
                if (hasNonArrow)
                    return false; // We only allow one slot with non-arrows

                hasNonArrow = true;
            }
        }

        return hasArrow && hasNonArrow;
    }

    @Override
    public ItemStack assemble(CraftingContainer container) {
        ItemStack arrowStack = null;
        ItemStack nonArrowStack = null;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.isEmpty())
                continue;

            if (ARROW_INGREDIENT.test(itemStack)) {
                arrowStack = itemStack;
            } else {
                nonArrowStack = itemStack;
            }
        }
        if (arrowStack == null || nonArrowStack == null)
            throw new IllegalStateException();

        CompoundTag arrowTag = arrowStack.save(new CompoundTag());
        arrowTag.putString("id", ItemInit.SHARROW.getId().toString());
        ItemStack result = ItemStack.of(arrowTag);
        SharrowItem.setCarryingStack(result, nonArrowStack);

        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(ItemInit.SHARROW.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerInit.SHARROW_CRAFTING.get();
    }
}
