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

	public static PlatformMod<CustomvillagernamesConfigData> MOD = null;
	public static String MOD_ID = null;
	public static Logger LOGGER = null;
	public static CustomvillagernamesConfigData CONFIG = null;
	
	@Override
	public void onInitialize() {
		// # Register Mod w/ Platform
		MOD = Platform.register( "customvillagernames", Customvillagernames.class, CustomvillagernamesConfigData.class );
		
		MOD_ID = MOD.getModId();
		LOGGER = MOD.getLogger();
		CONFIG = MOD.getConfig();
		
		// # Initialize Mod
		init();
	}
	
	private static void init(){
		Platform.init_mod( MOD );
		
		// # Add Features
		featureManager.addFeature( "replaceProfessions", replaceProfessionsFeature::execute );
		featureManager.addFeature( "randomVillagerNames", randomVillagerNamesFeature::execute );
		featureManager.addFeature( "resetVillagerNames", resetVillagerNameFeature::execute );
	}
	
}
