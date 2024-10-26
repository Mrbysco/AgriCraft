package com.agricraft.agricraft.datagen;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.common.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

	public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, blockTags, modId, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		// add agricraft seeds to the chicken food items
		this.tag(ItemTags.CHICKEN_FOOD).add(ModItems.SEED.get());

		// Common
		this.tag(TagKey.create(Registries.ITEM, ResourceLocation.tryParse("c:seeds")))
				.add(ModItems.SEED.get());
		this.tag(TagKey.create(Registries.ITEM, ResourceLocation.tryParse("c:nuggets/coal")))
				.add(ModItems.COAL_PEBBLE.get());
		this.tag(TagKey.create(Registries.ITEM, ResourceLocation.tryParse("c:nuggets/copper")))
				.add(ModItems.COPPER_NUGGET.get());
		this.tag(TagKey.create(Registries.ITEM, ResourceLocation.tryParse("c:nuggets/diamond")))
				.add(ModItems.DIAMOND_SHARD.get());
		this.tag(TagKey.create(Registries.ITEM, ResourceLocation.tryParse("c:nuggets/emerald")))
				.add(ModItems.EMERALD_SHARD.get());
		this.tag(TagKey.create(Registries.ITEM, ResourceLocation.tryParse("c:nuggets/quartz")))
				.add(ModItems.QUARTZ_SHARD.get());

		// Cross-Compatibility
		//     Recipes
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("rods/wooden")))
				.add(Items.STICK)
				.addOptionalTag(ResourceLocation.tryParse("c:wooden_rods"))
				.addOptionalTag(ResourceLocation.tryParse("c:rods/wooden"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/iron")))
				.add(Items.IRON_NUGGET)
				.addOptionalTag(ResourceLocation.tryParse("c:iron_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/iron"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("obsidian")))
				.add(Items.OBSIDIAN)
				.addOptionalTag(ResourceLocation.tryParse("c:obsidian"))
				.addOptionalTag(ResourceLocation.tryParse("c:obsidian"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("seeds")))
				.add(Items.BEETROOT_SEEDS)
				.add(Items.MELON_SEEDS)
				.add(Items.PUMPKIN_SEEDS)
				.add(Items.WHEAT_SEEDS)
				.addOptionalTag(ResourceLocation.tryParse("c:seeds"))
				.addOptionalTag(ResourceLocation.tryParse("c:seeds"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("ingots/iron")))
				.add(Items.IRON_INGOT)
				.addOptionalTag(ResourceLocation.tryParse("c:iron_ingots"))
				.addOptionalTag(ResourceLocation.tryParse("c:ingots/iron"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("glass_panes/colorless")))
				.add(Items.GLASS_PANE)
				.addOptionalTag(ResourceLocation.tryParse("c:clear_glass_panes"))
				.addOptionalTag(ResourceLocation.tryParse("c:glass_panes/colorless"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("fences/wooden")))
				.addOptionalTag(ResourceLocation.tryParse("minecraft:wooden_fences")) // Not sure why this tag isn't available during datagen?
				.addOptionalTag(ResourceLocation.tryParse("c:wooden_fences"))
				.addOptionalTag(ResourceLocation.tryParse("c:fences/wooden"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("string")))
				.add(Items.STRING)
				.addOptionalTag(ResourceLocation.tryParse("c:strings"))
				.addOptionalTag(ResourceLocation.tryParse("c:string"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("leather")))
				.add(Items.LEATHER)
				.addOptionalTag(ResourceLocation.tryParse("c:leathers"))
				.addOptionalTag(ResourceLocation.tryParse("c:leather"));
		//     Plants (produce)
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("dusts/glowstone")))
				.add(Items.GLOWSTONE_DUST)
				.addOptionalTag(ResourceLocation.tryParse("c:glowstone_dusts"))
				.addOptionalTag(ResourceLocation.tryParse("c:dusts/glowstone"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("dusts/redstone")))
				.add(Items.REDSTONE)
				.addOptionalTag(ResourceLocation.tryParse("c:redstone_dusts"))
				.addOptionalTag(ResourceLocation.tryParse("c:dusts/redstone"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("gems/lapis")))
				.add(Items.LAPIS_LAZULI)
				.addOptionalTag(ResourceLocation.tryParse("c:lapis"))
				.addOptionalTag(ResourceLocation.tryParse("c:lapis_gems"))
				.addOptionalTag(ResourceLocation.tryParse("c:gems/lapis"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/aluminium")))
				.addOptionalTag(ResourceLocation.tryParse("c:aluminum_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:aluminium_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/aluminum"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/aluminium"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/coal")))
				.add(ModItems.COAL_PEBBLE.get())
				.addOptionalTag(ResourceLocation.tryParse("c:coal_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/coal"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/copper")))
				.add(ModItems.COPPER_NUGGET.get())
				.addOptionalTag(ResourceLocation.tryParse("c:copper_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/copper"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/diamond")))
				.add(ModItems.DIAMOND_SHARD.get())
				.addOptionalTag(ResourceLocation.tryParse("c:diamond_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/diamond"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/emerald")))
				.add(ModItems.EMERALD_SHARD.get())
				.addOptionalTag(ResourceLocation.tryParse("c:emerald_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/emerald"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/gold")))
				.add(Items.GOLD_NUGGET)
				.addOptionalTag(ResourceLocation.tryParse("c:gold_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/gold"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/iron")))
				.add(Items.IRON_NUGGET)
				.addOptionalTag(ResourceLocation.tryParse("c:iron_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/iron"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/lead")))
				.addOptionalTag(ResourceLocation.tryParse("c:lead_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/lead"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/nickel")))
				.addOptionalTag(ResourceLocation.tryParse("c:nickel_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/nickel"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/osmium")))
				.addOptionalTag(ResourceLocation.tryParse("c:osmium_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/osmium"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/platinum")))
				.addOptionalTag(ResourceLocation.tryParse("c:platinum_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/platinum"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/quartz")))
				.add(ModItems.QUARTZ_SHARD.get())
				.addOptionalTag(ResourceLocation.tryParse("c:quartz_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/quartz"));
		this.tag(TagKey.create(Registries.ITEM, AgriApi.modLocation("nuggets/tin")))
				.addOptionalTag(ResourceLocation.tryParse("c:tin_nuggets"))
				.addOptionalTag(ResourceLocation.tryParse("c:nuggets/tin"));
	}

}
