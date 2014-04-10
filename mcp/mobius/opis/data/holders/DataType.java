package mcp.mobius.opis.data.holders;

import java.util.EnumSet;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.basetypes.SerialFloat;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.basetypes.SerialNumeral;
import mcp.mobius.opis.data.holders.basetypes.SerialString;
import mcp.mobius.opis.data.holders.basetypes.TargetEntity;
import mcp.mobius.opis.data.holders.basetypes.TicketData;
import mcp.mobius.opis.data.holders.newtypes.DataBlockRender;
import mcp.mobius.opis.data.holders.newtypes.DataBlockTick;
import mcp.mobius.opis.data.holders.newtypes.DataChunk;
import mcp.mobius.opis.data.holders.newtypes.DataDimension;
import mcp.mobius.opis.data.holders.newtypes.DataEntity;
import mcp.mobius.opis.data.holders.newtypes.DataEntityRender;
import mcp.mobius.opis.data.holders.newtypes.DataHandler;
import mcp.mobius.opis.data.holders.newtypes.DataNetworkTick;
import mcp.mobius.opis.data.holders.newtypes.DataPacket;
import mcp.mobius.opis.data.holders.newtypes.DataPacket250;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntity;
import mcp.mobius.opis.data.holders.newtypes.DataTileEntityRender;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.newtypes.DataTimingMillisecond;
import mcp.mobius.opis.data.holders.stats.StatAbstract;
import mcp.mobius.opis.data.holders.stats.StatsChunk;

import com.google.common.collect.HashBiMap;

public enum DataType {
	AMOUNTHOLDER    (AmountHolder.class),
	COORDINATESBLOCK(CoordinatesBlock.class),
	COORDINATESCHUNK(CoordinatesChunk.class), 
	SERIALDOUBLE    (SerialDouble.class),
	SERIALFLOAT     (SerialFloat.class),
	SERIALINT       (SerialInt.class),
	SERIALLONG      (SerialLong.class),
	SERIALSTRING    (SerialString.class),
	TARGETENTITY    (TargetEntity.class),
	TICKETDATA      (TicketData.class),
	
	DATABLOCKRENDER (DataBlockRender.class),
	DATABLOCKTICK   (DataBlockTick.class),
	DATACHUNK       (DataChunk.class),
	DATADIMENSION   (DataDimension.class),
	DATAENTITY      (DataEntity.class),
	DATAENTITYRENDER(DataEntityRender.class),
	DATAHANDLER     (DataHandler.class),
	DATANETWORKTICK (DataNetworkTick.class),
	DATAPACKET      (DataPacket.class),
	DATAPACKET250   (DataPacket250.class),
	DATATILEENTITY  (DataTileEntity.class),
	DATATILEENTITYRENDER(DataTileEntityRender.class),
	DATATIMING      (DataTiming.class),
	DATATIMINGMILLISECOND(DataTimingMillisecond.class),
	STATABSTRACT    (StatAbstract.class),
	STATSCHUNK      (StatsChunk.class);

	private Class clazz;
	private static HashBiMap<DataType, Class> bimap = HashBiMap.create(50);
	
    static
    {
        for (DataType type : DataType.values())
    		bimap.put(type, type.clazz);
    }	
	
	private DataType(Class clazz){
		this.clazz = clazz;
	}
	
	public static DataType getForClass(Class clazz){
		DataType type = bimap.inverse().get(clazz);
		if (type == null){
			modOpis.log.warning(String.format("Class %s was not registered with the DataType enum", clazz));			
		}
		return type;
	}
	
	public static Class getForOrdinal(int ordinal){
		Class retVal;
		try{
			DataType type   = DataType.values()[ordinal];
			retVal = bimap.get(type);
			if (retVal == null){
				modOpis.log.warning(String.format("DataType doesn't have a class registered for %s", type));
			}
		} catch (Exception e) {
			retVal = null;
			modOpis.log.warning(String.format("Index out of bound for ordinal %d in DataType", ordinal));
		}
		return retVal;
	}	
}
