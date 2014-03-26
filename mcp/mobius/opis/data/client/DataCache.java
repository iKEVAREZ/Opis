package mcp.mobius.opis.data.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsPlayer;
import mcp.mobius.opis.data.holders.stats.StatsTick;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.helpers.ModIdentification;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.swing.SwingUI;
import mcp.mobius.opis.swing.widgets.JButtonAccess;

public class DataCache {

	private static DataCache _instance = new DataCache();
	public  static DataCache instance() { return _instance; };
	
	private ArrayList<AmountHolder>     amountEntities = new ArrayList<AmountHolder>();
	private ArrayList<StatsTickHandler> timingHandlers = new ArrayList<StatsTickHandler>(); 
	private ArrayList<StatsEntity>      timingEntities = new ArrayList<StatsEntity>(); 
	private ArrayList<StatsTileEntity>  timingTileEnts = new ArrayList<StatsTileEntity>();
	private ArrayList<StatsChunk>       timingChunks   = new ArrayList<StatsChunk>();
	private ArrayList<StatsPlayer>		listPlayers    = new ArrayList<StatsPlayer>();
	
	private long   clockScrew          = 0;
	
	private AccessLevel clientAccess   = AccessLevel.NONE;
	
	private StatsTick timingTick = new StatsTick();
	private ArrayList<Double> timingTickGraphData = new ArrayList<Double>();
	//private DescriptiveStatistics timingTickGraphData = new DescriptiveStatistics(100);

	public void setAccessLevel(AccessLevel level){
		this.clientAccess = level;
		
		for (JButtonAccess button : SwingUI.registeredButtons){
			if (level.ordinal() < button.getAccessLevel().ordinal()){
				button.setEnabled(false);
			} else {
				button.setEnabled(true);
			}
		}
	}
	
	public AccessLevel getAccessLevel(){
		return this.clientAccess;
	}
	
	public void computeClockScrew(long value){
		clockScrew = System.currentTimeMillis() - value;
		System.out.printf("Adjusting clock screw. Server differential is %d ms.\n", clockScrew);
	}
	
	public long getClockScrew(){
		return this.clockScrew;
	}
	
	public ArrayList<AmountHolder> getAmountEntities(){
		return this.amountEntities;
	}
	
	public ArrayList<StatsTickHandler> getTimingHandlers(){
		return this.timingHandlers;
	}
	
	public ArrayList<StatsEntity> getTimingEntities(){
		return this.timingEntities;
	}	

	public ArrayList<StatsTileEntity> getTimingTileEnts(){
		return this.timingTileEnts;
	}		
	
	public ArrayList<StatsChunk> getTimingChunks(){
		return this.timingChunks;
	}		
	
	public void setAmountEntities(ArrayList<ISerializable> stats){
		JTable table            = SwingUI.instance().getPanelAmountEntities().getTable();
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		int               row   = this.updateData(table, model, this.amountEntities, stats, AmountHolder.class);	
		
		int totalEntities = 0;
		
		for (AmountHolder stat : DataCache.instance().getAmountEntities()){
				model.addRow(new Object[] {stat.key, stat.value});
				totalEntities += stat.value;
		}		

		SwingUI.instance().getPanelAmountEntities().getLblSummary().setText("Total : " + String.valueOf(totalEntities));
		this.dataUpdated(table, model, row);	
	}
	
	public void setTimingHandlers(ArrayList<ISerializable> timingHandlers_){
		JTable table            = SwingUI.instance().getPanelTimingHandlers().getTable();
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		int               row   = this.updateData(table, model, this.timingHandlers, timingHandlers_, StatsTickHandler.class);	
		
		for (StatsTickHandler stat : DataCache.instance().getTimingHandlers()){
			model.addRow(new Object[] {stat.getName(), stat});
		}

		this.dataUpdated(table, model, row);		
	}
	
	public void setTimingEntities(ArrayList<ISerializable> timingEntities_){
		JTable table            = SwingUI.instance().getPanelTimingEntities().getTable();
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		int               row   = this.updateData(table, model, this.timingEntities, timingEntities_, StatsEntity.class);	
		
		for (StatsEntity stat : DataCache.instance().getTimingEntities()){
			model.addRow(new Object[]  {stat.getName(), 
										stat.getID(),
										stat.getCoordinates().dim,
										String.format("[ %4d %4d %4d ]", 	stat.getCoordinates().x, stat.getCoordinates().y, stat.getCoordinates().z), 
										stat,
										String.valueOf(stat.getDataPoints())});
		}
		
		this.dataUpdated(table, model, row);		
		
	}
	
	public void setTimingTileEnts(ArrayList<ISerializable> timingTileEnts_){
		JTable table            = SwingUI.instance().getPanelTimingTileEnts().getTable();
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		int               row   = this.updateData(table, model, this.timingTileEnts, timingTileEnts_, StatsTileEntity.class);	
		
		for (StatsTileEntity stat : DataCache.instance().getTimingTileEnts()){
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
				     stat});
		}
		
		this.dataUpdated(table, model, row);		
	}
	
	public void setTimingChunks(ArrayList<ISerializable> timingChunks_){
		JTable table            = SwingUI.instance().getPanelTimingChunks().getTable();
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		int               row   = this.updateData(table, model, this.timingChunks, timingChunks_, StatsChunk.class);		
		
		for (StatsChunk stat : DataCache.instance().getTimingChunks()){
			model.addRow(new Object[]  {
					 stat.getChunk().dim,
					 String.format("[ %4d %4d ]", 	stat.getChunk().chunkX, stat.getChunk().chunkZ),
					 stat.tileEntities,
				     stat.entities,
				     stat});
		}
		
		this.dataUpdated(table, model, row);	
	}	
	
	public ArrayList<StatsPlayer> getListPlayers(){
		return this.listPlayers;
	}
	
	public void setListPlayers(ArrayList<ISerializable> playerList){
		JTable table            = SwingUI.instance().getPanelPlayers().getTable();
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		int               row   = this.updateData(table, model, this.listPlayers, playerList, StatsPlayer.class);

		for (StatsPlayer stat : DataCache.instance().getListPlayers()){
			model.addRow(new Object[]  {
					stat.getName(),
					stat.getCoordinates().dim,
					String.format("[ %4d %4d %4d ]", 	stat.getCoordinates().x, stat.getCoordinates().y, stat.getCoordinates().z),  
					 });
		}
		
		this.dataUpdated(table, model, row);
	}
	
	private <U> int updateData(JTable table, DefaultTableModel model, ArrayList<U> dataarray, ArrayList<? extends ISerializable> newdata, Class<U> datatype){
		int row = table.getSelectedRow();
		
		if (model.getRowCount() > 0)
			for (int i = model.getRowCount() - 1; i >= 0; i--)
				model.removeRow(i);		
		
		dataarray.clear();
		
		for (ISerializable stat : newdata)
			dataarray.add(datatype.cast(stat));
		
		return row;
	}
	
	private void dataUpdated(JTable table, DefaultTableModel model, int row){
		model.fireTableDataChanged();
		
		try{
			table.setRowSelectionInterval(row, row);
		} catch (IllegalArgumentException e ){
			
		}		
	}
}
