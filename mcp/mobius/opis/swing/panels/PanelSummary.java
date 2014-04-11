package mcp.mobius.opis.swing.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JPanel;

import mcp.mobius.opis.api.ITabPanel;
import mcp.mobius.opis.data.client.DataCache;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.newtypes.DataBlockTick;
import mcp.mobius.opis.data.holders.newtypes.DataNetworkTick;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntity;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.newtypes.DataTimingMillisecond;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.SelectedTab;
import mcp.mobius.opis.swing.actions.ActionRunOpis;
import mcp.mobius.opis.swing.widgets.JButtonAccess;
import mcp.mobius.opis.swing.widgets.JPanelMsgHandler;
import net.miginfocom.swing.MigLayout;
import net.minecraft.util.MathHelper;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JProgressBar;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PanelSummary extends JPanelMsgHandler implements ITabPanel{
	private JLabel lblTimingWorldTick;
	private JLabel lblTimingTileEnts;
	private JLabel lblTimingEntities;
	private JLabel lblTimingHandlers;
	private JLabel lblTimingTotal;
	private JLabel lblTickTime;
	private JLabel lblAmountTileEnts;
	private JLabel lblAmountEntities;
	private JLabel lblAmountHandlers;
	private JLabel lblAmountUpload;
	private JLabel lblAmountDownload;
	private JLabel lblAmountForced;
	private JLabel lblAmountLoaded;
	private JButtonAccess btnRun;
	private JProgressBar progressBarRun;
	private JLabel lblTimeStamp;

	private DescriptiveStatistics pingData = new DescriptiveStatistics(5);
	private long nPings = 0;
	
	/**
	 * Create the panel.
	 */
	public PanelSummary() {
		setLayout(new MigLayout("", "[20px:20px:20px,grow][][20px:20px:20px][60px:60px:60px][][20px:20px:20px][50px:50px:50px][20px:20px:20px][][20px:20px:20px][50px:50px:50px][][20px:20px:20px][grow][]", "[20px:20px:20px][][][][][][][][][20px:20px:20px][][grow]"));
		
		JLabel lblNewLabel_5 = new JLabel("Update time");
		add(lblNewLabel_5, "cell 3 1 2 1,alignx right");
		
		JLabel lblNewLabel_16 = new JLabel("Amount");
		add(lblNewLabel_16, "cell 6 1,alignx right");
		
		JLabel lblNewLabel_24 = new JLabel("Amount");
		add(lblNewLabel_24, "cell 10 1 2 1,alignx right");
		
		btnRun = new JButtonAccess("Run Opis", AccessLevel.PRIVILEGED);
		add(btnRun, "cell 14 1,alignx right");
		btnRun.addActionListener(new ActionRunOpis());
		
		JLabel lblNewLabel = new JLabel("World tick");
		add(lblNewLabel, "cell 1 2");
		
		lblTimingWorldTick = new JLabel("0");
		add(lblTimingWorldTick, "cell 3 2 2 1,alignx right");
		
		JLabel lblNewLabel_20 = new JLabel("Upload");
		add(lblNewLabel_20, "cell 8 2");
		
		lblAmountUpload = new JLabel("0");
		add(lblAmountUpload, "cell 10 2,alignx right");
		
		JLabel lblNewLabel_30 = new JLabel("kB/s");
		add(lblNewLabel_30, "cell 11 2,alignx right");
		
		progressBarRun = new JProgressBar();
		add(progressBarRun, "cell 13 2 2 1,growx");
		
		JLabel lblNewLabel_1 = new JLabel("TileEntities");
		add(lblNewLabel_1, "cell 1 3");
		
		lblTimingTileEnts = new JLabel("0");
		add(lblTimingTileEnts, "cell 3 3 2 1,alignx right");
		
		lblAmountTileEnts = new JLabel("0");
		add(lblAmountTileEnts, "cell 6 3,alignx right");
		
		JLabel lblNewLabel_21 = new JLabel("Download");
		add(lblNewLabel_21, "cell 8 3");
		
		lblAmountDownload = new JLabel("0");
		add(lblAmountDownload, "cell 10 3,alignx right");
		
		JLabel lblNewLabel_29 = new JLabel("kB/s");
		add(lblNewLabel_29, "cell 11 3,alignx right");
		
		lblTimeStamp = new JLabel("Last run : Never");
		add(lblTimeStamp, "cell 13 3 2 1,alignx right");
		
		JLabel lblNewLabel_2 = new JLabel("Entities");
		add(lblNewLabel_2, "cell 1 4");
		
		lblTimingEntities = new JLabel("0");
		add(lblTimingEntities, "cell 3 4 2 1,alignx right");
		
		lblAmountEntities = new JLabel("0");
		add(lblAmountEntities, "cell 6 4,alignx right");
		
		JLabel lblNewLabel_6 = new JLabel("Ping");
		add(lblNewLabel_6, "cell 8 4");
		
		lblTimingPing = new JLabel("0");
		add(lblTimingPing, "cell 10 4 2 1,alignx right");
		
		JLabel lblNewLabel_3 = new JLabel("Handlers");
		add(lblNewLabel_3, "cell 1 5");
		
		lblTimingHandlers = new JLabel("0");
		add(lblTimingHandlers, "cell 3 5 2 1,alignx right");
		
		lblAmountHandlers = new JLabel("0");
		add(lblAmountHandlers, "cell 6 5,alignx right");
		
		JLabel lblNewLabel_22 = new JLabel("Forced chunks");
		add(lblNewLabel_22, "cell 8 6");
		
		lblAmountForced = new JLabel("0");
		add(lblAmountForced, "cell 10 6 2 1,alignx right");
		
		JLabel lblNewLabel_32 = new JLabel("Network");
		add(lblNewLabel_32, "cell 1 6");
		
		lblTimingNetwork = new JLabel("0");
		add(lblTimingNetwork, "cell 3 6 2 1,alignx right");
		
		JLabel lblNewLabel_23 = new JLabel("Loaded chunks");
		add(lblNewLabel_23, "cell 8 7");
		
		lblAmountLoaded = new JLabel("0");
		add(lblAmountLoaded, "cell 10 7 2 1,alignx right");
		
		JLabel lblNewLabel_4 = new JLabel("Total");
		add(lblNewLabel_4, "cell 1 8");
		
		lblTimingTotal = new JLabel("0");
		add(lblTimingTotal, "cell 3 8 2 1,alignx right");
		
		JLabel lblNewLabel_31 = new JLabel("Tick Time");
		add(lblNewLabel_31, "cell 1 10");
		
		lblTickTime = new JLabel("0");
		add(lblTickTime, "cell 3 10 2 1,alignx right");
		
		JPanel panel = this.createGraph();
		add(panel, "cell 0 11 15 1,grow");

	}

	public JLabel getLblTimingWorldTick() {return lblTimingWorldTick;}
	public JLabel getLblTimingTileEnts()  {return lblTimingTileEnts;}
	public JLabel getLblTimingEntities()  {return lblTimingEntities;}
	public JLabel getLblTimingHandlers()  {return lblTimingHandlers;}
	public JLabel getLblTimingTotal()     {return lblTimingTotal;}
	public JLabel getLblTickTime()        {return lblTickTime;}
	public JLabel getLblAmountTileEnts()  {return lblAmountTileEnts;}
	public JLabel getLblAmountEntities()  {return lblAmountEntities;}
	public JLabel getLblAmountHandlers()  {return lblAmountHandlers;}
	public JLabel getLblAmountUpload()    {return lblAmountUpload;}
	public JLabel getLblAmountDownload()  {return lblAmountDownload;}
	public JLabel getLblAmountForced()    {return lblAmountForced;}
	public JLabel getLblAmountLoaded()    {return lblAmountLoaded;}
	public JButton getBtnRun()              {return btnRun;}
	public JProgressBar getProgressBarRun() {return progressBarRun;}
	public JLabel getLblTimeStamp()         {return lblTimeStamp;}

	public void setProgressBar(int min, int max, int value){
		if (min != -1)
			this.getProgressBarRun().setMinimum(min);
		
		if (max != -1)
			this.getProgressBarRun().setMaximum(max);
			
		if (value != -1)
			this.getProgressBarRun().setValue(value);			
	}
	
	private DataTimingMillisecond timingWorldTickTotal = new DataTimingMillisecond();
	private DataTimingMillisecond timingHandlersTotal  = new DataTimingMillisecond();
	private DataTimingMillisecond timingTileEntsTotal  = new DataTimingMillisecond();
	private DataTimingMillisecond timingEntitiesTotal  = new DataTimingMillisecond();
	private DataTimingMillisecond timingNetworkTotal   = new DataTimingMillisecond();
	
	public void setTimingEntUpdateTotal(double value){
	}	
	
	ArrayList<Double> datapoints = new ArrayList<Double>();
	XYSeries xydata = new XYSeries("Update time");
	XYSeriesCollection dataset = new XYSeriesCollection();
	XYPlot xyPlot;
	private JLabel lblTimingNetwork;
	private JLabel lblTimingPing;
	
	private JPanel createGraph(){
		JFreeChart chart = ChartFactory.createXYAreaChart("", "Seconds", "Update Time [ms]", dataset, PlotOrientation.VERTICAL, false, false, false);
		chart.setBackgroundPaint(new Color(255,255,255,0));
		xyPlot = chart.getXYPlot();
		xyPlot.getRendererForDataset(dataset).setSeriesPaint(0, Color.BLUE);
		
		for (double y = 25.0; y < 500.0; y += 25.0){
			ValueMarker marker = new ValueMarker(y);
			marker.setPaint(Color.black);
			xyPlot.addRangeMarker(marker);
		}		

		//ValueMarker marker = new ValueMarker(50.0);
		//marker.setPaint(Color.red);
		//xyPlot.addRangeMarker(marker);
		
		return new ChartPanel(chart);
	}
	
	public void setTimingTick(ISerializable tickStat){
		DataTimingMillisecond ticktime = ((DataTiming)tickStat).asMillisecond();
		
		this.getLblTickTime().setText(ticktime.toString());

		datapoints.add(ticktime.timing/1000./1000.);
		if (datapoints.size() > 101)
			datapoints.remove(0);
		
		xydata.clear();
		for (int i = 0; i < datapoints.size(); i++)
			xydata.add(i, datapoints.get(i));

		dataset.removeAllSeries();
		dataset.addSeries(xydata);
		
		Double verticalScale = 50.0 * (MathHelper.floor_double(xydata.getMaxY() / 50.0D) + 1);
		((NumberAxis)xyPlot.getRangeAxis()).setRange(0.0, verticalScale);
		((NumberAxis)xyPlot.getDomainAxis()).setRange(0.0, 100.0);
	}

	private DataTimingMillisecond getProfiledTickTotalTime(){
		return new DataTimingMillisecond(timingWorldTickTotal.timing + timingTileEntsTotal.timing + timingEntitiesTotal.timing + timingHandlersTotal.timing + timingNetworkTotal.timing);
	}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case VALUE_AMOUNT_TILEENTS:{
			this.getLblAmountTileEnts().setText(String.valueOf(((SerialInt)rawdata.value).value));
			break;
		}
		case VALUE_AMOUNT_ENTITIES:{
			this.getLblAmountEntities().setText(String.valueOf(((SerialInt)rawdata.value).value));		
			break;
		}
		case VALUE_AMOUNT_HANDLERS:{
			this.getLblAmountHandlers().setText(String.valueOf(((SerialInt)rawdata.value).value));
			break;
		}
		case VALUE_AMOUNT_UPLOAD:{
			double uploadKB = (((SerialLong)rawdata.value).value) / 1024.0;
			this.getLblAmountUpload().setText(String.format("%.3f", uploadKB));	
			break;
		}
		case VALUE_AMOUNT_DOWNLOAD:{
			double downloadKB = (((SerialLong)rawdata.value).value) / 1024.0;
			this.getLblAmountDownload().setText(String.format("%.3f", downloadKB));	
			break;
		}
		case VALUE_TIMING_TICK:{
			this.setTimingTick(rawdata.value);
			break;
		}		
		case VALUE_CHUNK_FORCED:{
			this.getLblAmountForced().setText(String.valueOf(((SerialInt)rawdata.value).value));
			break;
		}		
		case VALUE_CHUNK_LOADED:{
			this.getLblAmountLoaded().setText(String.valueOf(((SerialInt)rawdata.value).value));
			break;
		}
		case VALUE_TIMING_NETWORK:{
			this.timingNetworkTotal = ((DataNetworkTick)rawdata.value).update.asMillisecond();
			this.getLblTimingNetwork().setText(this.timingNetworkTotal.toString());			
			break;
		}			
		case VALUE_TIMING_TILEENTS:{
			this.timingTileEntsTotal = ((DataTiming)rawdata.value).asMillisecond();
			this.getLblTimingTileEnts().setText(this.timingTileEntsTotal.toString());
			this.getLblTimingTotal().setText(String.format("%s", this.getProfiledTickTotalTime().toString() ));	
			break;
		}			
		case VALUE_TIMING_ENTITIES:{
			this.timingEntitiesTotal = ((DataTiming)rawdata.value).asMillisecond();
			this.getLblTimingEntities().setText(this.timingEntitiesTotal.toString());
			this.getLblTimingTotal().setText(String.format("%s", this.getProfiledTickTotalTime().toString() ));
			break;
		}			
		case VALUE_TIMING_HANDLERS:{
			this.timingHandlersTotal = ((DataTiming)rawdata.value).asMillisecond();
			this.getLblTimingHandlers().setText(this.timingHandlersTotal.toString());
			this.getLblTimingTotal().setText(String.format("%s", this.getProfiledTickTotalTime().toString() ));
			break;
		}					
		case VALUE_TIMING_WORLDTICK:{
			this.timingWorldTickTotal = ((DataBlockTick)rawdata.value).total.asMillisecond();
			this.getLblTimingWorldTick().setText(this.timingWorldTickTotal.toString());
			this.getLblTimingTotal().setText(String.format("%s", this.getProfiledTickTotalTime().toString() ));	
			
			String tooltip = "<html><font face=\"monospace\"><pre>";
			
			for (Integer indim : ((DataBlockTick)rawdata.value).perdim.keySet()){
				//System.out.printf("WorldTick [ %4d ] %s\n", indim, ((DataBlockTick)rawdata.value).perdim.get(indim).asMillisecond().toString());
				tooltip += String.format("[ %4d ] %s<br>", indim, ((DataBlockTick)rawdata.value).perdim.get(indim).asMillisecond().toString());
			}
			
			tooltip += "</pre></html>";
			
			this.getLblTimingWorldTick().setToolTipText(tooltip);
			
			break;
		}
		case STATUS_START:{
			this.getBtnRun().setText("Running...");
			this.setProgressBar(0, ((SerialInt)rawdata.value).value, 0);	
			break;
		}
		case STATUS_STOP:{
			this.getBtnRun().setText("Run Opis");
			this.setProgressBar(0, ((SerialInt)rawdata.value).value, ((SerialInt)rawdata.value).value);	
			break;
		}		
		case STATUS_RUN_UPDATE:{
			this.setProgressBar(-1, -1, ((SerialInt)rawdata.value).value);	
			break;
		}		
		case STATUS_RUNNING:{
			this.getBtnRun().setText("Running...");
			this.setProgressBar(-1, -1, ((SerialInt)rawdata.value).value);
			break;
		}
		case STATUS_TIME_LAST_RUN:{
			long serverLastRun = ((SerialLong)rawdata.value).value;
			if (serverLastRun == 0){
				this.getLblTimeStamp().setText("Last run : <Never>");
			} else {
				long clientLastRun = serverLastRun + DataCache.instance().getClockScrew();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        Date resultdate = new Date(clientLastRun);
				
		        this.getLblTimeStamp().setText(String.format("Last run : %s", sdf.format(resultdate)));
			}
			break;
		}
	
		case VALUE_TIMING_ENTUPDATE:{
			break;
		}
		
		case STATUS_PING:{
			this.pingData.addValue((double)(System.nanoTime() - ((SerialLong)rawdata.value).value));
			this.nPings += 1;
			
			if (this.nPings % 2 == 0){
				DataTiming timing = new DataTiming(this.pingData.getGeometricMean());
				this.getLblTimingPing().setText(timing.asMillisecond().toString());
			}
			break;
		}
		
		default:
			return false;
			
		}
		return true;
	}

	public JLabel getLblTimingNetwork() {
		return lblTimingNetwork;
	}
	public JLabel getLblTimingPing() {
		return lblTimingPing;
	}
	
	@Override
	public SelectedTab getSelectedTab() {
		return SelectedTab.SUMMARY;
	}						
}
