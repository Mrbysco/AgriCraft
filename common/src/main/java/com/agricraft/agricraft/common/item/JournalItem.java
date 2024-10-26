package com.agricraft.agricraft.common.item;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.tools.journal.JournalData;
import com.agricraft.agricraft.api.tools.journal.JournalPage;
import com.agricraft.agricraft.client.ClientUtil;
import com.agricraft.agricraft.common.block.entity.SeedAnalyzerBlockEntity;
import com.agricraft.agricraft.common.datacomponent.ModDataComponents;
import com.agricraft.agricraft.common.item.journal.EmptyPage;
import com.agricraft.agricraft.common.item.journal.FrontPage;
import com.agricraft.agricraft.common.item.journal.GeneticsPage;
import com.agricraft.agricraft.common.item.journal.GrowthReqsPage;
import com.agricraft.agricraft.common.item.journal.IntroductionPage;
import com.agricraft.agricraft.common.item.journal.MutationsPage;
import com.agricraft.agricraft.common.item.journal.PlantPage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JournalItem extends Item {

	public JournalItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack stack = player.getItemInHand(usedHand);
		if (player.isDiscrete()) {
			return InteractionResultHolder.pass(stack);
		}
		if (level.isClientSide) {
			ClientUtil.openJournalScreen(player, usedHand);
			return InteractionResultHolder.consume(stack);
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		if (level.isClientSide) {
			return InteractionResult.PASS;
		}
		ItemStack heldItem = context.getItemInHand();
		// if a seed analyzer was clicked, insert the journal inside
		if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof SeedAnalyzerBlockEntity seedAnalyzer) {
			if (seedAnalyzer.hasJournal()) {
				return InteractionResult.PASS;
			}
			ItemStack remaining = seedAnalyzer.insertJournal(heldItem);
			heldItem.setCount(remaining.getCount());
			return InteractionResult.CONSUME;
		}
		return super.useOn(context);
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
//		researchPlant(stack, ResourceLocation.withDefaultNamespace("wheat"));
		return stack;
	}

	public static void researchPlant(ItemStack journal, ResourceLocation plantId) {
		String id = plantId.toString();
		if (journal.has(ModDataComponents.PLANTS.get())) {
			List<String> plants = journal.get(ModDataComponents.PLANTS.get());
			if (!plants.contains(id)) {
				plants.add(id);
			}
		} else {
			List<String> plants = new ArrayList<>();
			plants.add(id);
			journal.set(ModDataComponents.PLANTS.get(), plants);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		tooltipComponents.add(Component.translatable("agricraft.tooltip.journal", getResearchedPlants(stack)).withStyle(ChatFormatting.GRAY));
	}

	public static JournalData getJournalData(ItemStack journal) {
		return new Data(journal);
	}

	public static int getResearchedPlants(ItemStack journal) {
		if (!journal.has(ModDataComponents.PLANTS.get())) {
			return 0;
		}
		return journal.get(ModDataComponents.PLANTS.get()).size();
	}

	public static class Data implements JournalData {

		private final List<ResourceLocation> plants;
		private final List<JournalPage> pages;

		public Data(ItemStack journalStack) {
			this.plants = new ArrayList<>();
			this.pages = new ArrayList<>();
			if (journalStack.has(ModDataComponents.PLANTS.get())) {
				List<String> list = journalStack.get(ModDataComponents.PLANTS.get());
				for (String plant : list) {
					ResourceLocation plantId = ResourceLocation.tryParse(plant);
					if (AgriApi.getPlant(plantId).isPresent()) {
						plants.add(plantId);
					}
				}
			}
			this.plants.sort(Comparator.comparing(ResourceLocation::toString));
			this.initializePages();
		}

		public void initializePages() {
			this.pages.clear();
			this.pages.add(new FrontPage());
			this.pages.add(new IntroductionPage());
			this.pages.add(new GeneticsPage());
			this.pages.add(new GrowthReqsPage());
			for (ResourceLocation plant : this.plants) {
				PlantPage plantPage = new PlantPage(plant, plants);
				this.pages.add(plantPage);
				List<List<ResourceLocation>> mutations = plantPage.getMutationsOffPage();
				int size = mutations.size();
				if (size > 0) {
					int remaining = size;
					int from = 0;
					int to = Math.min(remaining, MutationsPage.LIMIT);
					while (remaining > 0) {
						pages.add(new MutationsPage(mutations.subList(from, to)));
						remaining -= (to - from);
						from = to;
						to = from + Math.min(remaining, MutationsPage.LIMIT);
					}
				}
			}
		}

		@Override
		public JournalPage getPage(int index) {
			if (0 <= index && index < this.pages.size()) {
				return this.pages.get(index);
			}
			return new EmptyPage();
		}

		@Override
		public int size() {
			return this.pages.size();
		}

		@Override
		public List<ResourceLocation> getDiscoveredSeeds() {
			return this.plants;
		}

	}

}
