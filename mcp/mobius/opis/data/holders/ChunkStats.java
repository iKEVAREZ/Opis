package mcp.mobius.opis.data.holders;

public class ChunkStats {
	public int    nentities  = 0;
	public double updateTime = 0;

	public ChunkStats(){}
	
	public ChunkStats(int nentities, double updateTime){
		this.nentities  = nentities;
		this.updateTime = updateTime;
	}
}
