package mcp.mobius.opis.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.logging.Level;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import mcp.mobius.mobiuscore.profiler.ProfilerSection;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.data.holders.newtypes.DataEntityRender;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntityRender;
import mcp.mobius.opis.data.profilers.ProfilerRenderEntity;
import mcp.mobius.opis.data.profilers.ProfilerRenderTileEntity;
import mcp.mobius.opis.swing.panels.PanelRenderEntities;
import mcp.mobius.opis.swing.panels.PanelRenderTileEnts;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;

public class OpisClientTickHandler implements ITickHandler {

	public long profilerUpdateTickCounter = 0;	
	public long profilerRunningTicks = 0;

	public static OpisClientTickHandler instance;
	
	public OpisClientTickHandler(){
		instance = this;
	}	
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.contains(TickType.CLIENT)){
			
			if (modOpis.profilerRunClient){
				((PanelRenderTileEnts)(TabPanelRegistrar.INSTANCE.getTab("opis.client.terender"))).getBtnRunRender().setText("Running...");
				((PanelRenderEntities)(TabPanelRegistrar.INSTANCE.getTab("opis.client.entrender"))).getBtnRunRender().setText("Running...");
			}
			else{
				((PanelRenderTileEnts)(TabPanelRegistrar.INSTANCE.getTab("opis.client.terender"))).getBtnRunRender().setText("Run Render");
				((PanelRenderEntities)(TabPanelRegistrar.INSTANCE.getTab("opis.client.entrender"))).getBtnRunRender().setText("Run Render");
			}
			
			profilerUpdateTickCounter++;
			
			if (profilerRunningTicks < modOpis.profilerMaxTicks && modOpis.profilerRunClient){
				profilerRunningTicks++;
			}else if (profilerRunningTicks >= modOpis.profilerMaxTicks && modOpis.profilerRunClient){
				profilerRunningTicks = 0;
				modOpis.profilerRunClient = false;
				ProfilerSection.desactivateAll();
				
				System.out.printf("Profiling done\n");

				ArrayList<DataTileEntityRender> tileEntData = new ArrayList<DataTileEntityRender>();
				double tileEntTotal = 0.0D;
				for (TileEntity te : ((ProfilerRenderTileEntity)ProfilerSection.RENDER_TILEENTITY.getProfiler()).data.keySet()){
					DataTileEntityRender dataTe = new DataTileEntityRender().fill(te);
					tileEntData.add(dataTe);
					tileEntTotal += dataTe.update.timing;
				}

				Collections.sort(tileEntData);
				((PanelRenderTileEnts)(TabPanelRegistrar.INSTANCE.getTab("opis.client.terender"))).setTable(tileEntData);
				((PanelRenderTileEnts)(TabPanelRegistrar.INSTANCE.getTab("opis.client.terender"))).getLblTotal().setText(String.format("Total : %.3f µs", tileEntTotal / 1000.0));
				
				ArrayList<DataEntityRender> entData = new ArrayList<DataEntityRender>();
				double entTotal = 0.0D;
				for (Entity ent : ((ProfilerRenderEntity)ProfilerSection.RENDER_ENTITY.getProfiler()).data.keySet()){
					DataEntityRender dataEnt = new DataEntityRender().fill(ent);
					entData.add(dataEnt);
					entTotal += dataEnt.update.timing;
				}
				
				Collections.sort(entData);
				((PanelRenderEntities)(TabPanelRegistrar.INSTANCE.getTab("opis.client.entrender"))).setTable(entData);
				((PanelRenderEntities)(TabPanelRegistrar.INSTANCE.getTab("opis.client.entrender"))).getLblTotal().setText(String.format("Total : %.3f µs", entTotal / 1000.0));				
			}		
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "opis.client.tickhandler";
	}

}
