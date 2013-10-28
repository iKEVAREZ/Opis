package mcp.mobius.opis.data;

public final class Coordinates {
	public final int dim, x, y, z;
	public final int chunkX, chunkZ;
	public boolean isChunk;
	
	public Coordinates(int dim, int x, int y, int z){
		this.dim = dim;
		this.x = x; 
		this.y = y; 
		this.z = z;
		this.chunkX = x >> 4;
		this.chunkZ = z >> 4;
		this.isChunk = false;
	}

	public Coordinates(int dim, int chunkX, int chunkZ){
		this.dim = dim;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.x = chunkX << 4; 
		this.y = 0; 
		this.z = chunkZ << 4;		
		this.isChunk = true;
	}	
	
	public boolean equals(Object o)  {
		Coordinates c = (Coordinates)o;
		if (this.isChunk)
			return (this.dim == c.dim) && (this.chunkX == c.chunkX) && (this.chunkZ == c.chunkZ);
		else
			return (this.dim == c.dim) && (this.x == c.x) && (this.y == c.y) && (this.z == c.z);
	};
	
	public int hashCode() {
		if (this.isChunk)
			return String.format("%s %s %s", this.dim, this.chunkX, this.chunkZ).hashCode();
		else
			return String.format("%s %s %s %s", this.dim, this.x, this.y, this.z).hashCode();
	};	
}
