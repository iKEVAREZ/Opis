package mcp.mobius.opis.overlay;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.IMwDataProvider;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import mcp.mobius.opis.data.ChunksData;
import mcp.mobius.opis.data.CoordinatesBlock;
import mcp.mobius.opis.data.CoordinatesChunk;
import mcp.mobius.opis.data.TileEntityStatsData;
import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.LayoutBase;
import mcp.mobius.opis.gui.widgets.LayoutCanvas;
import mcp.mobius.opis.gui.widgets.ViewTable;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import mcp.mobius.opis.network.Packet_ReqMeanTimeInDim;
import mcp.mobius.opis.network.Packet_ReqTEsInChunk;
import mcp.mobius.opis.network.Packet_UnregisterPlayer;

public class OverlayMeanTime implements IMwDataProvider {

	public class EntitiesTable extends ViewTable{
		MapView mapView;
		MapMode mapMode;
		
		public EntitiesTable(IWidget parent) { 	
			super(parent);
		}
		
		public void setMap(MapView mapView, MapMode mapMode){
		    this.mapView = mapView;
			this.mapMode = mapMode;			
		}
		
		@Override
		public void onMouseClick(MouseEvent event){
			Row row = this.getRow(event.x, event.y);
			if (row != null){
				CoordinatesBlock coord = ((TileEntityStatsData)row.getObject()).getCoordinates();
				this.mapView.setViewCentre(coord.x, coord.z);
			}
		}
	}
	
	public class ChunkOverlay implements IMwChunkOverlay{

		Point coord;
		int nentities;
		double time;
		double minTime;
		double maxTime;
		boolean selected;
		
		public ChunkOverlay(int x, int z, int nentities, double time, double mintime, double maxtime, boolean selected){
			this.coord     = new Point(x, z);
			this.nentities = nentities;
			this.time      = time;
			this.minTime   = mintime;
			this.maxTime   = maxtime;
			this.selected  = selected;
		}
		
		@Override
		public Point getCoordinates() {	return this.coord; }

		@Override
		public int getColor() {
			//System.out.printf("%s\n", this.maxTime);
			double scaledTime = this.time / this.maxTime;
			int    red        = MathHelper.ceiling_double_int(scaledTime * 255.0);
			int    blue       = 255 - MathHelper.ceiling_double_int(scaledTime * 255.0);
			//System.out.printf("%s\n", red);
			
			return (200 << 24) + (red << 16) + (blue); 
		}
		
		@Override
		public float getFilling() {	return 1.0f; }

		@Override
		public boolean hasBorder() { return true; }

		@Override
		public float getBorderWidth() { return 0.5f; }

		@Override
		public int getBorderColor() { return this.selected ? 0xffffffff : 0xff000000; }
		
	}		
	
	CoordinatesChunk selectedChunk = null;
	private static OverlayMeanTime _instance;
	public boolean    showList = false;
	public LayoutCanvas canvas = null;
	
	private OverlayMeanTime(){

	}
	
	public static OverlayMeanTime instance(){
		if(_instance == null)
			_instance = new OverlayMeanTime();			
		return _instance;
	}
	
	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX,	double centerZ, double minX, double minZ, double maxX, double maxZ) {
		ArrayList<IMwChunkOverlay> overlays = new ArrayList<IMwChunkOverlay>();
		
		double minTime = 9999;
		double maxTime = 0;

		for (CoordinatesChunk chunk : ChunksData.chunkMeanTime.keySet()){
			minTime = Math.min(minTime, ChunksData.chunkMeanTime.get(chunk).updateTime);
			maxTime = Math.max(maxTime, ChunksData.chunkMeanTime.get(chunk).updateTime);
		}
		
		for (CoordinatesChunk chunk : ChunksData.chunkMeanTime.keySet()){
			if (this.selectedChunk != null)
				overlays.add(new ChunkOverlay(chunk.chunkX, chunk.chunkZ, ChunksData.chunkMeanTime.get(chunk).nentities, ChunksData.chunkMeanTime.get(chunk).updateTime, minTime, maxTime, chunk.equals(this.selectedChunk)));
			else
				overlays.add(new ChunkOverlay(chunk.chunkX, chunk.chunkZ, ChunksData.chunkMeanTime.get(chunk).nentities, ChunksData.chunkMeanTime.get(chunk).updateTime, minTime, maxTime, false));
		}
		return overlays;
	}

	@Override
	public String getStatusString(int dim, int bX, int bY, int bZ) {
		int xChunk = bX >> 4;
		int zChunk = bZ >> 4;
		CoordinatesChunk chunkCoord = new CoordinatesChunk(dim, xChunk, zChunk);
		
		if (ChunksData.chunkMeanTime.containsKey(chunkCoord))
			return String.format(", %.5f ms", ChunksData.chunkMeanTime.get(chunkCoord).updateTime/1000.0);
		else
			return "";
	}

	@Override
	public void onMiddleClick(int dim, int bX, int bZ, MapView mapview) {
		this.showList = false;
		
		int xChunk = bX >> 4;
		int zChunk = bZ >> 4;		
		CoordinatesChunk clickedChunk = new CoordinatesChunk(dim, xChunk, zChunk); 
		
		if (ChunksData.chunkMeanTime.containsKey(clickedChunk)){
			if (this.selectedChunk == null)
				this.selectedChunk = clickedChunk;
			else if (this.selectedChunk.equals(clickedChunk))
				this.selectedChunk = null;
			else
				this.selectedChunk = clickedChunk;
		} else {
			this.selectedChunk = null;
		}
		
		if (this.selectedChunk != null)
			PacketDispatcher.sendPacketToServer(Packet_ReqTEsInChunk.create(this.selectedChunk));

	}

	@Override
	public void onDimensionChanged(int dimension, MapView mapview) {
		PacketDispatcher.sendPacketToServer(Packet_ReqMeanTimeInDim.create(dimension));		
	}

	@Override
	public void onMapCenterChanged(double vX, double vZ, MapView mapview) {
	}

	@Override
	public void onZoomChanged(int level, MapView mapview) {
	}

	@Override
	public void onOverlayActivated(MapView mapview) {
		this.selectedChunk = null;
		PacketDispatcher.sendPacketToServer(Packet_ReqMeanTimeInDim.create(mapview.getDimension()));			
	}

	@Override
	public void onOverlayDeactivated(MapView mapview) {
		this.showList = false;
		this.selectedChunk = null;
		PacketDispatcher.sendPacketToServer(Packet_UnregisterPlayer.create());		
	}

	@Override
	public void onDraw(MapView mapview, MapMode mapmode) {
		if (this.canvas == null)
			this.canvas = new LayoutCanvas();
		
		if (mapmode.marginLeft != 0){
			this.canvas.hide();
			return;
		}
		
		if (!this.showList)
			this.canvas.hide();
		else{
			this.canvas.show();		
			this.canvas.draw();
		}
		
	}
	
	@SideOnly(Side.CLIENT)
	public void setupTable(ArrayList<TileEntityStatsData> entities){
		LayoutBase layout = (LayoutBase)this.canvas.addWidget("Table", new LayoutBase(null));
		layout.setGeometry(new WidgetGeometry(100.0,0.0,300.0,100.0,CType.RELXY, CType.REL_Y, WAlign.RIGHT, WAlign.TOP));
		layout.setBackgroundColors(0x90000000, 0x90000000);
		
		EntitiesTable  table  = (EntitiesTable)layout.addWidget("Table_", new EntitiesTable(null));
		
		table.setGeometry(new WidgetGeometry(0.0,0.0,100.0,100.0,CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER, WAlign.CENTER)
		     .setColumnsTitle("\u00a7a\u00a7oType", "\u00a7a\u00a7oPos", "\u00a7a\u00a7oUpdate Time")
			 .setColumnsWidth(50,25,25)
			 .setRowColors(0xff808080, 0xff505050);

		
		for (TileEntityStatsData data : entities){
			String[] name = data.getType().split("\\.");
			table.addRow(data, name[name.length - 1], String.format("[ %s %s %s ]", data.getCoordinates().x, data.getCoordinates().y, data.getCoordinates().z),  String.format("%.5f ms",data.getGeometricMean()/1000.0));
		}

		this.showList = true;
	}

	@Override
	public boolean onMouseInput(MapView mapview, MapMode mapmode) {
		if (this.canvas.shouldRender() && ((LayoutCanvas)this.canvas).hasWidgetAtCursor()){
			((EntitiesTable)this.canvas.getWidget("Table").getWidget("Table_")).setMap(mapview, mapmode);
			this.canvas.handleMouseInput();
			return true;
		}
		return false;
	}

}
