package mcp.mobius.opis.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import mcp.mobius.opis.network.Packet251Extended;
import mcp.mobius.opis.network.Packets;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

public class Packet_TPS {

	public byte                  header;
	public HashMap<Integer, DescriptiveStatistics> ticktimes;

	public Packet_TPS(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		this.ticktimes = new HashMap<Integer, DescriptiveStatistics>();
		
		try{
			this.header = inputStream.readByte();
			int ndim = inputStream.readInt();
			
			for (int idim = 0; idim < ndim; idim++){
				int dim   = inputStream.readInt();
				int ndata = inputStream.readInt();
				
				this.ticktimes.put(dim, new DescriptiveStatistics(ndata));
				
				for (int idata = 0; idata < ndata; idata++)
					this.ticktimes.get(dim).addValue(inputStream.readDouble());				
				
			}

		} catch (IOException e){}				
	}

	public static Packet250CustomPayload create(){
		Packet251Extended packet      = new Packet251Extended();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		try{
			outputStream.writeByte(Packets.TPS);
			
			Hashtable<Integer, long[]> worldTickTimes = MinecraftServer.getServer().worldTickTimes;
			outputStream.writeInt(worldTickTimes.size());	//We write the number of dimensions
			
			for (Integer dim : worldTickTimes.keySet()){
				outputStream.writeInt(dim); 							// We write the dimension
				outputStream.writeInt(worldTickTimes.get(dim).length);	// Number of data points
				for (long time : worldTickTimes.get(dim))
					outputStream.writeDouble(time/1000.0);				// And finally, the data points	
			}

		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}		
	
}
