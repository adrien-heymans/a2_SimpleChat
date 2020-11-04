// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;
import java.util.Random;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /*
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 
  String message;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String loginID,String host, int port) 
  {
	 message ="";
    try 
    {
      client= new ChatClient(loginID,host,port,this);
      
      
    } 
    catch(IOException exception) 
    {
      display("Cannot open connection.  Awaiting command.");
      
    }
    
    // Create scanner object to read from console
    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      

      while (true) 
      {
        message = fromConsole.nextLine();
       
        if (message==null) {
        	//System.out.println("here 1");
        	client.handleMessageFromClientUI(" "); 
        	
        }
        
        else {
        	//System.out.println("here 2");
        	
        	
        	if(message.length()>7 && message.substring(0,8).equals("#setport")) {
   			 //System.out.println("here 4");
	   			if (!client.isConnected()) {
	   			System.out.println("here 5");
	   				client.setPort(Integer.parseInt(message.substring(9, message.length())));
	   				display("Port set to "+client.getPort());
	   				
	   			}
	   			else { 
	   				display("WARNING : You need to #logoff before changing the port.");
	   			}
        	}
        	else if(message.length()>7 && message.substring(0,8).equals("#sethost") ) {
   			 //System.out.println("here 3");
	   			if (!client.isConnected()) {
	   				client.setHost(message.substring(9, message.length()));
	   				display("Host set to "+client.getHost());
	   			}
	   			else {
	   				display("WARNING : You need to #logoff before changing the host.");
	   			}
	   			
	   		}
        	else {
	        	//System.out.println(message);
	        	client.handleMessageFromClientUI(message); 
	        	//System.out.println("here 3");
        	}
        }
        
      }
    } 
    catch (Exception ex) {
      
    	display("Unexpected error while reading from console ! "+ex);
      
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println(message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
    String host = "";
    int port;
    String loginID="";
    
    //the first element of the array is always the iD
    
    try
    {
      loginID = args[0]; 
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
    	//default value
      host = "localhost";
    }
    try
    {
      host = args[1]; 
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
    	//default value 
      host = "localhost";
    }
    
    try
    {
      port = Integer.parseInt(args[2]);
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      port = DEFAULT_PORT;
    }
    
    
    
    ClientConsole chat= new ClientConsole(loginID,host, port );
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
