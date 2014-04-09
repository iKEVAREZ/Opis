package mcp.mobius.opis.api;

import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.swing.SelectedTab;

public interface ITabPanel {
	String      getTabTitle();
	String      getTabRefName();
	SelectedTab getSelectedTab();
}
