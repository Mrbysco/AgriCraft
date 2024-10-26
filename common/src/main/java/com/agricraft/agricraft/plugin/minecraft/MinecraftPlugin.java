package com.agricraft.agricraft.plugin.minecraft;

import com.agricraft.agricraft.api.config.CoreConfig;
import com.agricraft.agricraft.common.registry.ModItems;
import net.minecraft.world.level.block.ComposterBlock;

public class MinecraftPlugin {

	public static void init() {
		// add agricraft seeds to the composter
		float compostValue = CoreConfig.seedCompostValue;
		if (compostValue > 0) {
			ComposterBlock.COMPOSTABLES.put(ModItems.SEED.get(), compostValue);
		}
		MinecraftPlantModifiers.register();
	}

}
