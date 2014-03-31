package mcp.mobius.opis.api;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import mcp.mobius.opis.swing.SwingUI;

public enum TabPanelRegistrar {
	INSTANCE;
	
	private ArrayList<ITabPanel>       panels = new ArrayList<ITabPanel>();
	private HashMap<String, ITabPanel> lookup = new HashMap<String, ITabPanel>(); 
	
	public ITabPanel registerTab(ITabPanel panel){
		this.panels.add(panel);
		this.lookup.put(panel.getTabRefName(), panel);
		SwingUI.instance().getTabbedPane().addTab(panel.getTabTitle(), (JPanel)panel);
		return panel;
	}
	
	public ITabPanel getTab(String refname){
		return lookup.get(refname);
	}

	public JPanel getTabAsPanel(String refname){
		return (JPanel)lookup.get(refname);
	}	
	
	public ArrayList<ITabPanel> getTabs(){
		return this.panels;
	}
}
