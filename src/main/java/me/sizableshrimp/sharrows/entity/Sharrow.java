package me.sizableshrimp.sharrows.entity;

import me.sizableshrimp.sharrows.init.EntityInit;
import me.sizableshrimp.sharrows.item.SharrowItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.level.block.state.BlockState;
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

        if (this.level.isClientSide || result.getType() == HitResult.Type.MISS)
            return;

        ItemStack carryingStack = getCarryingStack();
        if (carryingStack.isEmpty())
            return;

        setCarryingStack(ItemStack.EMPTY);
        InteractionResult placeResult = InteractionResult.FAIL;
        if (carryingStack.getItem() instanceof BlockItem blockItem && result instanceof BlockHitResult blockHitResult) {
            BlockPlaceContext blockPlaceContext = new BlockPlaceContext(this.level, null, InteractionHand.MAIN_HAND, carryingStack, blockHitResult);
            BlockPlaceContext updatedContext = blockItem.updatePlacementContext(blockPlaceContext);
            BlockPos clickedPos = updatedContext == null ? null : updatedContext.getClickedPos();
            BlockState prevState = clickedPos == null ? null : this.level.getBlockState(clickedPos);

            placeResult = blockItem.place(blockPlaceContext);

            if (placeResult != InteractionResult.FAIL && clickedPos != null && prevState != this.level.getBlockState(clickedPos)) {
                this.onHitBlock(new BlockHitResult(this.position(), blockHitResult.getDirection(), clickedPos, true));
            }
        }

        if (placeResult == InteractionResult.FAIL) {
            ItemEntity itemEntity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), carryingStack);
            double scale = 0.4D;
            itemEntity.setDeltaMovement((this.level.random.nextDouble() - 0.5D) * scale,
                    Math.max(0.3D, Math.abs(this.level.random.nextDouble() - 0.5D) * scale),
                    (this.level.random.nextDouble() - 0.5D) * scale);
            itemEntity.setDefaultPickUpDelay();
            this.level.addFreshEntity(itemEntity);
        }
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        ItemStack carryingStack = this.getCarryingStack();
        if (!carryingStack.isEmpty())
            compound.put("CarryingStack", carryingStack.save(new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("CarryingStack", Tag.TAG_COMPOUND))
            this.setCarryingStack(ItemStack.of(compound.getCompound("CarryingStack")));
    }
}
