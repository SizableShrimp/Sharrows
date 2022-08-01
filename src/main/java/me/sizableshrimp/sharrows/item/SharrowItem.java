package me.sizableshrimp.sharrows.item;

import me.sizableshrimp.sharrows.client.renderer.SharrowItemRenderer;
import me.sizableshrimp.sharrows.entity.Sharrow;
import me.sizableshrimp.sharrows.tooltip.CarryingStackTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SharrowItem extends ArrowItem {
    public SharrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public Sharrow createArrow(Level level, ItemStack stack, LivingEntity shooter) {
        Sharrow sharrow = new Sharrow(level, shooter);
        sharrow.setEffectsFromItem(stack);
        return sharrow;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);

        ItemStack carryingStack = getCarryingStack(stack);
        if (!carryingStack.isEmpty()) {
            MutableComponent component = carryingStack.getHoverName().copy();
            // component.append(" x").append(String.valueOf(carryingStack.getCount()));
            tooltipComponents.add(Component.translatable("item.sharrows.sharrow.description", component).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        ItemStack carryingStack = getCarryingStack(stack);
        if (carryingStack.isEmpty())
            return Optional.empty();

        return Optional.of(new CarryingStackTooltip(carryingStack));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final SharrowItemRenderer renderer = new SharrowItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    public static void setCarryingStack(ItemStack stack, ItemStack carryingStack) {
        stack.getOrCreateTag().put("CarryingStack", carryingStack.save(new CompoundTag()));
    }

    public static ItemStack getCarryingStack(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains("CarryingStack", Tag.TAG_COMPOUND))
            return ItemStack.EMPTY;

        return ItemStack.of(stack.getTag().getCompound("CarryingStack"));
    }
}
