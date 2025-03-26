package me.villagerunknown.customvillagernames.feature;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public class resetVillagerNameFeature {
	
	public static void execute() {
		registerVillagerNameResetEvent();
	}
	
	private static void registerVillagerNameResetEvent() {
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult ) -> {
			if( world.isClient() ) {
				return ActionResult.PASS;
			} // if
			
			if( entity.getType().equals( EntityType.VILLAGER ) ) {
				ItemStack itemStack = player.getStackInHand( hand );
				VillagerEntity villager = (VillagerEntity) entity;
				
				if( itemStack.getItem().equals( Items.NAME_TAG ) && itemStack.getComponents().contains( DataComponentTypes.CUSTOM_NAME ) ) {
					itemStack.decrementUnlessCreative( 1, player );
					villager.setCustomName(Text.of(""));
					
					return ActionResult.PASS;
				} // if
			} // if
			
			return ActionResult.PASS;
		});
	}
	
}
