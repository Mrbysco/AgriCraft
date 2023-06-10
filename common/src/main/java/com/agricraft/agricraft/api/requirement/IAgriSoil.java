package com.agricraft.agricraft.api.requirement;

import com.agricraft.agricraft.api.util.IAgriDisplayable;
import com.agricraft.agricraft.api.util.IAgriRegisterable;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Class for interacting with AgriCraft soil definitions.
 *
 * Retrieve instances from the World using AgriApi.getSoil(world, pos)
 * Retrieve instances from a Block state using ISoilRegistry.valueOf(state)
 */
public interface IAgriSoil extends IAgriRegisterable<IAgriSoil>, IAgriDisplayable {

	@NotNull
	@Override
	String getId();

	@NotNull
	Component getName();

	@NotNull
	Collection<BlockState> getVariants();

	@NotNull
	Humidity getHumidity();

	@NotNull
	Acidity getAcidity();

	@NotNull
	Nutrients getNutrients();

	double getGrowthModifier();

	boolean isSoil();

	default boolean isVariant(@NotNull BlockState state) {
		return this.getVariants().contains(state);
	}

	@Override
	default void addDisplayInfo(@NotNull Consumer<Component> consumer) {
		consumer.accept(Tooltips.SOIL);
		consumer.accept(Tooltips.tooltipHumidity(this));
		consumer.accept(Tooltips.tooltipAcidity(this));
		consumer.accept(Tooltips.tooltipNutrients(this));
	}

	/**
	 * Enum for describing the humidity property of soils
	 */
	enum Humidity implements SoilProperty {
		ARID,
		DRY,
		DAMP("moist"),
		WET("standard", "default"),
		WATERY,
		FLOODED,
		INVALID;

		private final Component textComponent;
		private final List<String> synonyms;

		Humidity(String... synonyms) {
			this.textComponent = Component.translatable("agricraft.tooltip.humidity." + this.name().toLowerCase());
			ImmutableList.Builder<String> builder = ImmutableList.builder();
			builder.add(this.name());
			builder.add(synonyms);
			this.synonyms = builder.build();
		}

		@Override
		public Component getDescription() {
			return this.textComponent;
		}

		@Override
		public boolean isValid() {
			return this != INVALID;
		}

		@Override
		public List<String> getSynonyms() {
			return this.synonyms;
		}

		public static Optional<Humidity> fromString(final String string) {
			return Arrays.stream(values()).filter(value -> value.isSynonym(string)).findAny();
		}
	}

	/**
	 * Enum for describing the acidity property of soils
	 */
	enum Acidity implements SoilProperty {
		HIGHLY_ACIDIC("0", "1", "2", "highly-acidic", "highly acidic", "very-acidic", "very acidic", "very_acidic"),
		ACIDIC("3", "4", "5"),
		SLIGHTLY_ACIDIC("6", "slightly-acidic", "slightly acidic", "standard", "default"),
		NEUTRAL("7"),
		SLIGHTLY_ALKALINE("8", "slightly-alkaline", "slightly alkaline"),
		ALKALINE("9", "10", "11"),
		HIGHLY_ALKALINE("12", "13", "14", "highly-alkaline", "highly alkaline", "very-alkaline", "very alkaline", "very_alkaline"),
		INVALID;

		private final Component textComponent;
		private final List<String> synonyms;

		Acidity(String... synonyms) {
			this.textComponent = Component.translatable("agricraft.tooltip.acidity." + this.name().toLowerCase());
			ImmutableList.Builder<String> builder = ImmutableList.builder();
			builder.add(this.name());
			builder.add(synonyms);
			this.synonyms = builder.build();
		}

		@Override
		public Component getDescription() {
			return this.textComponent;
		}

		@Override
		public boolean isValid() {
			return this != INVALID;
		}

		@Override
		public List<String> getSynonyms() {
			return this.synonyms;
		}

		public static Optional<Acidity> fromString(final String string) {
			return Arrays.stream(values()).filter(value -> value.isSynonym(string)).findAny();
		}
	}

	/**
	 * Enum for describing the nutrients property of soils
	 */
	enum Nutrients implements SoilProperty {
		NONE("zero", "empty"),
		VERY_LOW("scarce", "poor"),
		LOW,
		MEDIUM("normal", "average"),
		HIGH("standard", "default"),
		VERY_HIGH("rich"),
		INVALID;

		private final Component textComponent;
		private final List<String> synonyms;

		Nutrients(String... synonyms) {
			this.textComponent = Component.translatable("agricraft.tooltip.nutrients." + this.name().toLowerCase());
			ImmutableList.Builder<String> builder = ImmutableList.builder();
			builder.add(this.name());
			builder.add(synonyms);
			this.synonyms = builder.build();
		}

		@Override
		public Component getDescription() {
			return this.textComponent;
		}

		@Override
		public boolean isValid() {
			return this != INVALID;
		}

		@Override
		public List<String> getSynonyms() {
			return this.synonyms;
		}

		public static Optional<Nutrients> fromString(final String string) {
			return Arrays.stream(values()).filter(value -> value.isSynonym(string)).findAny();
		}
	}

	interface SoilProperty {

		Component getDescription();

		List<String> getSynonyms();

		boolean isValid();

		default boolean isSynonym(String string) {
			return this.getSynonyms().stream().anyMatch(string::equalsIgnoreCase);
		}

		String name();

		int ordinal();

	}

	final class Tooltips {

		private Tooltips() {
		}

		public static final Component SOIL = Component.translatable("agricraft.tooltip.soil").withStyle(ChatFormatting.DARK_GRAY);

		public static final Component HUMIDITY = Component.translatable("agricraft.tooltip.humidity").withStyle(ChatFormatting.DARK_GRAY);
		public static final Component ACIDITY = Component.translatable("agricraft.tooltip.acidity").withStyle(ChatFormatting.DARK_GRAY);
		public static final Component NUTRIENTS = Component.translatable("agricraft.tooltip.nutrients").withStyle(ChatFormatting.DARK_GRAY);

		private static Component tooltipHumidity(IAgriSoil soil) {
			return Component.literal(" - ")
					.append(HUMIDITY).append(Component.literal(": "))
					.append(soil.getHumidity().getDescription())
					.withStyle(ChatFormatting.DARK_GRAY);
		}

		private static Component tooltipAcidity(IAgriSoil soil) {
			return Component.literal(" - ")
					.append(ACIDITY).append(Component.literal(": "))
					.append(soil.getAcidity().getDescription())
					.withStyle(ChatFormatting.DARK_GRAY);
		}

		private static Component tooltipNutrients(IAgriSoil soil) {
			return Component.literal(" - ")
					.append(NUTRIENTS).append(Component.literal(": "))
					.append(soil.getNutrients().getDescription())
					.withStyle(ChatFormatting.DARK_GRAY);
		}

	}

}
