package mcp.mobius.opis.overlay;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.network.PacketDispatcher;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.IMwDataProvider;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.gui.widgets.LayoutCanvas;
import mcp.mobius.opis.network.client.Packet_ReqDataDim;

public class OverlayEntityPerChunk implements IMwDataProvider {

	public class ChunkOverlay implements IMwChunkOverlay{

		Point coord;
		int minEnts;
		int maxEnts;
		int ents;
		
		public ChunkOverlay(int x, int z, int minEnts, int maxEnts, int ents){
			this.coord = new Point(x, z);
			this.minEnts = minEnts;
			this.maxEnts = maxEnts;
			this.ents    = ents;
		}
		
		@Override
		public Point getCoordinates() {	return this.coord; }

		@Override
		public int getColor() {
			double scaledAmount = (double)this.ents / (double)this.maxEnts;
			int    red          = MathHelper.ceiling_double_int(scaledAmount * 255.0);
			int    blue         = 255 - MathHelper.ceiling_double_int(scaledAmount * 255.0);
			
			return (200 << 24) + (red << 16) + (blue);  }

		@Override
		public float getFilling() {	return 1.0f; }

		@Override
		public boolean hasBorder() { return true; }

		@Override
		public float getBorderWidth() { return 0.5f; }

		@Override
		public int getBorderColor() { return 0xff000000; }
		
	}	
	
	private static OverlayEntityPerChunk _instance;
	public boolean    showList = false;
	public LayoutCanvas canvas = null;
	public HashMap<CoordinatesChunk, Integer> overlayData = new HashMap<CoordinatesChunk, Integer>(); 
	
	private OverlayEntityPerChunk(){}
	
	public static OverlayEntityPerChunk instance(){
		if(_instance == null)
			_instance = new OverlayEntityPerChunk();			
		return _instance;
	}	
	
	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX, double centerZ, double minX, double minZ, double maxX, double maxZ) {
		ArrayList<IMwChunkOverlay> overlays = new ArrayList<IMwChunkOverlay>();
		
		int minEnts = 9999;
		int maxEnts = 0;

		for (CoordinatesChunk chunk : overlayData.keySet()){
			minEnts = Math.min(minEnts, overlayData.get(chunk));
			maxEnts = Math.max(maxEnts, overlayData.get(chunk));
		}		
		
		for (CoordinatesChunk chunk : overlayData.keySet())
			overlays.add(new ChunkOverlay(chunk.toChunkCoordIntPair().chunkXPos, chunk.toChunkCoordIntPair().chunkZPos, minEnts, maxEnts, overlayData.get(chunk)));
		return overlays;
	}

	@Override
	public String getStatusString(int dim, int bX, int bY, int bZ) {
		CoordinatesChunk chunk = new CoordinatesChunk(dim, bX >> 4, bZ >> 4);
		if (this.overlayData.containsKey(chunk))
			return String.format(", entities: %d", this.overlayData.get(chunk));
		else
			return ", entities: 0";
	}

	@Override
	public void onMiddleClick(int dim, int bX, int bZ, MapView mapview) {}

	@Override
	public void onDimensionChanged(int dimension, MapView mapview) {
		PacketDispatcher.sendPacketToServer(Packet_ReqDataDim.create(dimension, "overlay:chunk:entities"));
	}

	@Override
	public void onMapCenterChanged(double vX, double vZ, MapView mapview) {}

	@Override
	public void onZoomChanged(int level, MapView mapview) {}

	@Override
	public void onOverlayActivated(MapView mapview) {
		PacketDispatcher.sendPacketToServer(Packet_ReqDataDim.create(mapview.getDimension(), "overlay:chunk:entities"));
	}

	@Override
	public void onOverlayDeactivated(MapView mapview) {}

	@Override
	public void onDraw(MapView mapview, MapMode mapmode) {}

	@Override
	public boolean onMouseInput(MapView mapview, MapMode mapmode) {
		return false;
	}

}
