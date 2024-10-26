package com.agricraft.agricraft.client.neoforge;

import com.agricraft.agricraft.AgriCraft;
import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.config.AgriCraftConfig;
import com.agricraft.agricraft.client.AgriCraftClient;
import com.agricraft.agricraft.client.ber.CropBlockEntityRenderer;
import com.agricraft.agricraft.client.ber.SeedAnalyzerEntityRenderer;
import com.agricraft.agricraft.client.gui.MagnifyingGlassOverlay;
import com.agricraft.agricraft.client.gui.SeedAnalyzerScreen;
import com.agricraft.agricraft.common.registry.ModBlockEntityTypes;
import com.agricraft.agricraft.common.registry.ModBlocks;
import com.agricraft.agricraft.common.registry.ModMenus;
import com.agricraft.agricraft.common.util.PlatformClient;
import com.agricraft.agricraft.common.util.forge.NeoForgePlatformClient;
import com.teamresourceful.resourcefulconfig.client.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.util.Map;

/**
 * NeoForge client event handler in the mod event bus
 */
@EventBusSubscriber(modid = AgriApi.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AgriCraftNeoForgeClient {

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		PlatformClient.setup(new NeoForgePlatformClient());
		AgriCraftNeoForgeClient.init();
		ItemBlockRenderTypes.setRenderLayer(ModBlocks.SEED_ANALYZER.get(), RenderType.cutout());

		event.enqueueWork(() -> ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () ->
				(modContainer, parentScreen) -> new ConfigScreen(parentScreen, AgriCraft.CONFIGURATOR.getConfig(AgriCraftConfig.class))));

	}

	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent event) {
		event.register(ModMenus.SEED_ANALYZER_MENU.get(), SeedAnalyzerScreen::new);
	}

	@SubscribeEvent
	public static void loadModels(ModelEvent.RegisterAdditional event) {
		// https://discord.com/channels/313125603924639766/983834532904042537/1104441106248253592
		for (Map.Entry<ResourceLocation, Resource> entry : FileToIdConverter.json("models/seed").listMatchingResources(Minecraft.getInstance().getResourceManager()).entrySet()) {
			ResourceLocation seed = ResourceLocation.tryParse(entry.getKey().toString().replace("models/seed", "seed").replace(".json", ""));
			event.register(ModelResourceLocation.standalone(seed));
		}
		for (Map.Entry<ResourceLocation, Resource> entry : FileToIdConverter.json("models/crop").listMatchingResources(Minecraft.getInstance().getResourceManager()).entrySet()) {
			ResourceLocation seed = ResourceLocation.tryParse(entry.getKey().toString().replace("models/crop", "crop").replace(".json", ""));
			event.register(ModelResourceLocation.standalone(seed));
		}
		for (Map.Entry<ResourceLocation, Resource> entry : FileToIdConverter.json("models/weed").listMatchingResources(Minecraft.getInstance().getResourceManager()).entrySet()) {
			ResourceLocation seed = ResourceLocation.tryParse(entry.getKey().toString().replace("models/weed", "weed").replace(".json", ""));
			event.register(ModelResourceLocation.standalone(seed));
		}
		// add the crop sticks models else they're not loaded
		event.register(ModelResourceLocation.standalone(AgriApi.modLocation("block/wooden_crop_sticks")));
		event.register(ModelResourceLocation.standalone(AgriApi.modLocation("block/iron_crop_sticks")));
		event.register(ModelResourceLocation.standalone(AgriApi.modLocation("block/obsidian_crop_sticks")));
		event.register(ModelResourceLocation.standalone(AgriApi.modLocation("block/wooden_cross_crop_sticks")));
		event.register(ModelResourceLocation.standalone(AgriApi.modLocation("block/iron_cross_crop_sticks")));
		event.register(ModelResourceLocation.standalone(AgriApi.modLocation("block/obsidian_cross_crop_sticks")));
	}

	@SubscribeEvent
	public static void registerBer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntityTypes.CROP.get(), CropBlockEntityRenderer::new);
		event.registerBlockEntityRenderer(ModBlockEntityTypes.SEED_ANALYZER.get(), SeedAnalyzerEntityRenderer::new);
	}

	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiLayersEvent event) {
		event.registerAbove(VanillaGuiLayers.HOTBAR, AgriApi.modLocation("magnifying_glass_info"), (guiGraphics, deltaTracker) -> MagnifyingGlassOverlay.renderOverlay(guiGraphics, deltaTracker));
	}

	public static void init() {
		AgriCraftClient.init();
	}

}
