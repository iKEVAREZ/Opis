package mcp.mobius.opis.proxy;

import net.minecraft.util.ResourceLocation;
import mapwriter.api.MwAPI;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.gui.font.Fonts;
import mcp.mobius.opis.overlay.OverlayLoadedChunks;
import mcp.mobius.opis.overlay.OverlayMeanTime;

public class ProxyClient extends ProxyServer {
	
	@Override
	public void init(){
		//MwAPI.registerDataProvider("Loaded chunks", OverlayLoadedChunks.instance);
		MwAPI.registerDataProvider("Loaded chunks", OverlayLoadedChunks.instance());
		MwAPI.registerDataProvider("Mean time",     OverlayMeanTime.instance());
		
		modOpis.fontMC12 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 12, false);
		modOpis.fontMC24 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 24, false);		
	}
}
