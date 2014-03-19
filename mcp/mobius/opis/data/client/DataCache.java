package mcp.mobius.opis.data.client;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

import net.minecraft.item.ItemStack;
import mcp.mobius.opis.data.holders.AmountHolder;
import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.StatAbstract;
import mcp.mobius.opis.data.holders.TickHandlerStats;
import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.gui.swing.SwingUI;
import mcp.mobius.opis.tools.ModIdentification;

public class DataCache {

	private static DataCache _instance = new DataCache();
	public  static DataCache instance() { return _instance; };
	
	private ArrayList<AmountHolder>     amountEntities = new ArrayList<AmountHolder>();
	private ArrayList<TickHandlerStats> timingHandlers = new ArrayList<TickHandlerStats>(); 
	private ArrayList<EntityStats>      timingEntities = new ArrayList<EntityStats>(); 
	private ArrayList<TileEntityStats>  timingTileEnts = new ArrayList<TileEntityStats>();
	private ArrayList<ChunkStats>       timingChunks   = new ArrayList<ChunkStats>();
	
	private double timingHandlersTotal = 0D;
	private double timingEntitiesTotal = 0D;
	private double timingTileEntsTotal = 0D;
	
	private int    amountHandlersTotal = 0;
	private int    amountEntitiesTotal = 0;
	private int    amountTileEntsTotal = 0;
	
	public void setTimingHandlersTotal(double value){
		this.timingHandlersTotal = value;
		SwingUI.instance().getLblSummaryTimingHandlers().setText(String.format("%.3f", value/1000.));
		SwingUI.instance().getLblTimingHandlerValue().setText(String.format("Total update time : %.3f µs", value));
		SwingUI.instance().getLblSummaryTimingTotal().setText(String.format("%.3f", (this.timingHandlersTotal + this.timingEntitiesTotal + this.timingTileEntsTotal)/1000.));
	}
	public void setTimingEntitiesTotal(double value){
		this.timingEntitiesTotal = value;
		SwingUI.instance().getLblSummaryTimingEntities().setText(String.format("%.3f", value/1000.));
		SwingUI.instance().getLblTimingEntValue().setText(String.format("Total update time : %.3f µs", value));
		SwingUI.instance().getLblSummaryTimingTotal().setText(String.format("%.3f", (this.timingHandlersTotal + this.timingEntitiesTotal + this.timingTileEntsTotal)/1000.));		
	}
	public void setTimingTileEntsTotal(double value){
		this.timingTileEntsTotal = value;
		SwingUI.instance().getLblSummaryTimingTileEnts().setText(String.format("%.3f", value/1000.));
		SwingUI.instance().getLblTimingTEValue().setText(String.format("Total update time : %.3f µs", value));
		SwingUI.instance().getLblSummaryTimingTotal().setText(String.format("%.3f", (this.timingHandlersTotal + this.timingEntitiesTotal + this.timingTileEntsTotal)/1000.));		
	}	

	public void setAmountHandlersTotal(int value){
		this.amountHandlersTotal = value;
		SwingUI.instance().getLblSummaryAmountHandlers().setText(String.valueOf(value));
	}
	public void setAmountEntitiesTotal(int value){
		this.amountEntitiesTotal = value;
		SwingUI.instance().getLblSummaryAmountEntities().setText(String.valueOf(value));		
	}
	public void setAmountTileEntsTotal(int value){
		this.amountTileEntsTotal = value;
		SwingUI.instance().getLblSummaryAmountTileEnts().setText(String.valueOf(value));
	}		
	
	
	public ArrayList<AmountHolder> getAmountEntities(){
		return this.amountEntities;
	}
	
	public ArrayList<TickHandlerStats> getTimingHandlers(){
		return this.timingHandlers;
	}
	
	public ArrayList<EntityStats> getTimingEntities(){
		return this.timingEntities;
	}	

	public ArrayList<TileEntityStats> getTimingTileEnts(){
		return this.timingTileEnts;
	}		
	
	public ArrayList<ChunkStats> getTimingChunks(){
		return this.timingChunks;
	}		
	
	public void setAmountEntities(ArrayList<ISerializable> stats){
		this.amountEntities.clear();
		for (ISerializable stat : stats)
			this.amountEntities.add((AmountHolder)stat);			
		
		DefaultTableModel model = (DefaultTableModel)SwingUI.instance().getTableEntityList().getModel();
		this.clearRows(model);
		
		int totalEntities = 0;
		
		for (AmountHolder stat : DataCache.instance().getAmountEntities()){
				model.addRow(new Object[] {stat.key, stat.value});
				totalEntities += stat.value;
		}		

		SwingUI.instance().getLabelAmountValue().setText(String.valueOf(totalEntities));
		model.fireTableDataChanged();		
	}
	
	public void setTimingHandlers(ArrayList<ISerializable> timingHandlers_){
		this.timingHandlers.clear();
		for (ISerializable stat : timingHandlers_)
			this.timingHandlers.add((TickHandlerStats)stat);			
		
		DefaultTableModel model = (DefaultTableModel)SwingUI.instance().getTableTimingHandler().getModel();
		this.clearRows(model);
		
		for (TickHandlerStats stat : DataCache.instance().getTimingHandlers()){
			model.addRow(new Object[] {stat.getName(), String.format("%.3f \u00B5s", stat.getGeometricMean())});
		}

		model.fireTableDataChanged();		
	}
	
	public void setTimingEntities(ArrayList<ISerializable> timingEntities_){
		this.timingEntities.clear();
		for (ISerializable stat : timingEntities_)
			this.timingEntities.add((EntityStats)stat);		
		
		DefaultTableModel model = (DefaultTableModel)SwingUI.instance().getTableTimingEnt().getModel();
		this.clearRows(model);
		
		for (EntityStats stat : DataCache.instance().getTimingEntities()){
			model.addRow(new Object[]  {stat.getName(), 
										stat.getID(),
										stat.getCoordinates().dim,
										String.format("[ %4d %4d %4d ]", 	stat.getCoordinates().x, stat.getCoordinates().y, stat.getCoordinates().z), 
										String.format("%.3f \u00B5s", stat.getGeometricMean()),
										String.valueOf(stat.getDataPoints())});
		}
		
		model.fireTableDataChanged();			
		
	}
	
	public void setTimingTileEnts(ArrayList<ISerializable> timingTileEnts_){
		this.timingTileEnts.clear();
		for (ISerializable stat : timingTileEnts_)
			this.timingTileEnts.add((TileEntityStats)stat);
		
		DefaultTableModel model = (DefaultTableModel)SwingUI.instance().getTableTimingTE().getModel();
		this.clearRows(model);
		
		for (TileEntityStats stat : DataCache.instance().getTimingTileEnts()){
			ItemStack is;
			String name  = String.format("te.%d.%d", stat.getID(), stat.getMeta());
			String modID = "<UNKNOWN>";
			
			try{
				is = new ItemStack(stat.getID(), 1, stat.getMeta());
				name  = is.getDisplayName();
				modID = ModIdentification.idFromStack(is);
			}  catch (Exception e) {	}			
			
			model.addRow(new Object[]  {
					 name,
					 modID,
				     stat.getCoordinates().dim,
				     String.format("[ %4d %4d %4d ]", 	stat.getCoordinates().x, stat.getCoordinates().y, stat.getCoordinates().z),  
				     String.format("%.3f \u00B5s",stat.getGeometricMean())});
		}
		
		model.fireTableDataChanged();				
	}
	
	public void setTimingChunks(ArrayList<ISerializable> timingChunks_){
		this.timingChunks.clear();
		for (ISerializable stat : timingChunks_)
			this.timingChunks.add((ChunkStats)stat);
		
		DefaultTableModel model = (DefaultTableModel)SwingUI.instance().getTableTimingChunk().getModel();
		this.clearRows(model);
		
		for (ChunkStats stat : DataCache.instance().getTimingChunks()){
			model.addRow(new Object[]  {
					 stat.getChunk().dim,
					 String.format("[ %4d %4d ]", 	stat.getChunk().x, stat.getChunk().z),
					 stat.tileEntities,
				     stat.entities,
				     String.format("%.3f \u00B5s",stat.getDataSum())});
		}
		
		model.fireTableDataChanged();				
	}	
	
	private void clearRows(DefaultTableModel model){
		if (model.getRowCount() > 0)
			for (int i = model.getRowCount() - 1; i >= 0; i--)
				model.removeRow(i);		
	}
}
