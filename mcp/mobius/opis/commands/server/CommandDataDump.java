package mcp.mobius.opis.commands.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.stats.StatsMod;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.server.PlayerTracker;
import mcp.mobius.opis.tools.ModIdentification;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;

public class CommandDataDump extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_dump";
	}

	@Override
	public String getCommandNameOpis() {
		return this.getCommandName();
	}	
	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		ArrayList<StatsMod> modStats = TileEntityManager.getModStats();
        try {
			BufferedWriter out = new BufferedWriter(new FileWriter("mods.csv"));
			
			out.write(String.format("modid,ntes,mean,geommean,variance,min,max,sum,median\n"));
			
			for (StatsMod stat: modStats)
				out.write(String.format("%s,%d,%f,%f,%f,%f,%f,%f,%f\n", stat.getModID(),
																		stat.dstat.getN(),
																		stat.dstat.getMean(),
																		stat.dstat.getGeometricMean(),
																		stat.dstat.getVariance(),
																		stat.dstat.getMin(),
																		stat.dstat.getMax(),
																		stat.dstat.getSum(),
																		stat.getMedian()));
			out.close();
			if (icommandsender instanceof DedicatedServer){}
				//PacketDispatcher.sendPacketToServer(new Packet3Chat(ChatMessageComponent.createFromText("\u00A7oMods datadump done.")));
			else
				PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText("\u00A7oMods datadump done.")), (Player)icommandsender);
			
		} catch (IOException e) {
			modOpis.log.log(Level.WARNING, "Failed to write mods data");
			
			if (icommandsender instanceof DedicatedServer){}
				//PacketDispatcher.sendPacketToServer(new Packet3Chat(ChatMessageComponent.createFromText("\u00A7oMods datadump failed.")));	
			else
				PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText("\u00A7oMods datadump failed.")), (Player)icommandsender);
			
			e.printStackTrace();
		}	
        
        int nentities = TileEntityManager.stats.size();
        ArrayList<StatsTileEntity> entStats = TileEntityManager.getTopEntities(nentities);
        try {
			BufferedWriter out = new BufferedWriter(new FileWriter("tileentities.csv"));
			
			out.write(String.format("name,mod,dim,x,y,z,chunkX,chunkZ,npoints,mean,geommean,variance,min,max,sum,median\n"));

			for (StatsTileEntity stat: entStats){
				
				ItemStack is;
				String name  = String.format("te.%d.%d", stat.getID(), stat.getMeta());
				String modID = "<Unknown>";
				
				try{
					is = new ItemStack(stat.getID(), 1, stat.getMeta());
					name  = is.getDisplayName();
					modID = ModIdentification.idFromStack(is);
				}  catch (Exception e) {	}
				
				out.write(String.format("%s,%s,%d,%d,%d,%d,%d,%d,%d,%f,%f,%f,%f,%f,%f,%f\n", name,
																		modID,
																		stat.getCoordinates().dim,
																		stat.getCoordinates().x,
																		stat.getCoordinates().y,
																		stat.getCoordinates().z,
																		stat.getChunk().chunkX,
																		stat.getChunk().chunkZ,
																		stat.dstat.getN(),
																		stat.dstat.getMean(),
																		stat.dstat.getGeometricMean(),
																		stat.dstat.getVariance(),
																		stat.dstat.getMin(),
																		stat.dstat.getMax(),
																		stat.dstat.getSum(),
																		stat.getMedian()));
			}
			out.close();
			
			if (icommandsender instanceof DedicatedServer){}
				//PacketDispatcher.sendPacketToServer(new Packet3Chat(ChatMessageComponent.createFromText("\u00A7oTileEntities datadump done.")));				
			else
				PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText("\u00A7oTileEntities datadump done.")), (Player)icommandsender);
			
		} catch (IOException e) {
			modOpis.log.log(Level.WARNING, "Failed to write tile entities data");
			if (icommandsender instanceof DedicatedServer){}
				//PacketDispatcher.sendPacketToServer(new Packet3Chat(ChatMessageComponent.createFromText("\u00A7oTileEntities datadump failed.")));
			else
				PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText("\u00A7oTileEntities datadump failed.")), (Player)icommandsender);
			e.printStackTrace();
		}	        
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
		if (sender instanceof DedicatedServer) return true;
		if ((sender instanceof EntityPlayerMP) && ((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;
		if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) return true;
		return PlayerTracker.instance().isPrivileged(((EntityPlayerMP)sender).username);
    }

	@Override
	public String getDescription() {
		return "Dumps chunks and TEs data to csv.";
	}

}
