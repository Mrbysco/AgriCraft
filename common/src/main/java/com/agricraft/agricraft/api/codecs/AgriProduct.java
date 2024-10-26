package com.agricraft.agricraft.api.codecs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;

import java.util.Optional;

public record AgriProduct(ExtraCodecs.TagOrElementLocation item, DataComponentPatch components, int min, int max, double chance,
                          boolean required) {

	public static final Codec<AgriProduct> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ExtraCodecs.TAG_OR_ELEMENT_ID.fieldOf("item").forGetter(product -> product.item),
			DataComponentPatch.CODEC.optionalFieldOf("components").forGetter(product -> product.components.isEmpty() ? Optional.empty() : Optional.of(product.components)),
			Codec.INT.fieldOf("min").forGetter(product -> product.min),
			Codec.INT.fieldOf("max").forGetter(product -> product.max),
			Codec.DOUBLE.fieldOf("chance").forGetter(product -> product.chance),
			Codec.BOOL.fieldOf("required").forGetter(product -> product.required)
	).apply(instance, AgriProduct::new));

	public AgriProduct(ExtraCodecs.TagOrElementLocation item, Optional<DataComponentPatch> components, int min, int max, double chance, boolean required) {
		// codec use
		this(item, components.orElse(DataComponentPatch.builder().build()), min, max, chance, required);
	}

	public boolean shouldDrop(RandomSource random) {
		return this.chance > random.nextDouble();
	}

	public static Builder builder() {
		return new Builder();
	}

	public int getAmount(RandomSource random) {
		return random.nextIntBetweenInclusive(this.min, this.max);
	}

	public static class Builder {
		ExtraCodecs.TagOrElementLocation item = new ExtraCodecs.TagOrElementLocation(ResourceLocation.withDefaultNamespace("air"), false);
		DataComponentPatch components = DataComponentPatch.builder().build();
		int min = 1;
		int max = 3;
		double chance = 0.95;
		boolean required = true;

		public AgriProduct build() {
			return new AgriProduct(item, components, min, max, chance, required);
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
		public Builder components(DataComponentPatch componentPatch) {
			this.components = componentPatch;
			return this;
		}
		public Builder count(int min, int max, double chance) {
			this.min = min;
			this.max = max;
			this.chance = chance;
			return this;
		}
		public Builder required(boolean required) {
			this.required = required;
			return this;
		}

	}
}
