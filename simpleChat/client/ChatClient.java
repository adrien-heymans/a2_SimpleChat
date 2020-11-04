// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
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
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String loginID;
public void setLoginID(String s) {
	this.loginID=s;
}

;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   * @param loginID the id of the client 
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID,String host, int port, ChatIF clientUI) 
    throws IOException 
  {
	  
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID =loginID;
    
    if (loginID=="") {
    	System.out.println("ERROR : No login id provided.");
    	quit();
    }
    
    openConnection();
    try {
    	//sending the login ID to the servewr whn a client connects
		sendToServer("#login <"+loginID+">");
		System.out.println("<"+loginID+"> has logged on");
	} catch (Exception e) {
		
		
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
 
  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  
  {
	  //this is executed whemn the server quits, it send a message to all the clients, except serverConsol, and theyn all disconnect from the server
	  if (msg.equals("#disconnectClient")&&!(loginID.equals("admin"))) {
		  System.out.println("WARNING - The server has stopped listening for connections\n"
		  		+ "SERVER SHUTTING DOWN! DISCONNECTING!\n"
		  		+ "Abnormal termination of connection.\n"
		  		+ "");
		  try {
			  
			  //closing the connection
			closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  else {
		  //for every other kind of message 
		  clientUI.display(msg.toString());
	  } 
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
 * @throws Exception 
   */
  public void handleMessageFromClientUI(String message) throws Exception {
	  
	  //IMPLEMENTING THE USERS COMMANDS WE DETECT THEM IF THE FIRST CHARACTER IS A "#"
	 
	 
	 //if this is a special command
	 if(message.charAt(0)=='#'){
		 
		
		 //quitting command
		if(message.substring(0,5).equals("#quit")) {
			sendToServer(this.loginID+ " has disconected");
			
			quit();
			
		}
		
		//log in command
		else if(message.substring(0,6).equals("#login")) {
			//System.out.println("here 5");
			
			//we disregard if we are already connected
			if (isConnected()) {
				clientUI.display("You are already connected");
			}
			
			if (message.length()==6) {
				clientUI.display("You need to enter a username first");
				
			}
			else {
				if (loginID==null) {
					loginID = message.substring(7,message.length());
				}
				
			
			
				try {
					//openning the connection if we are not already connected
					
					openConnection();
					clientUI.display("Login succesfull");
				}
				catch (IOException ex) {
					
					clientUI.display("Client connected");
				}
			}
			
		}
		//log off command
		else if(message.substring(0,7).equals("#logoff")) {
			sendToServer(this.loginID+ " has disconected");
			
			 
				try {
					//clong the connection
					closeConnection();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e);
				}
			}
		
		//setting host command
		else if(message.substring(0,8).equals("#sethost")) {
			 
			
			//if we are connected we diregard the command
			if (!isConnected()) {
				setHost(message.substring(8, message.length()));
				
				//printing the message on the console 
				clientUI.display("Host set to "+getHost());
			}
			else {
				//if the client is connected, we can't change the host
				clientUI.display("WARNING : You need to #logoff before changing the host.");
			}
			
		}
		else if(message.substring(0,8).equals("#setport")) {
			 //System.out.println("here 4");
			
			//if we are connected we disreagrd the command
			if (!isConnected()) {
				
				setPort(Integer.parseInt(message.substring(9, message.length())));
				clientUI.display("Port set to "+getPort());
			}
			else {
				//if the client is connected, we can't change the host
				clientUI.display("WARNING : You need to #logoff before changing the port.");
			}
		}
		//getting the hiost command
		else if(message.substring(0,8).equals("#gethost")) {
			 //System.out.println("here 6");
			clientUI.display("Host : "+this.getHost());
			
		}
		//getting the port command
		else if(message.substring(0,8).equals("#getport")) { 
			 //System.out.println("here 7");
			clientUI.display("Port : "+this.getPort());
			
		}
		
	
		
	 }
	 else {
		
		 
		
			
			try
		    {
				
			//we send the message to the server
		      sendToServer(message);
		    }
		    catch(IOException e)
		    {
		    	//if a problem happened 
		      clientUI.display
		        ("Could not send message to server.  Terminating client.");
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
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  public String getLoginID() {
	  return this.loginID;
  }



	protected void connectionClosed() {
		clientUI.display("Connection closed");
	}
	
	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	protected void connectionException(Exception exception) {
		
	}
	
	/**
	 * Hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
	protected void connectionEstablished() {
		//clientUI.display("Connected to server on port "+getPort());
	}
}

//End of ChatClient class
