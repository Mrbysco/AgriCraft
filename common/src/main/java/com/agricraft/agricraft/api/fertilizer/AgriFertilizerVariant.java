package com.agricraft.agricraft.api.fertilizer;

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

public record AgriFertilizerVariant(ExtraCodecs.TagOrElementLocation item, DataComponentPatch components) {

	public static final Codec<AgriFertilizerVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ExtraCodecs.TAG_OR_ELEMENT_ID.fieldOf("item").forGetter(variant -> variant.item),
			DataComponentPatch.CODEC.optionalFieldOf("components").forGetter(variant -> variant.components.isEmpty() ? Optional.empty() : Optional.of(variant.components))
	).apply(instance, AgriFertilizerVariant::new));

	public AgriFertilizerVariant(ExtraCodecs.TagOrElementLocation item, Optional<DataComponentPatch> nbt) {
		// codec use
		this(item, nbt.orElse(DataComponentPatch.builder().build()));
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
		DataComponentPatch components = DataComponentPatch.builder().build();

		public AgriFertilizerVariant build() {
			return new AgriFertilizerVariant(item, components);
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

	}
}
