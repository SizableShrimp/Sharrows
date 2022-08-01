package me.sizableshrimp.sharrows.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record CarryingStackTooltip(ItemStack carryingStack) implements TooltipComponent {
}
