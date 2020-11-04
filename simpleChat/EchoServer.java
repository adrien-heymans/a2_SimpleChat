// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg1, ConnectionToClient client)
  {
	  //System.out.println("here 4");
	  String msg = (String) msg1;
	  //System.out.println(msg);
	  
	//WE HAVE TO PUT THE MODIFICATION HERE FOR THE SERVER CONSOLE 
	
	  
	 if ( msg.length()>11 && ((String) msg).charAt(11)=='#') {
		 System.out.println("Command received "+(String) msg);
		 //System.out.println(msg);
		
		 if (msg.equals("SERVER MSG>#quit")) {	
			 //System.out.println("here 5");
			 try {
				this.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 else if (msg.equals("SERVER MSG>#stop")) { 
			 //System.out.println("here 6");
			 stopListening();
		 }
		 else if (msg.equals("SERVER MSG>#close")) {
			 //System.out.println("here 7");
			 stopListening();
			 sendToAllClients("#disconnectClient");
			 
		 }
		 else if (msg.equals("SERVER MSG>#start")) {
			// System.out.println("here 8");
			 try {
				listen();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace(); 
			}
		 }
		 else if (msg.substring(0,19).equals("SERVER MSG>#setport")) {
			 
			 //System.out.println("here 9");
			 
			 if (!isListening()) {
				 //System.out.println("here 10");
				 int newPort = Integer.parseInt(msg.substring(20,msg.length()));
				 System.out.println(newPort);
				 
				 setPort(newPort); 
				 //System.out.println("here 11");
				 System.out.println("Port changed to : "+ ((String) msg).substring(20,((String) msg).length()));
				//System.out.println("here 12");
			 
			 }
			 
		 }
		 
		 else if (msg.equals("SERVER MSG>#getport")) {
			 System.out.println("Port : "+getPort());
			 
		 }
		 
	 }
	 else {
		 //System.out.println("here 11");
		 
		if (msg.length()>5 && msg.substring(0,6).equals("#login")) {
			System.out.println("A new client is attempting to connect to the server.");
			System.out.println("Message received: " + msg + " from null.");
			
			client.setInfo("id",msg.substring(8, msg.length()-1));
			System.out.println(client.getInfo("id")+ " has logged on.");
			
		}
		else if (msg.length()>10 && msg.substring(0,11).equals("SERVER MSG>")){
			
			System.out.println("Message received: " + msg + " from " + client.getInfo("id"));
		    this.sendToAllClients(msg);
		}
		else {
	  
		    System.out.println("Message received: " + msg + " from " + client.getInfo("id"));
		    this.sendToAllClients(client.getInfo("id")+">"+msg);
		}
	 }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex)  
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println ("New client connected : "+ client.getInfo("id") );
	  
	  
  }
 protected void clientDisconnected( ConnectionToClient client) {
	  System.out.println("Client disconnected : "+ client.getInfo("id") );
  }
  
}
//End of EchoServer class
