package me.sizableshrimp.sharrows.entity;

import me.sizableshrimp.sharrows.init.EntityInit;
import me.sizableshrimp.sharrows.init.ItemInit;
import me.sizableshrimp.sharrows.item.SharrowItem;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class Sharrow extends Arrow {
    private static final EntityDataAccessor<ItemStack> ID_CARRYING_STACK = SynchedEntityData.defineId(Sharrow.class, EntityDataSerializers.ITEM_STACK);

    public Sharrow(EntityType<? extends Sharrow> entityType, Level level) {
        super(entityType, level);
    }

    public Sharrow(Level level, double x, double y, double z) {
        this(EntityInit.SHARROW.get(), level);
        this.setPos(x, y, z);
    }

    public Sharrow(Level level, LivingEntity shooter) {
        this(level, shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
        this.setOwner(shooter);
        if (shooter instanceof Player) {
            this.pickup = Pickup.ALLOWED;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_CARRYING_STACK, ItemStack.EMPTY);
    }

    public void setCarryingStack(ItemStack stack) {
        this.entityData.set(ID_CARRYING_STACK, stack);
    }

    public ItemStack getCarryingStack() {
        return this.entityData.get(ID_CARRYING_STACK);
    }

    @Override
    public void setEffectsFromItem(ItemStack stack) {
        super.setEffectsFromItem(new ItemStack(Items.ARROW));

        ItemStack carryingStack = SharrowItem.getCarryingStack(stack);
        if (!carryingStack.isEmpty()) {
            this.setCarryingStack(carryingStack);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level.isClientSide && result.getType() != HitResult.Type.MISS) {
            ItemStack carryingStack = getCarryingStack();
            if (!carryingStack.isEmpty()) {
                InteractionResult placeResult = InteractionResult.FAIL;
                if (carryingStack.getItem() instanceof BlockItem blockItem && result instanceof BlockHitResult blockHitResult) {
                    placeResult = blockItem.place(new BlockPlaceContext(this.level, null, InteractionHand.MAIN_HAND, carryingStack, blockHitResult));
                }

                if (placeResult == InteractionResult.FAIL) {
                    ItemEntity itemEntity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), carryingStack);
                    double scale = 1.0D;
                    itemEntity.setDeltaMovement(this.level.random.nextDouble() * scale, this.level.random.nextDouble() * scale, this.level.random.nextDouble() * scale);
                    itemEntity.setDefaultPickUpDelay();
                    this.level.addFreshEntity(itemEntity);
                }
            }
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ItemInit.SHARROW.get());
    }
}