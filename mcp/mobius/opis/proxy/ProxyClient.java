package mcp.mobius.opis.proxy;

import mapwriter.api.MwAPI;
import mcp.mobius.opis.overlay.OverlayLoadedChunks;

public class ProxyClient extends ProxyServer {
	
	@Override
	public void init(){
		//MwAPI.registerDataProvider("Loaded chunks", OverlayLoadedChunks.instance);
		MwAPI.registerDataProvider("Loaded chunks", new OverlayLoadedChunks());
	}
}
