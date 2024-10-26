package com.agricraft.agricraft.datagen;

import com.agricraft.agricraft.api.AgriApi;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Cross-Compatibility
        //     Plants (blocks)
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/aluminium")))
                .addOptionalTag(ResourceLocation.tryParse("c:aluminum_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:aluminium_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/aluminum"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/aluminium"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/coal")))
                .add(Blocks.COAL_ORE)
                .add(Blocks.DEEPSLATE_COAL_ORE)
                .addOptionalTag(ResourceLocation.tryParse("minecraft:coal_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:coal_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/coal"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/copper")))
                .add(Blocks.COPPER_ORE)
                .add(Blocks.DEEPSLATE_COPPER_ORE)
                .addOptionalTag(ResourceLocation.tryParse("minecraft:copper_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:copper_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/copper"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/diamond")))
                .add(Blocks.DIAMOND_ORE)
                .add(Blocks.DEEPSLATE_DIAMOND_ORE)
                .addOptionalTag(ResourceLocation.tryParse("minecraft:diamond_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:diamond_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/diamond"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/emerald")))
                .add(Blocks.EMERALD_ORE)
                .add(Blocks.DEEPSLATE_EMERALD_ORE)
                .addOptionalTag(ResourceLocation.tryParse("minecraft:emerald_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:emerald_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/emerald"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/gold")))
                .add(Blocks.GOLD_ORE)
                .add(Blocks.DEEPSLATE_GOLD_ORE)
                .addOptionalTag(ResourceLocation.tryParse("minecraft:gold_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:gold_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/gold"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/iron")))
                .add(Blocks.IRON_ORE)
                .add(Blocks.DEEPSLATE_IRON_ORE)
                .addOptionalTag(ResourceLocation.tryParse("minecraft:iron_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:iron_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/iron"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/lapis")))
                .add(Blocks.LAPIS_ORE)
                .add(Blocks.DEEPSLATE_LAPIS_ORE)
                .addOptionalTag(ResourceLocation.tryParse("minecraft:lapis_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:lapis_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/lapis"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/lead")))
                .addOptionalTag(ResourceLocation.tryParse("c:lead_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/lead"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/netherite_scrap")))
                .add(Blocks.ANCIENT_DEBRIS)
                .addOptionalTag(ResourceLocation.tryParse("c:netherite_scrap_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/netherite_scrap"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/nickel")))
                .addOptionalTag(ResourceLocation.tryParse("c:nickel_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/nickel"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/osmium")))
                .addOptionalTag(ResourceLocation.tryParse("c:osmium_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/osmium"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/platinum")))
                .addOptionalTag(ResourceLocation.tryParse("c:platinum_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/platinum"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/quartz")))
                .add(Blocks.NETHER_QUARTZ_ORE)
                .addOptionalTag(ResourceLocation.tryParse("c:quartz_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/quartz"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/redstone")))
                .add(Blocks.REDSTONE_ORE)
                .add(Blocks.DEEPSLATE_REDSTONE_ORE)
                .addOptionalTag(ResourceLocation.tryParse("c:redstone_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/redstone"));
        this.tag(TagKey.create(Registries.BLOCK, AgriApi.modLocation("ores/tin")))
                .addOptionalTag(ResourceLocation.tryParse("c:tin_ores"))
                .addOptionalTag(ResourceLocation.tryParse("c:ores/tin"));
    }

}
