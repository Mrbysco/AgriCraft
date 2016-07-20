package com.infinityraider.agricraft.compat.jei;

import com.infinityraider.agricraft.apiimpl.MutationRegistry;
import com.infinityraider.agricraft.apiimpl.PlantRegistry;
import com.infinityraider.agricraft.compat.jei.mutation.MutationRecipeCategory;
import com.infinityraider.agricraft.compat.jei.mutation.MutationRecipeHandler;
import com.infinityraider.agricraft.compat.jei.produce.ProduceRecipeCategory;
import com.infinityraider.agricraft.compat.jei.produce.ProduceRecipeHandler;
import com.infinityraider.agricraft.init.AgriItems;
import javax.annotation.Nonnull;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import java.util.HashMap;
import java.util.Map;
import mezz.jei.api.IJeiHelpers;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class AgriCraftJEIPlugin implements IModPlugin {

	public static final String CATEGORY_MUTATION = "agricraft.mutation";
	public static final String CATEGORY_PRODUCE = "agricraft.produce";

	private static final Map<Item, String[]> nbtIgnores = new HashMap<>();

	private static IJeiRuntime jeiRuntime;
	private static IJeiHelpers jeiHelpers;

	@Override
	public void register(@Nonnull IModRegistry registry) {

		jeiHelpers = registry.getJeiHelpers();

		registry.addRecipeCategories(
				new MutationRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ProduceRecipeCategory(registry.getJeiHelpers().getGuiHelper())
		);

		registry.addRecipeHandlers(
				new MutationRecipeHandler(),
				new ProduceRecipeHandler()
		);

		registry.addRecipeCategoryCraftingItem(new ItemStack(AgriItems.CROPS), CATEGORY_MUTATION, CATEGORY_PRODUCE);

		for (Map.Entry<Item, String[]> nbt : nbtIgnores.entrySet()) {
			jeiHelpers.getNbtIgnoreList().ignoreNbtTagNames(nbt.getKey(), nbt.getValue());
		}

	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntimeInstance) {
		jeiRuntime = jeiRuntimeInstance;
		PlantRegistry.getInstance().getPlants().forEach(jeiRuntime.getRecipeRegistry()::addRecipe);
		MutationRegistry.getInstance().getMutations().forEach(jeiRuntime.getRecipeRegistry()::addRecipe);
	}

}
