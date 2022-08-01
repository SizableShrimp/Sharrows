package me.sizableshrimp.sharrows.init;

import me.sizableshrimp.sharrows.SharrowsMod;
import me.sizableshrimp.sharrows.entity.Sharrow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SharrowsMod.MODID);

    public static final RegistryObject<EntityType<Sharrow>> SHARROW = registerEntity("sharrow", () ->
            EntityType.Builder.<Sharrow>of(Sharrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20));

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITY_TYPES.register(name, () -> supplier.get().build(name));
    }
}
