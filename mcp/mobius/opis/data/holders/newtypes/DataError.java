package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;

public class DataError implements ISerializable {

	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
	}

	public static DataError readFromStream(DataInputStream stream) throws IOException {
		return new DataError();
	}
}
