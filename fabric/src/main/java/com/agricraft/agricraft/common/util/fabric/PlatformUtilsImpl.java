package com.agricraft.agricraft.common.util.fabric;

import com.agricraft.agricraft.AgriCraft;
import com.agricraft.agricraft.api.codecs.AgriPlant;
import com.agricraft.agricraft.api.codecs.AgriSoil;
import com.agricraft.agricraft.common.item.AgriSeedItem;
import com.agricraft.agricraft.common.registry.ModItems;
import com.agricraft.agricraft.common.util.PlatformUtils;
import com.agricraft.agricraft.fabric.AgriCraftFabric;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Fabric implementation of {@link com.agricraft.agricraft.common.util.PlatformUtils}
 */
public class PlatformUtilsImpl {

	public static AgriSeedItem createAgriSeedItem(Item.Properties properties) {
		return new AgriSeedItem(properties);
	}


	public static CreativeModeTab createMainCreativeTab() {
		return FabricItemGroup.builder()
				.icon(() -> new ItemStack(ModItems.DEBUGGER.get()))
				.title(Component.translatable("itemGroup.agricraft.main"))
				.displayItems(ModItems::addItemsToTabs)
				.build();
	}

	public static CreativeModeTab createSeedsCreativeTab() {
		return FabricItemGroup.builder()
				.title(Component.translatable("itemGroup.agricraft.seeds"))
				.icon(() -> new ItemStack(Items.WHEAT_SEEDS))
				.displayItems((itemDisplayParameters, output) -> {
					if (AgriCraftFabric.cachedServer != null) {
						Registry<AgriPlant> registry = AgriCraftFabric.cachedServer.registryAccess().registry(PlatformUtils.getPlantRegistryKey()).get();
						AgriCraft.LOGGER.info("add seeds in tab: " + registry.stream().count());
						for (Map.Entry<ResourceKey<AgriPlant>, AgriPlant> entry : registry.entrySet()) {
							output.accept(AgriSeedItem.toStack(entry.getValue()));
						}
					} else {
						AgriCraft.LOGGER.info("cached server is null");
					}
				})
				.build();
	}

	public static String getIdFromPlant(AgriPlant plant) {
		if (AgriCraftFabric.cachedServer == null) {
			return "";
		}
		return AgriCraftFabric.cachedServer.registryAccess().registry(PlatformUtils.getPlantRegistryKey()).get().getKey(plant).toString();
	}

	public static AgriPlant getPlantFromId(String id) {
		if (AgriCraftFabric.cachedServer == null) {
			return null;
		}
		return AgriCraftFabric.cachedServer.registryAccess().registry(PlatformUtils.getPlantRegistryKey()).get().get(new ResourceLocation(id));
	}

	public static String getIdFromSoil(AgriSoil soil) {
		if (AgriCraftFabric.cachedServer == null) {
			return "";
		}
		return AgriCraftFabric.cachedServer.registryAccess().registry(PlatformUtils.AGRISOILS).get().getKey(soil).toString();
	}

	public static Optional<AgriSoil> getSoilFromBlock(BlockState blockState) {
		if (AgriCraftFabric.cachedServer == null) {
			return Optional.empty();
		}
		return AgriCraftFabric.cachedServer.registryAccess().registry(PlatformUtils.AGRISOILS).get().stream().filter(soil -> soil.isVariant(blockState)).findFirst();
	}

	public static List<Item> getItemsFromTag(ResourceLocation tag) {
		return BuiltInRegistries.ITEM.getTag(TagKey.create(Registries.ITEM, tag))
				.map(HolderSet.ListBacked::stream)
				.map(str -> str.map(Holder::value))
				.map(Stream::toList)
				.orElse(List.of());
	}

	public static List<Block> getBlocksFromLocation(ExtraCodecs.TagOrElementLocation tag) {
		if (!tag.tag()) {
			return List.of(BuiltInRegistries.BLOCK.get(tag.id()));
		} else {
			return BuiltInRegistries.BLOCK.getTag(TagKey.create(Registries.BLOCK, tag.id()))
					.map(HolderSet.ListBacked::stream)
					.map(str -> str.map(Holder::value))
					.map(Stream::toList)
					.orElse(List.of());
		}
	}

	public static List<Fluid> getFluidsFromLocation(ExtraCodecs.TagOrElementLocation tag) {
		if (!tag.tag()) {
			return List.of(BuiltInRegistries.FLUID.get(tag.id()));
		} else {
			return BuiltInRegistries.FLUID.getTag(TagKey.create(Registries.FLUID, tag.id()))
					.map(HolderSet.ListBacked::stream)
					.map(str -> str.map(Holder::value))
					.map(Stream::toList)
					.orElse(List.of());
		}
	}

}
