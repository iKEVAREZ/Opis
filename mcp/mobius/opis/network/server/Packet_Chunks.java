package mcp.mobius.opis.network.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mapwriter.region.MwChunk;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.network.Packet251Extended;
import mcp.mobius.opis.network.Packets;
import net.minecraft.client.Minecraft;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet56MapChunks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.DimensionManager;

public class Packet_Chunks {

	public byte header;
	public int  dim;
	public MwChunk[] chunks;
	
	@SideOnly(Side.CLIENT)
	public Packet_Chunks(Packet250CustomPayload packet) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		
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

	public static Packet250CustomPayload create(int dim, boolean hasNoSky, ArrayList<Chunk> chunks){
		//Packet250CustomPayload packet = new Packet250CustomPayload();
		Packet251Extended packet      = new Packet251Extended();
		ByteArrayOutputStream bos     = new ByteArrayOutputStream(1);
		DataOutputStream outputStream = new DataOutputStream(bos);

		Packet56MapChunks chunkPacket = new Packet56MapChunks(chunks);
		
		try{
			outputStream.writeByte(Packets.CHUNKS);
			outputStream.writeInt(dim);
			outputStream.writeBoolean(hasNoSky);
			 chunkPacket.writePacketData(outputStream);
		}catch(IOException e){}
		
		packet.channel = "Opis";
		packet.data    = bos.toByteArray();
		packet.length  = bos.size();		
		
		/*
        if (packet.length > 32767){
            //throw new IllegalArgumentException(String.format("Payload may not be larger than 32k : %s", packet.length));
        	modOpis.log.log(Level.WARNING, String.format("Payload may not be larger than 32k : %s", packet.length));
        	return null;
        }
        */
		
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
