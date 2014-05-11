package mcp.mobius.opis.network.packets.server;

import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mcp.mobius.opis.api.MessageHandlerRegistrar;
import mcp.mobius.opis.data.holders.basetypes.TicketData;
import mcp.mobius.opis.gui.overlay.OverlayLoadedChunks;
import mcp.mobius.opis.network.PacketBase;

public class PacketTickets extends PacketBase {

	public HashSet<TicketData> tickets = new HashSet<TicketData>();	
	
	public PacketTickets(){}
	public PacketTickets(HashSet<TicketData>  data){
		this.tickets = data;
	}
	
	@Override
	public void encode(ByteArrayDataOutput output) {
		output.writeInt(tickets.size());
		for (TicketData ticket : tickets)
			ticket.writeToStream(output);

	}

	@Override
	public void decode(ByteArrayDataInput input) {
		this.header = input.readByte();
		int ntickets = input.readInt();
		
		for (int i = 0; i < ntickets; i++)
			tickets.add(TicketData.readFromStream(input));

	}

    @SideOnly(Side.CLIENT)
    public void actionClient(World world, EntityPlayer player){
    	OverlayLoadedChunks.instance().setupTable(this.tickets);
    }		
	
}
