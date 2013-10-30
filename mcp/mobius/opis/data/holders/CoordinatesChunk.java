package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.tileentity.TileEntity;

public final class CoordinatesChunk implements ISerializable {
	public final int dim, x, y, z;
	public final int chunkX, chunkZ;
	//public boolean isChunk;
	
	public CoordinatesChunk(CoordinatesBlock coord){
		this.dim = coord.dim;
		this.chunkX = coord.chunkX;
		this.chunkZ = coord.chunkZ;
		
		this.x = chunkX << 4; 
		this.y = 0; 
		this.z = chunkZ << 4;
	}
	
	public CoordinatesChunk(int dim, int chunkX, int chunkZ){
		this.dim = dim;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		
		this.x = chunkX << 4; 
		this.y = 0; 
		this.z = chunkZ << 4;		
	}
	
	public CoordinatesChunk(TileEntity te){
		this.dim = te.getWorldObj().provider.dimensionId;
		this.chunkX = te.xCoord >> 4;
		this.chunkZ = te.zCoord >> 4;
		
		this.x = this.chunkX << 4; 
		this.y = 0; 
		this.z = this.chunkZ << 4;		
	}	
	
	public String toString(){
		return String.format("[%6d %6d %6d]", this.dim, this.chunkX, this.chunkZ);
	}	
	
	public boolean equals(Object o)  {
		CoordinatesChunk c = (CoordinatesChunk)o;
		return (this.dim == c.dim) && (this.chunkX == c.chunkX) && (this.chunkZ == c.chunkZ);
	}
	
	public int hashCode() {
		return String.format("%s %s %s", this.dim, this.chunkX, this.chunkZ).hashCode();
	}

	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeInt(this.dim);
		stream.writeInt(this.chunkX);
		stream.writeInt(this.chunkZ);		
	}

	public static CoordinatesChunk readFromStream(DataInputStream stream) throws IOException {
		return new CoordinatesChunk(stream.readInt(), stream.readInt(), stream.readInt());
	}	
}