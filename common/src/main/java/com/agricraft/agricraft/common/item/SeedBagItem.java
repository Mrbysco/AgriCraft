package com.agricraft.agricraft.common.item;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.config.CoreConfig;
import com.agricraft.agricraft.api.crop.AgriCrop;
import com.agricraft.agricraft.api.genetic.AgriGenome;
import com.agricraft.agricraft.api.genetic.AgriGenomeProviderItem;
import com.agricraft.agricraft.api.stat.AgriStat;
import com.agricraft.agricraft.api.stat.AgriStatRegistry;
import com.agricraft.agricraft.common.block.CropBlock;
import com.agricraft.agricraft.common.block.CropState;
import com.agricraft.agricraft.common.datacomponent.ModDataComponents;
import com.agricraft.agricraft.common.registry.ModBlocks;
import com.agricraft.agricraft.common.util.LangUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SeedBagItem extends Item {

	public static final List<BagSorter> SORTERS = new ArrayList<>();
	public static final BagSorter DEFAULT_SORTER = new BagSorter() {
		@Override
		public ResourceLocation getId() {
			return AgriApi.modLocation("default");
		}

		@Override
		public int compare(BagEntry entry1, BagEntry entry2) {
			int s1 = AgriStatRegistry.getInstance().stream().mapToInt(stat -> entry1.genome.getStatGene(stat).getTrait()).sum();
			int s2 = AgriStatRegistry.getInstance().stream().mapToInt(stat -> entry2.genome.getStatGene(stat).getTrait()).sum();
			if (s1 != s2) {
				return s2 - s1;
			}
			return AgriStatRegistry.getInstance().stream().mapToInt(stat -> {
				Integer d1 = entry1.genome.getStatGene(stat).getDominant().trait();
				Integer r1 = entry1.genome.getStatGene(stat).getRecessive().trait();
				Integer d2 = entry2.genome.getStatGene(stat).getDominant().trait();
				Integer r2 = entry2.genome.getStatGene(stat).getRecessive().trait();
				return (d2 + r2) - (d1 + r1);
			}).sum();
		}
	};

	static {
		SORTERS.add(DEFAULT_SORTER);
	}

	public SeedBagItem(Properties properties) {
		super(properties);
	}

	public static void addStatSorter(AgriStat stat) {
		SORTERS.add(new StatSorter(stat));
	}

	private static boolean plantOnCrop(AgriCrop crop, ItemStack seed) {
		if (crop.hasPlant() || crop.isCrossCropSticks()) {
			return false;
		}
		crop.plantGenome(AgriGenome.fromNBT(seed.get(ModDataComponents.GENOME.get())));
		return true;
	}

	public static int add(ItemStack seedBag, ItemStack insertedStack) {
		if (insertedStack.isEmpty() || !(insertedStack.getItem() instanceof AgriGenomeProviderItem seed)) {
			return 0;
		}
		Optional<AgriGenome> opt = seed.getGenome(insertedStack);
		if (opt.isEmpty()) {
			return 0;
		}
		AgriGenome genome = opt.get();
		if (seedBag.has(ModDataComponents.SPECIES.get())) {
			// bag already has seeds, we can add seeds only if they have the same species
			if (!genome.getSpeciesGene().getTrait().equals(seedBag.get(ModDataComponents.SPECIES.get()))) {
				return 0;
			}
		}
		// at this point, either there are no seeds in the bag, or the seed to be inserted has the same species as the ones inside
		if (!seedBag.has(ModDataComponents.SEEDS.get())) {
			seedBag.set(ModDataComponents.SEEDS.get(), new ArrayList<>());
			seedBag.set(ModDataComponents.SPECIES.get(), genome.getSpeciesGene().getTrait());
		}
		List<CompoundTag> seeds = seedBag.get(ModDataComponents.SEEDS.get());
		int size = size(seedBag);
		if (size >= CoreConfig.seedBagCapacity) {
			return 0;
		}
		// we should try to merge the genome to another one by increasing its count if they are the same
		CompoundTag seedTag = new CompoundTag();
		int insertedCount = Math.min(CoreConfig.seedBagCapacity - size, insertedStack.getCount());
		seedTag.putInt("count", insertedCount);
		genome.writeToNBT(seedTag);
		seeds.add(seedTag);
		seedBag.set(ModDataComponents.SEEDS.get(), seeds);
		sort(seedBag);
		return insertedCount;
	}

	public static ItemStack extractFirstStack(ItemStack seedBag) {
		List<CompoundTag> seeds = seedBag.get(ModDataComponents.SEEDS.get());
		BagEntry entry = BagEntry.fromNBT(seeds.get(0));
		ItemStack seed = AgriSeedItem.toStack(entry.genome);
		seed.setCount(entry.count);
		seeds.removeFirst();
		if (seeds.isEmpty()) {
			seedBag.remove(ModDataComponents.SEEDS.get());
			seedBag.remove(ModDataComponents.SPECIES.get());
		}
		seedBag.set(ModDataComponents.SEEDS.get(), seeds);
		return seed;
	}

	public static ItemStack extractFirstItem(ItemStack seedBag, boolean simulate) {
		List<CompoundTag> seeds = seedBag.get(ModDataComponents.SEEDS.get());
		CompoundTag seedTag = seeds.get(0);
		AgriGenome genome = AgriGenome.fromNBT(seedTag);
		ItemStack seed = AgriSeedItem.toStack(genome);
		if (!simulate) {
			int count = seedTag.getInt("count") - 1;
			seedTag.putInt("count", count);
			if (count <= 0) {
				seeds.removeFirst();
			}
			if (seeds.isEmpty()) {
				seedBag.remove(ModDataComponents.SEEDS.get());
				seedBag.remove(ModDataComponents.SPECIES.get());
			}
		}
		seedBag.set(ModDataComponents.SEEDS.get(), seeds);
		return seed;
	}

	public static void changeSorter(ItemStack seedBag, int delta) {
		if (delta == 0) {
			return;
		}
		int sorterIndex = 0;
		if (seedBag.has(ModDataComponents.SORTER.get())) {
			sorterIndex = seedBag.getOrDefault(ModDataComponents.SORTER.get(), 0);
		}
		sorterIndex += delta;
		if (sorterIndex < 0) {
			sorterIndex += SORTERS.size();
		}
		sorterIndex %= SORTERS.size();
		seedBag.set(ModDataComponents.SORTER.get(), sorterIndex);
		sort(seedBag);
	}

	private static void sort(ItemStack seedBag) {
		int sorterIndex = 0;
		if (seedBag.has(ModDataComponents.SORTER.get())) {
			sorterIndex = seedBag.getOrDefault(ModDataComponents.SORTER.get(), 0);
		}
		List<CompoundTag> seeds = seedBag.get(ModDataComponents.SEEDS.get());
		List<BagEntry> entries = new ArrayList<>();
		for (int i = 0; i < seeds.size(); i++) {
			entries.add(BagEntry.fromNBT(seeds.get(i)));
		}
		BagSorter sorter = SORTERS.get(sorterIndex);
		entries.sort(sorter);
		seeds.clear();
		for (BagEntry entry : entries) {
			CompoundTag t = new CompoundTag();
			entry.writeToNBT(t);
			seeds.add(t);
		}
		seedBag.set(ModDataComponents.SEEDS.get(), seeds);
	}

	public static int size(ItemStack seedBag) {
		if (!seedBag.has(ModDataComponents.SEEDS.get())) {
			return 0;
		}
		List<CompoundTag> seeds = seedBag.get(ModDataComponents.SEEDS.get());
		int count = 0;
		for (int i = 0; i < seeds.size(); i++) {
			count += seeds.get(i).getInt("count");
		}
		return count;
	}

	public static boolean isEmpty(ItemStack stack) {
		return stack.has(ModDataComponents.SPECIES.get());
	}

	public static boolean isFilled(ItemStack stack) {
		return stack.has(ModDataComponents.SEEDS.get()) && size(stack) == CoreConfig.seedBagCapacity;
	}

	private static void playRemoveOneSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	private static void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if (context.getHand() == InteractionHand.OFF_HAND) {
			return super.useOn(context);
		}
		ItemStack seedBag = context.getItemInHand();
		if (isEmpty(seedBag)) {
			return InteractionResult.PASS;
		}
		Level level = context.getLevel();
		ItemStack seed = extractFirstItem(seedBag, true);
		BlockPos pos = context.getClickedPos();
		Optional<AgriCrop> optional = AgriApi.getCrop(level, pos);
		// if we clicked on a crop stick
		if (optional.isPresent()) {
			if (plantOnCrop(optional.get(), seed)) {
				extractFirstItem(seedBag, false);
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		}
		// if we clicked on a soil
		optional = AgriApi.getCrop(level, pos.above());
		if (optional.isPresent()) {
			// if there's a crop above
			if (plantOnCrop(optional.get(), seed)) {
				extractFirstItem(seedBag, false);
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		} else if (CoreConfig.plantOffCropSticks) {
			// if there is nothing above
			if (AgriApi.getSoil(level, pos).isPresent() && level.getBlockState(pos.above()).isAir()) {
				level.setBlock(pos.above(), ModBlocks.CROP.get().defaultBlockState().setValue(CropBlock.CROP_STATE, CropState.PLANT), Block.UPDATE_ALL_IMMEDIATE);
				optional = AgriApi.getCrop(level, pos.above());
				if (optional.isPresent()) {
					optional.get().plantGenome(AgriGenome.fromNBT(seed.get(ModDataComponents.GENOME.get())), context.getPlayer());
					extractFirstItem(seedBag, false);
					return InteractionResult.SUCCESS;
				}
				return InteractionResult.PASS;
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack seedBag, Slot slot, ClickAction action, Player player) {
		// when you right-click on another stack with the bag
		if (seedBag.getCount() == 1 && action == ClickAction.SECONDARY) {
			ItemStack itemStack = slot.getItem();
			if (itemStack.isEmpty()) {
				if (!isEmpty(seedBag)) {
					playRemoveOneSound(player);
					ItemStack seed = extractFirstStack(seedBag);
					slot.safeInsert(seed);
				}
			} else if (itemStack.getItem().canFitInsideContainerItems()) {
				int inserted = add(seedBag, itemStack);
				slot.safeTake(itemStack.getCount(), inserted, player);
				if (inserted > 0) {
					playInsertSound(player);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack seedBag, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		// when you right-click on the bag with another stack
		if (seedBag.getCount() == 1 && action == ClickAction.SECONDARY && slot.allowModification(player)) {
			if (other.isEmpty()) {
				if (!isEmpty(seedBag)) {
					playRemoveOneSound(player);
					ItemStack seed = extractFirstStack(seedBag);
					access.set(seed);
				}
			} else if (other.getItem().canFitInsideContainerItems()) {
				int inserted = add(seedBag, other);
				if (inserted > 0) {
					playInsertSound(player);
					other.shrink(inserted);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		if (isEmpty(stack)) {
			tooltipComponents.add(Component.translatable("agricraft.tooltip.bag.empty").withStyle(ChatFormatting.DARK_GRAY));
		} else {
			if (stack.has(ModDataComponents.SPECIES.get())) {
				String species = stack.get(ModDataComponents.SPECIES.get());
				tooltipComponents.add(Component.translatable("agricraft.tooltip.bag.content", size(stack)).append(LangUtils.seedName(species)).withStyle(ChatFormatting.DARK_GRAY));
			}
		}
		int i = stack.getOrDefault(ModDataComponents.SORTER.get(), 0);
		String id = SORTERS.get(i).getId().toString().replace(":", ".");
		tooltipComponents.add(Component.translatable("agricraft.tooltip.bag.sorter")
				.append(Component.translatable("agricraft.tooltip.bag.sorter." + id))
				.withStyle(ChatFormatting.DARK_GRAY));
	}

	/**
	 * Sorts its entries by having the best in first, and the worst in last
	 */
	public interface BagSorter extends Comparator<BagEntry> {

		ResourceLocation getId();

	}

	public record BagEntry(int count, AgriGenome genome) {

		public static BagEntry fromNBT(CompoundTag tag) {
			return new BagEntry(tag.getInt("count"), AgriGenome.fromNBT(tag));
		}

		public void writeToNBT(CompoundTag tag) {
			tag.putInt("count", this.count);
			this.genome.writeToNBT(tag);
		}

	}

	public static class StatSorter implements BagSorter {

		private final AgriStat stat;
		private final ResourceLocation id;

		public StatSorter(AgriStat stat) {
			this.stat = stat;
			this.id = AgriApi.modLocation(stat.getId());
		}

		@Override
		public ResourceLocation getId() {
			return this.id;
		}

		@Override
		public int compare(BagEntry entry1, BagEntry entry2) {
			int s1 = entry1.genome.getStatGene(this.stat).getTrait();
			int s2 = entry2.genome.getStatGene(this.stat).getTrait();
			if (s1 == s2) {
				return DEFAULT_SORTER.compare(entry1, entry2);
			}
			return s2 - s1;
		}

	}

}
