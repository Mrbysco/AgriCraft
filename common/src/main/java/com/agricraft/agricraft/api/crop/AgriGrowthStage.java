package com.agricraft.agricraft.api.crop;

import com.agricraft.agricraft.api.config.CoreConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;

public class AgriGrowthStage {
	public static final Codec<AgriGrowthStage> CODEC = RecordCodecBuilder.create(inst -> inst.group(
					Codec.INT.optionalFieldOf("index", 0).forGetter(AgriGrowthStage::index),
					Codec.INT.optionalFieldOf("total", 0).forGetter(AgriGrowthStage::total)
			)
			.apply(inst, AgriGrowthStage::new));
	public static final StreamCodec<FriendlyByteBuf, AgriGrowthStage> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT,
			p -> p.stage,
			ByteBufCodecs.VAR_INT,
			p -> p.total,
			AgriGrowthStage::new
	);

	private final int stage;
	private final int total;

	public AgriGrowthStage(int stage, int total) {
		this.stage = stage;
		this.total = total;
	}

	public int index() {
		return this.stage;
	}

	public int total() {
		return this.total;
	}

	public boolean isMature() {
		return this.isFinal();
	}

	public boolean isFinal() {
		return this.stage >= this.total - 1;
	}

	public boolean canDropSeed() {
		return this.isFinal() || !CoreConfig.onlyMatureSeedDrops;
	}

	public AgriGrowthStage getNext(AgriCrop crop, RandomSource random) {
		return this.isFinal() ? this : new AgriGrowthStage(this.stage + 1, this.total);
	}

	public AgriGrowthStage getPrevious(AgriCrop crop, RandomSource random) {
		return this.stage <= 0 ? this : new AgriGrowthStage(this.stage - 1, this.total);
	}

	/**
	 * @return the growth as a value between 0 and 1
	 */
	public double growthPercentage() {
		return (this.stage + 1.0) / this.total;
	}

	@Override
	public String toString() {
		return "AgriGrowthStage{" +
				"stage=" + stage +
				", total=" + total +
				'}';
	}

}
