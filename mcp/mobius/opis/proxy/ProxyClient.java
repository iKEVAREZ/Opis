package mcp.mobius.opis.proxy;

import mapwriter.api.MwAPI;
import mcp.mobius.opis.overlay.OverlayLoadedChunks;
import mcp.mobius.opis.overlay.OverlayMeanTime;

public class ProxyClient extends ProxyServer {
	
	@Override
	public void init(){
		//MwAPI.registerDataProvider("Loaded chunks", OverlayLoadedChunks.instance);
		MwAPI.registerDataProvider("Loaded chunks", new OverlayLoadedChunks());
		MwAPI.registerDataProvider("Mean time", OverlayMeanTime.instance());
	}
}
