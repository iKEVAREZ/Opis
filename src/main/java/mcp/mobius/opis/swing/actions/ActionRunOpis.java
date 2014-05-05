package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ActionRunOpis implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		PacketDispatcher.sendPacketToServer(Packet_ReqData.create(Message.COMMAND_START));
	}
}
