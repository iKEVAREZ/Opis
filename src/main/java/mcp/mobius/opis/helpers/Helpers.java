package mcp.mobius.opis.helpers;

import net.minecraft.network.TcpReaderThread;
import net.minecraft.network.TcpWriterThread;
import net.minecraft.server.ServerListenThread;
import net.minecraft.server.ThreadMinecraftServer;
import cpw.mods.fml.relauncher.Side;

public class Helpers {
	public static Side getEffectiveSide(){
        Thread thr = Thread.currentThread();

        if ((thr instanceof ThreadMinecraftServer) || (thr instanceof ServerListenThread) || (thr instanceof TcpWriterThread) || (thr instanceof TcpReaderThread))
        {
            return Side.SERVER;
        }
        
        return Side.CLIENT;		
	}
}
