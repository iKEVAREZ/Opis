package mcp.mobius.opis.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import mcp.mobius.opis.network.enums.AccessLevel;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JProgressBar;

public class PanelSummary extends JPanel implements ActionListener{
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

	/**
	 * Create the panel.
	 */
	public PanelSummary() {
		setLayout(new MigLayout("", "[20px:20px:20px][][20px:20px:20px][60px:60px:60px][][20px:20px:20px][50px:50px:50px][20px:20px:20px][][20px:20px:20px][50px:50px:50px][][20px:20px:20px][grow][]", "[20px:20px:20px][][][][][][][][20px:20px:20px][][grow]"));
		
		JLabel lblNewLabel_5 = new JLabel("Update time");
		add(lblNewLabel_5, "cell 3 1 2 1,alignx right");
		
		JLabel lblNewLabel_16 = new JLabel("Amount");
		add(lblNewLabel_16, "cell 6 1,alignx right");
		
		JLabel lblNewLabel_24 = new JLabel("Amount");
		add(lblNewLabel_24, "cell 10 1 2 1,alignx right");
		
		btnRun = new JButtonAccess("Run Opis", AccessLevel.PRIVILEGED);
		add(btnRun, "cell 14 1,alignx right");
		btnRun.addActionListener(this);
		
		JLabel lblNewLabel = new JLabel("World tick");
		add(lblNewLabel, "cell 1 2");
		
		lblTimingWorldTick = new JLabel("0");
		add(lblTimingWorldTick, "cell 3 2,alignx right");
		
		JLabel lblNewLabel_13 = new JLabel("ms");
		add(lblNewLabel_13, "cell 4 2");
		
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
		add(lblTimingTileEnts, "cell 3 3,alignx right");
		
		JLabel lblNewLabel_12 = new JLabel("ms");
		add(lblNewLabel_12, "cell 4 3");
		
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
		add(lblTimingEntities, "cell 3 4,alignx right");
		
		JLabel lblNewLabel_11 = new JLabel("ms");
		add(lblNewLabel_11, "cell 4 4");
		
		lblAmountEntities = new JLabel("0");
		add(lblAmountEntities, "cell 6 4,alignx right");
		
		JLabel lblNewLabel_3 = new JLabel("Handlers");
		add(lblNewLabel_3, "cell 1 5");
		
		lblTimingHandlers = new JLabel("0");
		add(lblTimingHandlers, "cell 3 5,alignx right");
		
		JLabel lblNewLabel_10 = new JLabel("ms");
		add(lblNewLabel_10, "cell 4 5");
		
		lblAmountHandlers = new JLabel("0");
		add(lblAmountHandlers, "cell 6 5,alignx right");
		
		JLabel lblNewLabel_22 = new JLabel("Forced chunks");
		add(lblNewLabel_22, "cell 8 5");
		
		lblAmountForced = new JLabel("0");
		add(lblAmountForced, "cell 10 5 2 1,alignx right");
		
		JLabel lblNewLabel_23 = new JLabel("Loaded chunks");
		add(lblNewLabel_23, "cell 8 6");
		
		lblAmountLoaded = new JLabel("0");
		add(lblAmountLoaded, "cell 10 6 2 1,alignx right");
		
		JLabel lblNewLabel_4 = new JLabel("Total");
		add(lblNewLabel_4, "cell 1 7");
		
		lblTimingTotal = new JLabel("0");
		add(lblTimingTotal, "cell 3 7,alignx right");
		
		JLabel lblNewLabel_15 = new JLabel("ms");
		add(lblNewLabel_15, "cell 4 7");
		
		JLabel lblNewLabel_31 = new JLabel("Tick Time");
		add(lblNewLabel_31, "cell 1 9");
		
		lblTickTime = new JLabel("0");
		add(lblTickTime, "cell 3 9,alignx right");
		
		JLabel lblNewLabel_33 = new JLabel("ms");
		add(lblNewLabel_33, "cell 4 9,alignx left");

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

	@Override
	public void actionPerformed(ActionEvent e) {}
}
