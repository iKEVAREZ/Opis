package mcp.mobius.opis.events;

import mcp.mobius.opis.network.rcon.nexus.NexusClient;
import mcp.mobius.opis.network.rcon.server.RConServer;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OpisServerEventHandler {

	public static boolean printEntityTrace = false;
	public static boolean printEntityFull  = false;
	
	public static boolean firstLoad = false;
	
	@SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onWorldLoad(WorldEvent.Load event) {
		
		if (!firstLoad && event.world.provider.dimensionId == 0){
			firstLoad = true;
			
			Thread rconserver = new Thread(RConServer.instance);
			rconserver.setName("Opis Server");
			rconserver.start();

			Thread nexusclient = new Thread(NexusClient.instance);
			nexusclient.setName("Nexus Client");
			nexusclient.start();			
			
		}
		
	}
	
	
	/*
	@SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onEntityConstructed(EntityEvent.EntityConstructing event) {
		if (printEntityTrace){

			if (event.entity instanceof EntityItem){
				System.out.printf("Entity %s of type %s [ %s ] created\n", event.entity.getEntityId(), event.entity.getClass().getName(), ((EntityItem)(event.entity)).getEntityItem().getDisplayName());
				
				
			} else {
				System.out.printf("Entity %s of type %s created\n", event.entity.getEntityId(), event.entity.getClass().getName());
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
	*/
}
