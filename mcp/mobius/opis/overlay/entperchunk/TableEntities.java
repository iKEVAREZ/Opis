package mcp.mobius.opis.overlay.entperchunk;

import cpw.mods.fml.common.network.PacketDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import mapwriter.map.MapView;
import mapwriter.map.mapmode.MapMode;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.data.holders.TargetEntity;
import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.widgets.tableview.TableRow;
import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
import mcp.mobius.opis.network.client.Packet_ReqData;
import mcp.mobius.opis.network.enums.DataReq;
import mcp.mobius.opis.overlay.entperchunk.OverlayEntityPerChunk.ReducedData;

public class TableEntities extends ViewTable {
	MapView mapView;
	MapMode mapMode;
	OverlayEntityPerChunk overlay;		
	
	public TableEntities(IWidget parent, OverlayEntityPerChunk overlay) { 	
		super(parent);
		this.overlay = overlay;			
	}
	
	public void setMap(MapView mapView, MapMode mapMode){
	    this.mapView = mapView;
		this.mapMode = mapMode;			
	}
	
	@Override
	public void onMouseClick(MouseEvent event){
		TableRow row = this.getRow(event.x, event.y);
		if (row != null){
			//CoordinatesBlock coord = ((EntityStats)row.getObject()).getCoord();
			//PacketDispatcher.sendPacketToServer(Packet_ReqTeleport.create(coord));
			int eid = ((EntityStats)row.getObject()).getID();
			int dim = ((EntityStats)row.getObject()).getCoordinates().dim;
			PacketDispatcher.sendPacketToServer(Packet_ReqData.create(DataReq.COMMAND, DataReq.TELEPORT, DataReq.ENTITIES, new TargetEntity(eid, dim)));
			Minecraft.getMinecraft().setIngameFocus();			
		}
	}
}
