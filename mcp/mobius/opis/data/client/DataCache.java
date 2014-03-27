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
