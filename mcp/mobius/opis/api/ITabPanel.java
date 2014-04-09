package mcp.mobius.opis.api;

import mcp.mobius.opis.network.enums.Message;

public interface ITabPanel {
	String  getTabTitle();
	String  getTabRefName();
	Message getFocusMessage();
}
