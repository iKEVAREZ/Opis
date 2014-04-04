package mcp.mobius.opis.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mcp.mobius.mobiuscore.profiler.ProfilerSection;
import mcp.mobius.opis.modOpis;

public class ActionRunOpisClient implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		modOpis.profilerRunClient = true;
		ProfilerSection.activateAll();
	}
}
