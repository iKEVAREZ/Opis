package mcp.mobius.opis.network;

public class ClientCommandHandler {
	
	private static ClientCommandHandler _instance;
	private ClientCommandHandler(){}
	
	public static ClientCommandHandler instance(){
		if(_instance == null)
			_instance = new ClientCommandHandler();			
		return _instance;
	}
	
	public void handle(byte cmd){
		System.out.printf("Received client cmd with id %s\n", cmd);
	}
}
