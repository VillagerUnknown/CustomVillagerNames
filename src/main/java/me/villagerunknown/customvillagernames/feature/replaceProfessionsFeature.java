package me.villagerunknown.customvillagernames.feature;

import me.villagerunknown.customvillagernames.Customvillagernames;
import me.villagerunknown.platform.Platform;
import me.villagerunknown.platform.builder.StringsMapBuilder;
import me.villagerunknown.platform.util.ListUtil;
import me.villagerunknown.platform.util.StringUtil;

import java.util.List;

public class replaceProfessionsFeature {
	
	private static final String FILENAME = Customvillagernames.MOD_ID + "-professions.json";
	private static final List<String> PROFESSIONS_LIST = ListUtil.VILLAGER_PROFESSION_STRINGS;
	
	private static StringsMapBuilder professions = new StringsMapBuilder( FILENAME, PROFESSIONS_LIST );
	
	public static void execute() {
		// Add reload event
		Platform.LOAD.add( replaceProfessionsFeature::reload );
	}
	
	public static void reload() {
		Customvillagernames.LOGGER.info( "Reloading " + FILENAME );
		professions = new StringsMapBuilder( FILENAME, PROFESSIONS_LIST );
	}
	
	public static List<String> getKeysList() {
		return professions.getMap().keySet().stream().toList();
	}
	
	public static List<String> getValuesList() {
		return professions.getMap().values().stream().toList();
	}
	
	public static String getProfession( String professionKey ) {
		if( professions.getMap().containsKey( professionKey ) ) {
			return professions.getString( professionKey );
		} // if
		
		return "none";
	}
	
	public static String getProfessionCapitalized( String professionKey ) {
		return StringUtil.capitalizeAll( getProfession( professionKey ) );
	}
	
}
