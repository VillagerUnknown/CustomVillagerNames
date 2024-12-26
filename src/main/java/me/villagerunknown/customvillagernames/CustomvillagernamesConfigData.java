package me.villagerunknown.customvillagernames;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "villagerunknown-customvillagernames")
public class CustomvillagernamesConfigData implements me.shedaniel.autoconfig.ConfigData {
	
	/**
	 * Villagers
	 */
	
	@ConfigEntry.Category("Villagers")
	public float chanceToPrependTextToName = 0F;
	
	@ConfigEntry.Category("Villagers")
	public String PrependText = "Villager ";
	
	@ConfigEntry.Category("Villagers")
	public boolean prependProfessionToName = true;
	
	@ConfigEntry.Category("Villagers")
	public float chanceToAppendSrToAdults = 0.05F;
	
	@ConfigEntry.Category("Villagers")
	public String SrText = " Sr.";
	
	@ConfigEntry.Category("Villagers")
	public float chanceToAppendJrToChildren = 0.05F;
	
	@ConfigEntry.Category("Villagers")
	public String JrText = " Jr.";
	
}
