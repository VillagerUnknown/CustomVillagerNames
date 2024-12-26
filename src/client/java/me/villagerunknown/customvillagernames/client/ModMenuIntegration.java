package me.villagerunknown.customvillagernames.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.villagerunknown.customvillagernames.CustomvillagernamesConfigData;

public class ModMenuIntegration implements ModMenuApi {
	
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(CustomvillagernamesConfigData.class, parent).get();
	}
	
}
