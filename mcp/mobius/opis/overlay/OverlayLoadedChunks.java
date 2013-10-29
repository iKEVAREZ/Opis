package mcp.mobius.opis.overlay;

import java.awt.Point;
import java.util.ArrayList;
import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.world.ChunkCoordIntPair;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.IMwDataProvider;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import mcp.mobius.opis.data.ChunksData;
import mcp.mobius.opis.network.Packet_ReqChunksInDim;
import mcp.mobius.opis.network.Packet_UnregisterPlayer;

public class OverlayLoadedChunks implements IMwDataProvider {

	public class ChunkOverlay implements IMwChunkOverlay{

		Point coord;
		boolean forced;
		
		public ChunkOverlay(int x, int z, boolean forced){
			this.coord = new Point(x, z);
			this.forced = forced;
		}
		
		@Override
		public Point getCoordinates() {	return this.coord; }

		@Override
		public int getColor() {	return this.forced ? 0x500000ff : 0x5000ff00; }

		@Override
		public float getFilling() {	return 1.0f; }

		@Override
		public boolean hasBorder() { return true; }

		@Override
		public float getBorderWidth() { return 0.5f; }

		@Override
		public int getBorderColor() { return 0xff000000; }
		
	}	
	
	//public static OverlayLoadedChunks instance = new OverlayLoadedChunks();
	//private OverlayLoadedChunks(){};
	
	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX,	double centerZ, double minX, double minZ, double maxX, double maxZ) {
		ArrayList<IMwChunkOverlay> overlays = new ArrayList<IMwChunkOverlay>();
		for (ChunkCoordIntPair chunk : ChunksData.chunksLoad.keySet())
			overlays.add(new ChunkOverlay(chunk.chunkXPos, chunk.chunkZPos, ChunksData.chunksLoad.get(chunk)));
		return overlays;
	}

	@Override
	public String getStatusString(int dim, int bX, int bY, int bZ) {
		int xChunk = bX >> 4;
		int zChunk = bZ >> 4;
		ChunkCoordIntPair chunkCoord = new ChunkCoordIntPair(xChunk, zChunk);
		
		if (ChunksData.chunksLoad.containsKey(chunkCoord)){
			if (ChunksData.chunksLoad.get(chunkCoord))
				return ", Force loaded";
			else
				return ", Player loaded";
		}
		else
			return ", Not loaded";
	}

	@Override
	public void onMiddleClick(int dim, int bX, int bZ, MapView mapview) {
	}

	@Override
	public void onDimensionChanged(int dimension, MapView mapview) {
		PacketDispatcher.sendPacketToServer(Packet_ReqChunksInDim.create(dimension));
	}

	@Override
	public void onMapCenterChanged(double vX, double vZ, MapView mapview) {
	}

	@Override
	public void onZoomChanged(int level, MapView mapview) {
	}

	@Override
	public void onOverlayActivated(MapView mapview) {
		PacketDispatcher.sendPacketToServer(Packet_ReqChunksInDim.create(mapview.getDimension()));
	}

	@Override
	public void onOverlayDeactivated(MapView mapview) {
		PacketDispatcher.sendPacketToServer(Packet_UnregisterPlayer.create());
	}

	@Override
	public void onDraw(MapView mapview, MapMode mapmode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMouseInput() {
		// TODO Auto-generated method stub
		return false;
	}
}
