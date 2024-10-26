package com.agricraft.agricraft.api.genetic;

import com.agricraft.agricraft.common.datacomponent.ModDataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface AgriGenomeProviderItem {

	/**
	 * Change the genome of the plant.
	 * @param genome the new genome of the crop
	 */
	default void setGenome(ItemStack stack, AgriGenome genome) {
		CompoundTag tag = new CompoundTag();
		genome.writeToNBT(tag);
		stack.set(ModDataComponents.GENOME.get(), tag);
	}

	default Optional<AgriGenome> getGenome(ItemStack stack) {
		return Optional.ofNullable(AgriGenome.fromNBT(stack.get(ModDataComponents.GENOME.get())));
	}

}
