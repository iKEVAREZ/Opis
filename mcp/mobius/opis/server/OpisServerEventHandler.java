package mcp.mobius.opis.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityEvent;

public class OpisServerEventHandler {

	public static boolean printEntityTrace = false;
	public static boolean printEntityFull  = false;
	
	@ForgeSubscribe
    @SideOnly(Side.SERVER)
    public void onEntityConstructed(EntityEvent.EntityConstructing event) {
		if (printEntityTrace){

			if (event.entity instanceof EntityItem){
				System.out.printf("Entity %s of type %s [ %s ] created\n", event.entity.entityId, event.entity.getClass().getName(), ((EntityItem)(event.entity)).getEntityItem().getDisplayName());
				
				
			} else {
				System.out.printf("Entity %s of type %s created\n", event.entity.entityId, event.entity.getClass().getName());
			}
			
			
			if (printEntityFull){
				try{
					throw new RuntimeException();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
