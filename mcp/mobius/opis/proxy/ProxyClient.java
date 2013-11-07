package mcp.mobius.opis.proxy;

import java.awt.Font;
import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import mapwriter.api.MwAPI;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.gui.font.Fonts;
import mcp.mobius.opis.gui.font.TrueTypeFont;
import mcp.mobius.opis.gui.screens.ScreenBase;
import mcp.mobius.opis.gui.screens.ScreenTileEntity;
import mcp.mobius.opis.overlay.OverlayLoadedChunks;
import mcp.mobius.opis.overlay.OverlayMeanTime;

public class ProxyClient extends ProxyServer {
	
	public static TrueTypeFont fontMC8, fontMC12, fontMC16, fontMC18, fontMC24;	
	
	@Override
	public void init(){
		//MwAPI.registerDataProvider("Loaded chunks", OverlayLoadedChunks.instance);
		MwAPI.registerDataProvider("Loaded chunks", OverlayLoadedChunks.instance());
		MwAPI.registerDataProvider("Mean time",     OverlayMeanTime.instance());
		
		//fontMC12 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 12, false);
		//fontMC24 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 24, false);
		//fontMC8  = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"),  8, true);
		//fontMC12 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 12, true);
		//fontMC16 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 16, true);		
		//fontMC18 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 18, true);		
		//fontMC24 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 24, true);
		fontMC8 = Fonts.loadSystemFont("Monospace", 12, true, Font.TRUETYPE_FONT | Font.BOLD);
	}

	@Override	
	public void displayTileEntityList(ArrayList<TileEntityStats> stats){
		ScreenBase screenTEs = new ScreenTileEntity(null, stats);
		screenTEs.display();		
	}
	
	
}
