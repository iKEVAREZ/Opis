package mcp.mobius.opis.overlay;

import java.awt.Point;
import java.util.ArrayList;

import cpw.mods.fml.common.network.PacketDispatcher;
import mapwriter.api.IMwChunkOverlay;
import mapwriter.api.IMwDataProvider;
import mapwriter.map.MapView;
import mcp.mobius.opis.data.ChunksData;
import mcp.mobius.opis.data.CoordinatesChunk;
import mcp.mobius.opis.network.Packet_ReqMeanTimeInDim;
import mcp.mobius.opis.network.Packet_UnregisterPlayer;

public class OverlayMeanTime implements IMwDataProvider {

	public class ChunkOverlay implements IMwChunkOverlay{

		Point coord;
		int nentities;
		double time;
		
		public ChunkOverlay(int x, int z, int nentities, double time){
			this.coord = new Point(x, z);
			this.nentities = nentities;
			this.time = time;
		}
		
		@Override
		public Point getCoordinates() {	return this.coord; }

		@Override
		public int getColor() {	return 0x5000ff00; }

		@Override
		public float getFilling() {	return 1.0f; }

		@Override
		public boolean hasBorder() { return true; }

		@Override
		public float getBorderWidth() { return 0.5f; }

		@Override
		public int getBorderColor() { return 0xff000000; }
		
	}		
	
	@Override
	public ArrayList<IMwChunkOverlay> getChunksOverlay(int dim, double centerX,	double centerZ, double minX, double minZ, double maxX, double maxZ) {
		ArrayList<IMwChunkOverlay> overlays = new ArrayList<IMwChunkOverlay>();
		for (CoordinatesChunk chunk : ChunksData.chunkMeanTime.keySet())
			overlays.add(new ChunkOverlay(chunk.chunkX, chunk.chunkZ, ChunksData.chunkMeanTime.get(chunk).nentities, ChunksData.chunkMeanTime.get(chunk).updateTime));
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
		PacketDispatcher.sendPacketToServer(Packet_ReqMeanTimeInDim.create(mapview.getDimension()));			
	}

	@Override
	public void onOverlayDeactivated(MapView mapview) {
		PacketDispatcher.sendPacketToServer(Packet_UnregisterPlayer.create());		
	}

}
