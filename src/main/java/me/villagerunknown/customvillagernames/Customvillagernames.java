package me.villagerunknown.customvillagernames;

import me.villagerunknown.customvillagernames.feature.randomVillagerNamesFeature;
import me.villagerunknown.customvillagernames.feature.replaceProfessionsFeature;
import me.villagerunknown.customvillagernames.feature.resetVillagerNameFeature;
import me.villagerunknown.platform.Platform;
import me.villagerunknown.platform.PlatformMod;
import me.villagerunknown.platform.manager.featureManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class Customvillagernames implements ModInitializer {

	public static PlatformMod<CustomvillagernamesConfigData> MOD = Platform.register( "customvillagernames", Customvillagernames.class, CustomvillagernamesConfigData.class );
	public static String MOD_ID = MOD.getModId();
	public static Logger LOGGER = MOD.getLogger();
	public static CustomvillagernamesConfigData CONFIG = MOD.getConfig();
	
	@Override
	public void onInitialize() {
		// # Register mod with Platform
		Platform.init_mod( MOD );
		
		// # Add Features
		featureManager.addFeature( "replace-professions", replaceProfessionsFeature::execute );
		featureManager.addFeature( "random-villager-names", randomVillagerNamesFeature::execute );
		featureManager.addFeature( "reset-villager-names", resetVillagerNameFeature::execute );
		
		// # Load Features
		featureManager.loadFeatures();
	}
	
}
