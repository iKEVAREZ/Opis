package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface ISerializable {
	void writeToStream(DataOutputStream stream) throws IOException;
	//Object readFromStream(DataInputStream stream) throws IOException;
}
