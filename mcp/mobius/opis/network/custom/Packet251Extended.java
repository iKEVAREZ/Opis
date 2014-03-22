package mcp.mobius.opis.network.custom;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.network.packet.Packet250CustomPayload;

public class Packet251Extended extends Packet250CustomPayload {

    public Packet251Extended() {}

    public Packet251Extended(String par1Str, byte[] par2ArrayOfByte)
    {
        this.channel = par1Str;
        this.data = par2ArrayOfByte;

        if (par2ArrayOfByte != null)
            this.length = par2ArrayOfByte.length;
    }	
	
    @Override
    public void readPacketData(DataInput par1DataInput) throws IOException
    {
        this.channel = readString(par1DataInput, 20);
        this.length = par1DataInput.readInt();

        this.data = new byte[this.length];
        par1DataInput.readFully(this.data);
    }

    @Override    
    public void writePacketData(DataOutput par1DataOutput) throws IOException
    {
        writeString(this.channel, par1DataOutput);
        par1DataOutput.writeInt(this.length);

        if (this.data != null)
            par1DataOutput.write(this.data);
    }    
    
    public int getPacketSize()
    {
        return 2 + this.channel.length() * 2 + 4 + this.length;
    }    
    
}
