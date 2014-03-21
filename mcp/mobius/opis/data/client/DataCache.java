package mcp.mobius.opis.data.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import codechicken.lib.math.MathHelper;
import net.minecraft.item.ItemStack;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsTick;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.gui.swing.SwingUI;
import mcp.mobius.opis.tools.ModIdentification;

public class DataCache {

	private static DataCache _instance = new DataCache();
	public  static DataCache instance() { return _instance; };
	
	private ArrayList<AmountHolder>     amountEntities = new ArrayList<AmountHolder>();
	private ArrayList<StatsTickHandler> timingHandlers = new ArrayList<StatsTickHandler>(); 
	private ArrayList<StatsEntity>      timingEntities = new ArrayList<StatsEntity>(); 
	private ArrayList<StatsTileEntity>  timingTileEnts = new ArrayList<StatsTileEntity>();
	private ArrayList<StatsChunk>       timingChunks   = new ArrayList<StatsChunk>();
	
	private double timingHandlersTotal = 0D;
	private double timingEntitiesTotal = 0D;
	private double timingTileEntsTotal = 0D;
	private double timingWorldTickTotal= 0D;
	private double timingEntUpdateTotal= 0D;
	
	private int    amountHandlersTotal = 0;
	private int    amountEntitiesTotal = 0;
	private int    amountTileEntsTotal = 0;
	
	private long   amountUpload        = 0;
	private long   amountDownload      = 0;
	
	private StatsTick timingTick = new StatsTick();
	private ArrayList<Double> timingTickGraphData = new ArrayList<Double>();
	//private DescriptiveStatistics timingTickGraphData = new DescriptiveStatistics(100);

	public void setAmountUpload(long value){
		this.amountUpload = value;
		double uploadKB = (value / 8.0) / 1024.0;
		SwingUI.instance().getLblSummaryUpload().setText(String.format("%.3f", uploadKB));
	}

	public void setAmountDownload(long value){
		this.amountDownload = value;
		double downloadKB = (value / 8.0) / 1024.0;
		SwingUI.instance().getLblSummaryDownload().setText(String.format("%.3f", downloadKB));
	}	
	
	private double getProfiledTickTotalTime(){
		return (timingWorldTickTotal + this.timingHandlersTotal + timingTileEntsTotal + timingEntitiesTotal)/1000.;
	}
	
	public void setTimingEntUpdateTotal(double value){
		this.timingEntUpdateTotal = value;
		//SwingUI.instance().getLblSummaryTimingGlobalUpdate().setText(String.format("%.3f", value/1000.));
		//SwingUI.instance().getLblSummaryTimingTotal().setText(String.format("%.3f", this.getProfiledTickTotalTime() ));
	}
	
	public void setTimingWorldTickTotal(double value){
		this.timingWorldTickTotal = value;
		SwingUI.instance().getLblSummaryTimingWorldTick().setText(String.format("%.3f", value/1000.));
		SwingUI.instance().getLblSummaryTimingTotal().setText(String.format("%.3f", this.getProfiledTickTotalTime() ));
	}	
	
	public void setTimingHandlersTotal(double value){
		this.timingHandlersTotal = value;
		SwingUI.instance().getLblSummaryTimingHandlers().setText(String.format("%.3f", value/1000.));
		SwingUI.instance().getLblTimingHandlerValue().setText(String.format("Total update time : %.3f µs", value));
		SwingUI.instance().getLblSummaryTimingTotal().setText(String.format("%.3f",this.getProfiledTickTotalTime()));
	}
	public void setTimingEntitiesTotal(double value){
		this.timingEntitiesTotal = value;
		SwingUI.instance().getLblSummaryTimingEntities().setText(String.format("%.3f", value/1000.));
		SwingUI.instance().getLblTimingEntValue().setText(String.format("Total update time : %.3f µs", value));
		SwingUI.instance().getLblSummaryTimingTotal().setText(String.format("%.3f",this.getProfiledTickTotalTime()));		
	}
	public void setTimingTileEntsTotal(double value){
		this.timingTileEntsTotal = value;
		SwingUI.instance().getLblSummaryTimingTileEnts().setText(String.format("%.3f", value/1000.));
		SwingUI.instance().getLblTimingTEValue().setText(String.format("Total update time : %.3f µs", value));
		SwingUI.instance().getLblSummaryTimingTotal().setText(String.format("%.3f",this.getProfiledTickTotalTime()));		
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
	
	public void setTimingTick(ISerializable tickStat){
		this.timingTick = (StatsTick)tickStat;
		SwingUI.instance().getLblSummaryTimingTick().setText(String.valueOf(String.format("%.3f", this.timingTick.getGeometricMean()/1000.)));
		
		if (timingTickGraphData.size() > 100)
			timingTickGraphData.remove(0);
		
		timingTickGraphData.add(this.timingTick.getGeometricMean()/1000.);
		//timingTickGraphData.addValue(this.timingTick.getGeometricMean()/1000.);
		
		XYSeries xyData = new XYSeries("Update time");
		
		double maxValue = 0D;
		for (int i = 0; i < timingTickGraphData.size(); i++){
			xyData.add(i, timingTickGraphData.get(i));
		}
		
		/*
		double[] values = this.timingTickGraphData.getValues();
		for (int i = 0; i < values.length; i++)
			xyData.add(i, values[i]);
		*/
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(xyData);
		
		
		//JFreeChart chart = ChartFactory.createXYLineChart("", "Seconds", "Update Time [ms]", dataset, PlotOrientation.VERTICAL, false, false, false);
		JFreeChart chart = ChartFactory.createXYAreaChart("", "Seconds", "Update Time [ms]", dataset, PlotOrientation.VERTICAL, false, false, false);
		chart.setBackgroundPaint(new Color(255,255,255,0));
		XYPlot xyPlot = chart.getXYPlot();
		xyPlot.getRendererForDataset(dataset).setSeriesPaint(0, Color.BLUE);
		((NumberAxis)xyPlot.getRangeAxis()).setRange(0.0, 50.0 * (MathHelper.floor_double(xyData.getMaxY() / 50.0D) + 1));
		
		Dimension dim = SwingUI.instance().getLblSummaryTickChart().getSize();
		
		//System.out.printf("%s %s\n", dim.height, dim.width);
		
		BufferedImage image = chart.createBufferedImage(dim.width,dim.height);
		SwingUI.instance().getLblSummaryTickChart().setIcon(new ImageIcon(image));
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
			this.timingHandlers.add((StatsTickHandler)stat);			
		
		DefaultTableModel model = (DefaultTableModel)SwingUI.instance().getTableTimingHandler().getModel();
		this.clearRows(model);
		
		for (StatsTickHandler stat : DataCache.instance().getTimingHandlers()){
			model.addRow(new Object[] {stat.getName(), String.format("%.3f \u00B5s", stat.getGeometricMean())});
		}

		model.fireTableDataChanged();		
	}
	
	public void setTimingEntities(ArrayList<ISerializable> timingEntities_){
		this.timingEntities.clear();
		for (ISerializable stat : timingEntities_)
			this.timingEntities.add((StatsEntity)stat);		
		
		DefaultTableModel model = (DefaultTableModel)SwingUI.instance().getTableTimingEnt().getModel();
		this.clearRows(model);
		
		for (StatsEntity stat : DataCache.instance().getTimingEntities()){
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
			this.timingTileEnts.add((StatsTileEntity)stat);
		
		DefaultTableModel model = (DefaultTableModel)SwingUI.instance().getTableTimingTE().getModel();
		this.clearRows(model);
		
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
				     String.format("%.3f \u00B5s",stat.getGeometricMean())});
		}
		
		model.fireTableDataChanged();				
	}
	
	public void setTimingChunks(ArrayList<ISerializable> timingChunks_){
		this.timingChunks.clear();
		for (ISerializable stat : timingChunks_)
			this.timingChunks.add((StatsChunk)stat);
		
		DefaultTableModel model = (DefaultTableModel)SwingUI.instance().getTableTimingChunk().getModel();
		this.clearRows(model);
		
		for (StatsChunk stat : DataCache.instance().getTimingChunks()){
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
