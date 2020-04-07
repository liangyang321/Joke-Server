/*--------------------------------------------------------

1. Name / Date: Ang Li/ 1/24/2020

2. Java version used, if not the official version for the class:

1.8.0_222

3. Precise command-line compilation examples / instructions:

e.g.:

> javac JokeServer.java
> javac JokeClient.java
> javac JokeClientAdmin.java


4. Precise examples / instructions to run this program:


> java JokeServer
> java JokeClient
> java JokeClientAdmin

All acceptable commands are displayed on the various consoles.

This runs across machines, in which case you have to pass the IP address of
the server to the clients. For exmaple, if the server is running at
140.192.1.22 then you would type:

> java JokeClient 140.192.1.22
> java JokeClientAdmin 140.192.1.22

5. List of files needed for running the program.

e.g.:

 a. checklist.html
 b. JokeServer.java
 c. JokeClient.java
 d. JokeClientAdmin.java

5. Notes:

e.g.:

My random generator will probably not perform the real random. It's a fake random generator.
*/


import java.io.*;
import java.net.*;

/**
 * this class create another client into port 1008 which can change the mode of server
 * @author liang
 *
 */
public class JokeClientAdmin {
	public static void main(String[] args) {
		String hostName;
		if (args.length < 1) hostName = "localhost";
		else hostName = args[0];
		
		System.out.println("admin_client starts");
		System.out.println("using server:" + hostName + ", port :10008");
		
		//get input stream from keyboard
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			String commad;
			do {
				System.out.println("please enter the mode you want, type P is proverb mode, type J"
						+ "is joke mode, enter quit to quit");
				System.out.flush();         //flush stream
				commad = in.readLine();     //enter command p:change to proverb mode, j:joke mode
				if (commad.indexOf("quit")<0) talk_to_server(commad,hostName);
			}
			while(commad.indexOf("quit") < 0);
		}
		catch(IOException e) {
			
		}
	}
	
	/**
	 * pass admin_command to mode_worker server 
	 * @param command
	 * @param serverName
	 */
	static void talk_to_server(String command, String serverName) {
		Socket socket;		
		BufferedReader in;  //read from server
		PrintStream out;    //write out to server
		String text_in;     //store text from server
		try {
			socket = new Socket(serverName,10008); 
			
			//allows read from server
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//allows write to server
			out = new PrintStream (socket.getOutputStream());
			
			//write out to server
			out.println(command);
			out.flush();
			
			//read back from server side
			while((text_in = in.readLine()) != null) {
				System.out.println(text_in);
			}
			
			socket.close();
		}catch (IOException e) {
			System.out.println("socket error");
		}
	}
}
