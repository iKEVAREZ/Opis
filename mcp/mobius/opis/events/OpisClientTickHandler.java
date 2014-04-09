package mcp.mobius.opis.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.logging.Level;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import mcp.mobius.mobiuscore.profiler.ProfilerSection;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.newtypes.DataBlockRender;
import mcp.mobius.opis.data.holders.newtypes.DataEntityRender;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntityRender;
import mcp.mobius.opis.data.profilers.ProfilerRenderBlock;
import mcp.mobius.opis.data.profilers.ProfilerRenderEntity;
import mcp.mobius.opis.data.profilers.ProfilerRenderTileEntity;
import mcp.mobius.opis.swing.panels.PanelRenderEntities;
import mcp.mobius.opis.swing.panels.PanelRenderTileEnts;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public enum OpisClientTickHandler implements ITickHandler {
	INSTANCE;
	
	public long profilerUpdateTickCounter = 0;	
	public long profilerRunningTicks = 0;
	public long timer1000 = System.nanoTime();
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.contains(TickType.CLIENT)){
			
			// One second timer
			if (System.nanoTime() - timer1000 > 1000000000L){
				timer1000 = System.nanoTime();
			}
			
			
			
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
				ProfilerSection.desactivateAll(Side.CLIENT);
				
				System.out.printf("Profiling done\n");

				this.updateTabs();
				
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

	private void updateTabs(){
		//====================================================================================				
		
		ArrayList<DataTileEntityRender> tileEntData = new ArrayList<DataTileEntityRender>();
		double tileEntTotal = 0.0D;
		for (TileEntity te : ((ProfilerRenderTileEntity)ProfilerSection.RENDER_TILEENTITY.getProfiler()).data.keySet()){
			try{
				DataTileEntityRender dataTe = new DataTileEntityRender().fill(te);
				tileEntData.add(dataTe);
				tileEntTotal += dataTe.update.timing;
			} catch (Exception e) {
				modOpis.log.warning(String.format("Error while adding entity %s to the list", te));
			}
		}

		System.out.printf("Rendered %d TileEntities\n", tileEntData.size());
		
		Collections.sort(tileEntData);
		((PanelRenderTileEnts)(TabPanelRegistrar.INSTANCE.getTab("opis.client.terender"))).setTable(tileEntData);
		((PanelRenderTileEnts)(TabPanelRegistrar.INSTANCE.getTab("opis.client.terender"))).getLblTotal().setText(String.format("Total : %.3f µs", tileEntTotal / 1000.0));
		
		//====================================================================================
		
		ArrayList<DataEntityRender> entData = new ArrayList<DataEntityRender>();
		double entTotal = 0.0D;
		for (Entity ent : ((ProfilerRenderEntity)ProfilerSection.RENDER_ENTITY.getProfiler()).data.keySet()){
			try{
				DataEntityRender dataEnt = new DataEntityRender().fill(ent);
				entData.add(dataEnt);
				entTotal += dataEnt.update.timing;
			} catch (Exception e) {
				modOpis.log.warning(String.format("Error while adding entity %s to the list", ent));
			}					
		}

		System.out.printf("Rendered %d Entities\n", entData.size());
		
		Collections.sort(entData);
		((PanelRenderEntities)(TabPanelRegistrar.INSTANCE.getTab("opis.client.entrender"))).setTable(entData);
		((PanelRenderEntities)(TabPanelRegistrar.INSTANCE.getTab("opis.client.entrender"))).getLblTotal().setText(String.format("Total : %.3f µs", entTotal / 1000.0));
		
		//====================================================================================				
		
		ArrayList<DataBlockRender> blockData = new ArrayList<DataBlockRender>();
		for (CoordinatesBlock coord : ((ProfilerRenderBlock)ProfilerSection.RENDER_BLOCK.getProfiler()).data.keySet()){
			try{
				DataBlockRender dataBlock = new DataBlockRender().fill(coord);
				blockData.add(dataBlock);
			} catch (Exception e) {
				modOpis.log.warning(String.format("Error while adding block %s to the list", coord));
			}					
		}

		Collections.sort(blockData);
		for (DataBlockRender data : blockData){
			ItemStack stack = new ItemStack(data.id, 0, data.meta);
			String    name  = stack.getDisplayName();
			
			System.out.printf("%s %s : %s\n", data.pos, name, data.update);
		}		
	}
	
}
