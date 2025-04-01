package me.villagerunknown.customvillagernames.feature;

import me.villagerunknown.customvillagernames.Customvillagernames;
import me.villagerunknown.platform.Platform;
import me.villagerunknown.platform.builder.StringsListBuilder;
import me.villagerunknown.platform.util.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.village.VillagerProfession;

import java.util.List;
import java.util.Optional;

public class randomVillagerNamesFeature {
	
	private static final String FILENAME = Customvillagernames.MOD_ID + "-list.json";
	private static final List<String> NAMES_LIST = ListUtil.HUMAN_NAMES;
	
	private static StringsListBuilder names = new StringsListBuilder( FILENAME, NAMES_LIST );
	
	public static void execute() {
		// Add reload event
		Platform.LOAD.add( randomVillagerNamesFeature::reload );
		
		// Register with Server Tick Events
		ServerTickEvents.START_SERVER_TICK.register( (MinecraftServer server) -> {
			for (ServerWorld world : server.getWorlds()) {
				List<VillagerEntity> villagers = WorldUtil.getEntitiesByType( world, VillagerEntity.class );
				
				for (VillagerEntity villager : villagers) {
					randomizeName( villager );
				} // for
			} // for
		});
	}
	
	public static void reload() {
		Customvillagernames.LOGGER.info( "Reloading " + FILENAME );
		names = new StringsListBuilder( FILENAME, NAMES_LIST );
	}
	
	private static void randomizeName( VillagerEntity villager ) {
		Optional<RegistryKey<VillagerProfession>> professionKey = villager.getVillagerData().profession().getKey();
		String profession = replaceProfessionsFeature.NO_PROFESSION;
		
		if( professionKey.isPresent() ) {
			VillagerProfession professionRegistration = Registries.VILLAGER_PROFESSION.get(professionKey.get());
			if( null != professionRegistration ) {
				profession = replaceProfessionsFeature.getProfession(
						professionRegistration.id().toString()
								.replace( "translation{key='entity.minecraft.villager.", "" )
								.replace( "', args=[]}", "" )
				);
			} // if
		} // if
		
		String professionCapitalized = StringUtil.capitalizeAll( profession );
		String name = names.getRandomString();
		
		if( villager.getCustomName() == null ) {
			// # Villager does not have a name.
			
			if(Customvillagernames.CONFIG.chanceToPrependTextToName > 0 && MathUtil.hasChance( Customvillagernames.CONFIG.chanceToPrependTextToName )) {
				name = Customvillagernames.CONFIG.PrependText + name;
			} // if
			
			if(Customvillagernames.CONFIG.prependProfessionToName && !profession.equals(replaceProfessionsFeature.NO_PROFESSION)) {
				name = professionCapitalized + " " + name;
			} // if
			
			if( Customvillagernames.CONFIG.chanceToAppendSrToAdults > 0 && MathUtil.hasChance( Customvillagernames.CONFIG.chanceToAppendSrToAdults ) && !villager.isBaby() && !name.contains("Sr.")) {
				name = name + Customvillagernames.CONFIG.SrText;
			} else if( Customvillagernames.CONFIG.chanceToAppendJrToChildren > 0 && MathUtil.hasChance( Customvillagernames.CONFIG.chanceToAppendJrToChildren ) && villager.isBaby() && !name.contains("Jr.")) {
				name = name + Customvillagernames.CONFIG.JrText;
			} // if, else if
			
			villager.setCustomName( Text.of(name) );
		} else {
			// # Villager has a name.
			
			String existingName = villager.getCustomName().toString().replace("literal{","").replace("}","");
			String newName = "";
			
			if( existingName.isEmpty() || null == existingName ) {
				return;
			} // if
			
			if( existingName.contains( " " ) ) {
				// # Villager's name has a space in it.
				
				var nameParts = existingName.split(" ");
				
//				String[] namePartsNoProfession = ArrayUtil.removeFirstElement( nameParts );
				String gluedNamePartsNoProfession = ArrayUtil.joinValues( nameParts, " " );
				
//				List<String> professionStrings = ListUtil.VILLAGER_PROFESSION_STRINGS;
				List<String> professionStrings = replaceProfessionsFeature.getKeysList();
				
//				boolean nameContainsProfession = professionStrings.contains( nameParts[0].toLowerCase().trim() );
				boolean nameContainsProfession = false;
				
				for (String professionString : professionStrings) {
					String capitalizedProfession = replaceProfessionsFeature.getProfessionCapitalized( professionString );
					
					if( existingName.contains( capitalizedProfession ) ) {
						nameContainsProfession = true;
						gluedNamePartsNoProfession = existingName.replace( capitalizedProfession, "" ).trim();
					} // if
				} // for
				
				if( nameContainsProfession ) {
					// # Villager has a valid profession in their name.
					
					if( profession.equals(replaceProfessionsFeature.NO_PROFESSION) ) {
						// # Villager has no profession.
						
						newName = gluedNamePartsNoProfession;
					} else if( !profession.equals(replaceProfessionsFeature.NO_PROFESSION) && !nameParts[0].toLowerCase().equals(profession) ){
						// # Villager's active profession is different from the profession in their name.
						
						newName = professionCapitalized + " " + gluedNamePartsNoProfession;
					} else {
						// # Villager's active profession is in their name, or the villager has no profession.
						
						newName = existingName;
					} // if, else
					
				} else {
					// # Villager doesn't have a valid profession in their name.
					
					if( Customvillagernames.CONFIG.prependProfessionToName && !profession.equals(replaceProfessionsFeature.NO_PROFESSION) && !existingName.contains( professionCapitalized ) ) {
						// # Villager has a profession.
						
						newName = professionCapitalized + " " + existingName;
					} else {
						// # Villager doesn't have a profession.
						
						newName = existingName;
					} // if, else
					
				} // if, else
				
			} else {
				// # Villager's name does not have a space.
				
				if( Customvillagernames.CONFIG.prependProfessionToName && !profession.equals(replaceProfessionsFeature.NO_PROFESSION)) {
					// # Villager has a profession.
					
					newName = professionCapitalized + " " + existingName;
					
				} else {
					// # Villager has no profession.
					
					newName = existingName;
				}
				
			} // if
			
			if( !newName.equals(existingName) ) {
				villager.setCustomName(Text.of(newName.trim()));
			} // if
			
		} // if, else
		
	}
	
}
