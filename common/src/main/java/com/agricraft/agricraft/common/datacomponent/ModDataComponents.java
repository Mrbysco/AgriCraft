package com.agricraft.agricraft.common.datacomponent;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.crop.AgriGrowthStage;
import com.agricraft.agricraft.common.util.Platform;
import com.agricraft.agricraft.common.util.PlatformRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;

import java.util.List;

public class ModDataComponents {
	public static final PlatformRegistry<DataComponentType<?>> DATA_COMPONENT_TYPES = Platform.get().createRegistry(BuiltInRegistries.DATA_COMPONENT_TYPE, AgriApi.MOD_ID);

	public static final PlatformRegistry.Entry<DataComponentType<Boolean>> MAGNIFYING = DATA_COMPONENT_TYPES.register("magnifying", () ->
			DataComponentType.<Boolean>builder()
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL)
					.build());

	public static final PlatformRegistry.Entry<DataComponentType<Integer>> SORTER = DATA_COMPONENT_TYPES.register("sorter", () ->
			DataComponentType.<Integer>builder()
					.persistent(Codec.INT)
					.networkSynchronized(ByteBufCodecs.VAR_INT)
					.build());

	public static final PlatformRegistry.Entry<DataComponentType<List<CompoundTag>>> SEEDS = DATA_COMPONENT_TYPES.register("seeds", () ->
			DataComponentType.<List<CompoundTag>>builder()
					.persistent(CompoundTag.CODEC.listOf())
					.networkSynchronized(ByteBufCodecs.COMPOUND_TAG.apply(ByteBufCodecs.list()))
					.build());

	public static final PlatformRegistry.Entry<DataComponentType<List<String>>> PLANTS = DATA_COMPONENT_TYPES.register("plants", () ->
			DataComponentType.<List<String>>builder()
					.persistent(Codec.STRING.listOf())
					.networkSynchronized(ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()))
					.build());

	public static final PlatformRegistry.Entry<DataComponentType<CompoundTag>> GENOME = DATA_COMPONENT_TYPES.register("genome", () ->
			DataComponentType.<CompoundTag>builder()
					.persistent(CompoundTag.CODEC)
					.networkSynchronized(ByteBufCodecs.COMPOUND_TAG)
					.build());

	public static final PlatformRegistry.Entry<DataComponentType<String>> SPECIES = DATA_COMPONENT_TYPES.register("species", () ->
			DataComponentType.<String>builder()
					.persistent(Codec.STRING)
					.networkSynchronized(ByteBufCodecs.STRING_UTF8)
					.build());

	public static final PlatformRegistry.Entry<DataComponentType<AgriGrowthStage>> GROWTH_STAGE = DATA_COMPONENT_TYPES.register("growth_stage", () ->
			DataComponentType.<AgriGrowthStage>builder()
					.persistent(AgriGrowthStage.CODEC)
					.networkSynchronized(AgriGrowthStage.STREAM_CODEC)
					.build());
}
