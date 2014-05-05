package mcp.mobius.opis.network.packets.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mapwriter.region.MwChunk;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.network.enums.Packets;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet56MapChunks;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class Packet_Chunks {

	public static int MAXPACKETSIZE = 30000;	// This should be set to 32k but can be made lower for testing
	
	public byte header;
	public int  dim;
	public MwChunk[] chunks;
	
	private static ByteArrayOutputStream bosPayload = new ByteArrayOutputStream(1);
	private static DataOutputStream streamPayload   = new DataOutputStream(bosPayload);
	
	@SideOnly(Side.CLIENT)
	public static Packet_Chunks read(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		Packet_Chunks   retPacket   = null;
		
		byte header;
		int  dataSize;
		
		try{
			header   = inputStream.readByte();
			dataSize = inputStream.readInt();
			
			if (dataSize == -1){
				retPacket = new Packet_Chunks(bosPayload.toByteArray());
				bosPayload.reset();
			} else {
				byte[] buffer = new byte[dataSize];
				int    readBytes = 0;
				while ((readBytes = inputStream.read(buffer)) != -1){
					streamPayload.write(buffer);
				}
			}
			
		} catch (IOException e){}
		
		return retPacket;
	}	
	
	public static Packet250CustomPayload[] createPackets(String channel, byte header, byte[] data){
		Packet250CustomPayload[] packets = new Packet250CustomPayload[(data.length / MAXPACKETSIZE) + 2];	// Might require a small change to take round values into account
		
		ByteArrayInputStream bis   = new ByteArrayInputStream(data);
		DataInputStream dataStream = new DataInputStream(bis);
		int packetIndex    = 0;
		int readBytes      = 0;
		
		while(readBytes != -1){
			Packet250CustomPayload tmpPacket = new Packet250CustomPayload();
			ByteArrayOutputStream bos        = new ByteArrayOutputStream(1);
			DataOutputStream outputStream    = new DataOutputStream(bos);	

			try{
			
				outputStream.writeByte(header);
				byte[] buffer = new byte[MAXPACKETSIZE];
				readBytes = dataStream.read(buffer);
				outputStream.writeInt(readBytes);
				
				if (readBytes != -1)
					outputStream.write(buffer);
			
			} catch (IOException e){}
			
			tmpPacket.channel = channel;
			tmpPacket.data    = bos.toByteArray();
			tmpPacket.length  = bos.size();			
			
			packets[packetIndex] = tmpPacket;
			packetIndex += 1;
		}
		return packets;
	}
	
	

	@SideOnly(Side.CLIENT)
	public Packet_Chunks(byte[] data) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(data));
		
		try{
			this.header      = inputStream.readByte();
			this.dim         = inputStream.readInt();
			boolean hasNoSky = inputStream.readBoolean();
			
			Packet56MapChunks chunkPacket = new Packet56MapChunks();
			chunkPacket.readPacketData(inputStream);
			this.chunks = new MwChunk[chunkPacket.getNumberOfChunkInPacket()];
			for (int i = 0; i < chunkPacket.getNumberOfChunkInPacket(); i++){
				
				MwChunk newChunk;
				newChunk = this.unpackChunk(dim, chunkPacket.getChunkPosX(i), chunkPacket.getChunkPosZ(i), chunkPacket.getChunkCompressedData(i), chunkPacket.field_73590_a[i], chunkPacket.field_73588_b[i], true, hasNoSky);
				this.chunks[i] = newChunk;
			}
			
		} catch (IOException e){}
	}

	public static void send(int dim, boolean hasNoSky, ArrayList<Chunk> chunks, Player player){
		Packet250CustomPayload packet    = create(dim, hasNoSky, chunks);
		Packet250CustomPayload[] packets = createPackets("Opis_Chunk", Packets.CHUNKS, packet.data);
		
		for (Packet250CustomPayload p : packets){
			if (p != null)
				PacketDispatcher.sendPacketToPlayer(p, player);
		}
	}
	
	private static Packet250CustomPayload create(int dim, boolean hasNoSky, ArrayList<Chunk> chunks){
		//Packet250CustomPayload packet = new Packet250CustomPayload();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		Packet56MapChunks chunkPacket = new Packet56MapChunks(chunks);
		
		try{
			outputStream.writeByte(Packets.CHUNKS);
			outputStream.writeInt(dim);
			outputStream.writeBoolean(hasNoSky);
			 chunkPacket.writePacketData(outputStream);
		}catch(IOException e){}
		
		packet.channel = "Opis_Chunk";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		return packet;
	}		
	
	public MwChunk unpackChunk(int dim, int x, int z, byte[] compressedData, int chunkExistFlag, int chunkHasAddSectionFlag, boolean par4, boolean hasNoSky)
    {
		ExtendedBlockStorage[] storageArrays = new ExtendedBlockStorage[16];
		byte[] blockBiomeArray = new byte[256];
		
        int k = 0;
		
        for (int l = 0; l < storageArrays.length; ++l){
            if ((chunkExistFlag & 1 << l) != 0){
                if (storageArrays[l] == null)
                	storageArrays[l] = new ExtendedBlockStorage(l << 4, hasNoSky);

                byte[] abyte1 = storageArrays[l].getBlockLSBArray();
                System.arraycopy(compressedData, k, abyte1, 0, abyte1.length);
                k += abyte1.length;
            }
            else if (par4 && storageArrays[l] != null)
                storageArrays[l] = null;
        }		

        NibbleArray nibblearray;

        for (int l = 0; l < storageArrays.length; ++l){
            if ((chunkExistFlag & 1 << l) != 0 && storageArrays[l] != null){
                nibblearray = storageArrays[l].getMetadataArray();
                System.arraycopy(compressedData, k, nibblearray.data, 0, nibblearray.data.length);
                k += nibblearray.data.length;
            }
        }        
        
        for (int l = 0; l < storageArrays.length; ++l){
            if ((chunkExistFlag & 1 << l) != 0 && storageArrays[l] != null){
                nibblearray = storageArrays[l].getBlocklightArray();
                System.arraycopy(compressedData, k, nibblearray.data, 0, nibblearray.data.length);
                k += nibblearray.data.length;
            }
        }        
        
        if (hasNoSky){
            for (int l = 0; l < storageArrays.length; ++l){
                if ((chunkExistFlag & 1 << l) != 0 && storageArrays[l] != null){
                    nibblearray = storageArrays[l].getSkylightArray();
                    System.arraycopy(compressedData, k, nibblearray.data, 0, nibblearray.data.length);
                    k += nibblearray.data.length;
                }
            }
        }        
        
        for (int l = 0; l < storageArrays.length; ++l){
            if ((chunkHasAddSectionFlag & 1 << l) != 0){
                if (storageArrays[l] == null)
                    k += 2048;
                else{
                    nibblearray = storageArrays[l].getBlockMSBArray();
                    if (nibblearray == null)
                    	nibblearray = storageArrays[l].createBlockMSBArray();

                    System.arraycopy(compressedData, k, nibblearray.data, 0, nibblearray.data.length);
                    k += nibblearray.data.length;
                }
            }
            else if (par4 && storageArrays[l] != null && storageArrays[l].getBlockMSBArray() != null)
                storageArrays[l].clearMSBArray();
        }
        
        if (par4){
            System.arraycopy(compressedData, k, blockBiomeArray, 0, blockBiomeArray.length);
            int i1 = k + blockBiomeArray.length;
        }

        for (int l = 0; l < storageArrays.length; ++l)
            if (storageArrays[l] != null && (chunkExistFlag & 1 << l) != 0)
                storageArrays[l].removeInvalidBlocks();
        
        //int x, int z, int dimension, byte[][] msbArray, byte[][] lsbArray, byte[][] metaArray, byte[] biomeArray
        
		byte[][] msbArray = new byte[16][];
		byte[][] lsbArray = new byte[16][];
		byte[][] metaArray = new byte[16][];
		byte[][] lightingArray = new byte[16][];		
		
		if (storageArrays != null) {
			for (ExtendedBlockStorage storage : storageArrays) {
				if (storage != null) {
					int y = (storage.getYLocation() >> 4) & 0xf;
					lsbArray[y]  =  storage.getBlockLSBArray();
					msbArray[y]  = (storage.getBlockMSBArray() != null) ? storage.getBlockMSBArray().data : null;
					metaArray[y] = (storage.getMetadataArray() != null) ? storage.getMetadataArray().data : null;
					lightingArray[y] = (storage.getBlocklightArray() != null) ? storage.getBlocklightArray().data : null;					
				}
			}
		}        

        return new MwChunk(x, z, dim, msbArray, lsbArray, metaArray, lightingArray, blockBiomeArray);
	}
}
