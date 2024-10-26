package com.agricraft.agricraft.api.codecs;

import com.agricraft.agricraft.common.util.Platform;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record AgriSeed(ExtraCodecs.TagOrElementLocation item, boolean overridePlanting, DataComponentPatch components,
                       double grassDropChance, double seedDropChance, double seedDropBonus) {
	// TODO: @Ketheroth move drop chances in AgriPlant to make it usable for the default agricraft seed

	public static final Codec<AgriSeed> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ExtraCodecs.TAG_OR_ELEMENT_ID.fieldOf("item").forGetter(seed -> seed.item),
			Codec.BOOL.fieldOf("override_planting").forGetter(seed -> seed.overridePlanting),
			DataComponentPatch.CODEC.optionalFieldOf("components").forGetter(seed -> seed.components.isEmpty() ? Optional.empty() : Optional.of(seed.components)),
			Codec.DOUBLE.fieldOf("grass_drop_chance").forGetter(plant -> plant.grassDropChance),
			Codec.DOUBLE.fieldOf("seed_drop_bonus").forGetter(plant -> plant.seedDropBonus),
			Codec.DOUBLE.fieldOf("seed_drop_chance").forGetter(plant -> plant.seedDropChance)
	).apply(instance, AgriSeed::new));

	public AgriSeed(ExtraCodecs.TagOrElementLocation item, boolean overridePlanting, Optional<DataComponentPatch> nbt,
	                double grassDropChance, double seedDropChance, double seedDropBonus) {
		// codec use
		this(item, overridePlanting, nbt.orElse(DataComponentPatch.builder().build()), grassDropChance, seedDropChance, seedDropBonus);
	}

	public static Builder builder() {
		return new Builder();
	}

	public boolean isVariant(ItemStack itemStack) {
		List<Item> items = Platform.get().getItemsFromLocation(this.item());
		if (items.contains(itemStack.getItem())) {
			if (this.components.isEmpty()) {
				return true;
			}
			for (Map.Entry<DataComponentType<?>, Optional<?>> entry : this.components.entrySet()) {
				if (!itemStack.has(entry.getKey()) || !itemStack.get(entry.getKey()).equals(entry.getValue().get())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public static class Builder {

		ExtraCodecs.TagOrElementLocation item;
		boolean overridePlanting = true;
		DataComponentPatch components = DataComponentPatch.builder().build();
		double grassDropChance = 0;
		double seedDropChance = 1.0;
		double seedDropBonus = 0.0;

		public AgriSeed build() {
			return new AgriSeed(item, overridePlanting, components, grassDropChance, seedDropChance, seedDropBonus);
		}

		public Builder item(String location) {
			this.item = new ExtraCodecs.TagOrElementLocation(ResourceLocation.tryParse(location), false);
			return this;
		}

		public Builder item(String namespace, String path) {
			this.item = new ExtraCodecs.TagOrElementLocation(ResourceLocation.fromNamespaceAndPath(namespace, path), false);
			return this;
		}

		public Builder tag(String location) {
			this.item = new ExtraCodecs.TagOrElementLocation(ResourceLocation.tryParse(location), true);
			return this;
		}

		public Builder tag(String namespace, String path) {
			this.item = new ExtraCodecs.TagOrElementLocation(ResourceLocation.fromNamespaceAndPath(namespace, path), true);
			return this;
		}

		public Builder components(DataComponentPatch components) {
			this.components = components;
			return this;
		}

		public Builder chances(double grass, double seed, double seedBonus) {
			this.grassDropChance = grass;
			this.seedDropChance = seed;
			this.seedDropBonus = seedBonus;
			return this;
		}

		public Builder overridePlanting(boolean overridePlanting) {
			this.overridePlanting = overridePlanting;
			return this;
		}

	}

}
