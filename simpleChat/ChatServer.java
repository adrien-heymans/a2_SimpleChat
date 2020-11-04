// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 



import ocsf.client.*;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatServer extends AbstractServer
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF serverUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatServer(int port, ChatIF serverUI) 
    throws IOException 
  {
    super(port); //Call the superclass constructor
    this.serverUI = serverUI;
    listen();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    serverUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
 * @throws Exception 
   */
  public void handleMessageFromServerUI(String message) throws Exception {
	  
	 if(message.charAt(0)=='#'){
	  
		if(message.substring(0,5).equals("#quit")) {
			quit();
			
		}
		else if(message.substring(0,5).equals("#stop")) {
			System.out.println("stoping");
			
		}
		
		else if(message.substring(0,6).equals("#start")) {
			
			listen();
		}
		
		
		else if(message.substring(0,6).equals("#close")) {
			System.out.println("closing");
			
		}
		else if(message.substring(0,8).equals("#setport")) {
			 //System.out.println("here 4");
			setPort(Integer.parseInt(message.substring(9, message.length())));
			System.out.println("New port set to "+getPort());
			
		}

		else if(message.substring(0,8).equals("#getport")) {
			 //System.out.println("here 7");
			serverUI.display("Port : "+this.getPort());
			
		}
	 }
	 else {
		try
	    {
		System.out.println("here");
	      sendToAllClients("SERVER MSG>"+ message);
	     System.out.println("here 2");
	      
	    }
	    catch(Exception e)
	    {
	      serverUI.display("Could not send message to clients.  Terminating admin.");
	      quit();
	    }
	}
    
    
    
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e) {}
    System.exit(0);
  }


@Override
protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
	// TODO Auto-generated method stub
	
}
}
//End of ChatClient class