package mcp.mobius.opis.data;

public class ChunkStatsData {
	public int    nentities  = 0;
	public double updateTime = 0;

	public ChunkStatsData(){}
	
	public ChunkStatsData(int nentities, double updateTime){
		this.nentities = nentities;
		this.updateTime = updateTime;
	}
}
