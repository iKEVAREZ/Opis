package mcp.mobius.opis.gui.screens;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import mapwriter.Mw;
import mapwriter.api.MwAPI;
import mapwriter.gui.MwGui;
import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.gui.events.MouseEvent;
import mcp.mobius.opis.gui.interfaces.CType;
import mcp.mobius.opis.gui.interfaces.IWidget;
import mcp.mobius.opis.gui.interfaces.WAlign;
import mcp.mobius.opis.gui.widgets.WidgetGeometry;
import mcp.mobius.opis.gui.widgets.tableview.TableRow;
import mcp.mobius.opis.gui.widgets.tableview.ViewTable;
import mcp.mobius.opis.overlay.OverlayMeanTime;

public class ScreenChunks extends ScreenBase {

	public class ChunksTable extends ViewTable{
		public ChunksTable(IWidget parent) { 	
			super(parent);
		}
		
		@Override
		public void onMouseClick(MouseEvent event){
			TableRow row = this.getRow(event.x, event.y);
			if (row != null){
				CoordinatesChunk coord = ((ChunkStats)row.getObject()).coord;
				OverlayMeanTime.instance().setSelectedChunk(coord.dim, coord.chunkX, coord.chunkZ);
				MwAPI.setCurrentDataProvider(OverlayMeanTime.instance());
				this.mc.displayGuiScreen(new MwGui(Mw.instance, coord.dim, coord.x, coord.z));
			}
		}
	}	
	
	
	
	public ScreenChunks(GuiScreen parent, ArrayList<ChunkStats> chunks) {
		super(parent);
		
		ChunksTable table = (ChunksTable)this.getRoot().addWidget("Table", new ChunksTable(null));

		table.setGeometry(new WidgetGeometry(50.0, 50.0, 80.0, 80.0,CType.RELXY, CType.RELXY, WAlign.CENTER, WAlign.CENTER));
		
	    table.setColumnsAlign(WAlign.CENTER, WAlign.CENTER, WAlign.CENTER, WAlign.CENTER)
		     //.setColumnsTitle("\u00a7a\u00a7oType", "\u00a7a\u00a7oPos", "\u00a7a\u00a7oUpdate Time")
	    	 .setColumnsTitle("Dim", "Pos", "N Ents", "Update Time")
			 .setColumnsWidth(40, 20, 20, 20)
			 .setRowColors(0xff808080, 0xff505050)
			 .setFontSize(1.0f);		

		for (ChunkStats data : chunks){
			table.addRow(data, 
				     	 String.format("%3d", data.coord.dim),
					     String.format("[ %4d %4d ]", 	data.coord.chunkX, data.coord.chunkZ),
					     String.format("%d", data.nentities),
					     String.format("%.5f ms",data.updateTime/1000.0));
		}	    
	    
	}
		
	
}
