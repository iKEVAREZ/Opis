package mcp.mobius.opis.commands.client;

import javax.swing.JFrame;
import javax.swing.JLabel;

import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.MessageHandler;
import mcp.mobius.opis.network.OpisPacketHandler;
import mcp.mobius.opis.network.enums.ClientCommand;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.Packet_ClientCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatMessageComponent;

public class CommandOpis extends CommandBase {

	@Override
	public String getCommandName() {
		return "opis";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (!(icommandsender instanceof Player)){
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("You are not a normal client and can't open the Swing interface."));
			return;
		}
		
		if (!Message.COMMAND_OPEN_SWING.canPlayerUseCommand((Player)icommandsender)){
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("Your access level prevents you from doing that."));
			return;			
		}
		
		PlayerTracker.instance().playersSwing.add((Player)icommandsender);
		((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_ClientCommand.create(ClientCommand.SHOW_SWING));
		OpisPacketHandler.sendFullUpdate((Player)icommandsender);
	}
	
	@Override
    public int getRequiredPermissionLevel(){
        return 0;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender){
		return true;
    }	
}
