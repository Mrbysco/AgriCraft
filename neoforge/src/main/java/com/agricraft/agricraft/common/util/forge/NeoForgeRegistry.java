package com.agricraft.agricraft.common.util.forge;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.common.util.PlatformRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeRegistry<T> implements PlatformRegistry<T> {

	private final DeferredRegister<T> registry;

	public NeoForgeRegistry(Registry<T> registry, String modid) {
		this.registry = DeferredRegister.create(registry.key(), modid);
	}

	@Override
	public <I extends T> Entry<I> register(String id, Supplier<I> supplier) {
		DeferredHolder<T, I> object = this.registry.register(id, supplier);
		return new NeoForgeRegistryEntry<>(object);
	}

	@Override
	public void init() {
		final var containerOpt = ModList.get().getModContainerById(AgriApi.MOD_ID);
		if (containerOpt.isEmpty())
			throw new NullPointerException("Cannot find mod container for id " + AgriApi.MOD_ID);
		final var cont = containerOpt.get();
		if (cont instanceof FMLModContainer fmlModContainer) {
			this.registry.register(fmlModContainer.getEventBus());
		} else {
			throw new ClassCastException("The container of the mod " + AgriApi.MOD_ID + " is not a FML one!");
		}
	}

	public static class NeoForgeRegistryEntry<T, R extends T> implements Entry<R> {

		private final DeferredHolder<T, R> object;

		public NeoForgeRegistryEntry(DeferredHolder<T, R> object) {
			this.object = object;
		}

		@Override
		public ResourceLocation id() {
			return this.object.getId();
		}

		@Override
		public R get() {
			return this.object.get();
		}

	}

}
