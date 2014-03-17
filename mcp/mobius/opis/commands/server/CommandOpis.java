package mcp.mobius.opis.commands.server;

import javax.swing.JFrame;
import javax.swing.JLabel;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandOpis extends CommandBase {

	@Override
	public String getCommandName() {
		return "opis";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		showUI();
	}

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }	
	
    public static void showUI() {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }    
    
}
