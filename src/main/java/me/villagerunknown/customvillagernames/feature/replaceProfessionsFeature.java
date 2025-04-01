package me.villagerunknown.customvillagernames.feature;

import me.villagerunknown.customvillagernames.Customvillagernames;
import me.villagerunknown.platform.Platform;
import me.villagerunknown.platform.builder.StringsMapBuilder;
import me.villagerunknown.platform.util.ListUtil;
import me.villagerunknown.platform.util.StringUtil;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import org.apache.http.config.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class replaceProfessionsFeature {
	
	public static final String NO_PROFESSION = "none";
	
	private static final String FILENAME = Customvillagernames.MOD_ID + "-professions.json";
	private static final List<String> PROFESSIONS_LIST = new ArrayList<>();
	
	private static StringsMapBuilder professions = null;
	
	public static void execute() {
		init();
		
		// Add reload event
		Platform.LOAD.add( replaceProfessionsFeature::reload );
	}
	
	private static void init() {
		for( Identifier villagerProfessionId : Registries.VILLAGER_PROFESSION.getIds() ) {
			if( villagerProfessionId.getPath().toLowerCase().contains( NO_PROFESSION ) ) {
				continue;
			} // if
			
			PROFESSIONS_LIST.add( villagerProfessionId.getPath().toLowerCase() );
		} // for
		
		professions = new StringsMapBuilder( FILENAME, PROFESSIONS_LIST );
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
		String parsedProfessionKey = professionKey.replaceAll( "([-_]+)", " " );
		
		if( professions.getMap().containsKey( professionKey ) ) {
			String replacedProfession = professions.getString( professionKey );
			
			if( Objects.equals(replacedProfession, professionKey) ) {
				return parsedProfessionKey;
			} // if
			
			return replacedProfession;
		} // if
		
		return parsedProfessionKey;
	}
	
	public static String getProfessionCapitalized( String professionKey ) {
		return StringUtil.capitalizeAll( getProfession( professionKey ) );
	}
	
}
