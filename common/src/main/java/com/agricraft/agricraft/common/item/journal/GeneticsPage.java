package com.agricraft.agricraft.common.item.journal;

import com.agricraft.agricraft.api.AgriApi;
import com.agricraft.agricraft.api.tools.journal.JournalPage;
import net.minecraft.resources.ResourceLocation;

public class GeneticsPage implements JournalPage {

	public static final ResourceLocation ID = AgriApi.modLocation("genetics_page");

	@Override
	public ResourceLocation getDrawerId() {
		return ID;
	}

}
