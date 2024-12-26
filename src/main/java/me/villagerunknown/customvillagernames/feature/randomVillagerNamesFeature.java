package me.villagerunknown.customvillagernames.feature;

import me.villagerunknown.customvillagernames.Customvillagernames;
import me.villagerunknown.platform.Platform;
import me.villagerunknown.platform.list.StringsList;
import me.villagerunknown.platform.util.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;

import java.util.List;

public class randomVillagerNamesFeature {
	
	private static final String FILENAME = Customvillagernames.MOD_ID + "-list.json";
	private static final List<String> NAMES_LIST = ListUtil.HUMAN_NAMES;
	
	private static StringsList names = new StringsList( FILENAME, NAMES_LIST );
	
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
		names = new StringsList( FILENAME, NAMES_LIST );
	}
	
	private static void randomizeName( VillagerEntity villager ) {
		String profession = villager.getVillagerData().getProfession().toString().toLowerCase();
		String professionCapitalized = StringUtil.capitalize( profession );
		String name = names.getRandomString();
		
		if( villager.getCustomName() == null ) {
			// # Villager does not have a name.
			
			if(Customvillagernames.CONFIG.chanceToPrependTextToName > 0 && MathUtil.hasChance( Customvillagernames.CONFIG.chanceToPrependTextToName )) {
				name = Customvillagernames.CONFIG.PrependText + name;
			} // if
			
			if(Customvillagernames.CONFIG.prependProfessionToName && !profession.equals("none")) {
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
				
				String[] namePartsNoProfession = ArrayUtil.removeFirstElement( nameParts );
				String gluedNamePartsNoProfession = ArrayUtil.joinValues( namePartsNoProfession, " " );
				
				if( ListUtil.VILLAGER_PROFESSION_STRINGS.contains( nameParts[0].toLowerCase().trim() ) ) {
					// # Villager has a valid profession in their name.
					
					if( profession.equals("none") ) {
						// # Villager has no profession.
						
						newName = gluedNamePartsNoProfession;
					} else if( !profession.equals("none") && !nameParts[0].toLowerCase().equals(profession) ){
						// # Villager's active profession is different from the profession in their name.
						
						newName = professionCapitalized + " " + gluedNamePartsNoProfession;
					} else {
						// # Villager's active profession is in their name, or the villager has no profession.
						
						newName = existingName;
					} // if, else
					
				} else {
					// # Villager doesn't have a valid profession in their name.
					
					if( Customvillagernames.CONFIG.prependProfessionToName && !profession.equals("none") && !existingName.contains( professionCapitalized ) ) {
						// # Villager has a profession.
						
						newName = professionCapitalized + " " + existingName; // this is the issue!
					} else {
						// # Villager doesn't have a profession.
						
						newName = existingName;
					} // if, else
					
				} // if, else
				
			} else {
				// # Villager's name does not have a space.
				
				if( Customvillagernames.CONFIG.prependProfessionToName && !profession.equals("none")) {
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
