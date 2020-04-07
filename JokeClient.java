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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class JokeClient {
	
	/*keep user_name in client side, and when we want print, we just need to add name in the 
	 * beginning. For each client thread, only one user_name. 
	*/
	private static String user_name; 
	private static int[] joke_visited;       //visited check
	private static int[] proverb_visited;    //visited check
	private static Set<String> joke_set;     //unique and cycle check
	private static Set<String> proverb_set;  //unique and cycle check
	
	private static ArrayList<String> jokes;
	private static ArrayList<String> proverbs;
	
	public static void main(String[] args) {
		String hostName;
		if (args.length < 1) hostName = "localhost"; 
		else hostName = args[0];
		
		System.out.println("client starts");
		System.out.println("using server:" + hostName + ", port :10007");
		
		jokes = new ArrayList<String>();
		jokes.add("JA:");
		jokes.add("JB:");
		jokes.add("JC:");
		jokes.add("JD:");
		
		proverbs = new ArrayList<String>();
		proverbs.add("PA");		
		proverbs.add("PB");		
		proverbs.add("PC");		
		proverbs.add("PD");		
		
		joke_visited = new int[4];
		proverb_visited = new int[4];
		Map<String, Integer> m = new HashMap<String,Integer>();
		joke_set = new HashSet<String>();
		proverb_set = new HashSet<String>();
	
		//get input stream from keyboard
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.println("please enter your name here:");
			//keep user name one time
			user_name = in.readLine(); 
			
			String command;
			do {
				System.out.println("press enter to "
						+ "see what you can got jokes or proverb, you might not "
						+ "get it, keep pressing ! quit to end ");
				System.out.flush();         //flush stream
				command = in.readLine();
				if (command.indexOf("quit")<0) talk_to_server(command,hostName);
			}
			while(command.indexOf("quit") < 0);
		}
		catch(IOException e) {
			
		}
	}

/*
 

	/**
	 * passed client command and serverName
	 * @param command
	 * @param serverName
	 */
	static void talk_to_server(String command, String serverName) {
		Socket socket;		
		BufferedReader in;  //read from server
		PrintStream out;    //write out to server
		String text_in;     //store text from server
		try {
			socket = new Socket(serverName,10007); 
			
			//allows read from server
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			//allows write to server
			out = new PrintStream (socket.getOutputStream());
			out.println(user_name);
			
			
			//read current mode and send current state of joke or proverb list during each request
			String mode = in.readLine();  
			int num = Integer.parseInt(mode);   //parse to Int
			if (num == 0) {
				//send to server the cookie if server is joke mode
				out.println(joke_visited[0]);   
				out.println(joke_visited[1]);
				out.println(joke_visited[2]);
				out.println(joke_visited[3]);
				
				
			} else if (num == 1) {
				//send to server the cookie if server is proverb mode
				out.println(proverb_visited[0]);
				out.println(proverb_visited[1]);
				out.println(proverb_visited[2]);
				out.println(proverb_visited[3]);
			}
			
			//press enter to continue
			out.println(command);
			out.flush();
			
		//	File file = new File("JokeLog.txt");
		//	if (!file.exists()) file.createNewFile();
			
		//	BufferedWriter fw = new BufferedWriter(new FileWriter(file));
		
			//read jokes or proverb's random number from server
			String str = in.readLine(); 
			int order = Integer.parseInt(str);
			
		
			String print_out = null; 
			
			//hard_code all the jokes and proverbs, based on the random number from server
			//, we will print different jokes and proverb
			
			if (num == 0) {                                      //check server mode, if in joke mode
				if (!joke_set.contains(jokes.get(order))) {      //unique check, 'order' is random # from server
					if (order == 0 ) {
						print_out = "JA: "  +user_name+" " +jokes.get(order);
					}
					else if (order ==1 ) {
						print_out = "JB "  +user_name+" "+ jokes.get(order);
					}
					else if (order == 2) {
						print_out = "JC "  +user_name+" "+ jokes.get(order);
					}
					else if (order == 3) {
						print_out = "JD "  +user_name+" "+ jokes.get(order);
					}
					System.out.println(print_out);         
					mark_vistied_joke(print_out, joke_visited);    //mark displayed joke as visited 
					
					joke_set.add(jokes.get(order)); //put displayed joke into set   
					if (joke_set.size() ==4) {      //check joke cycle first,if full, reset and notice client
						joke_set.clear();
						System.out.println("joke cycle done");
						//reset joke_visited array to 0
						for (int i = 0; i <4; i++) {
							joke_visited[i] = 0;
						}
					}
				}else {
					System.out.println("returned random number already displayed");
				}
			}
			else if (num == 1) {
				if (!proverb_set.contains(proverbs.get(order))) {      //unique check, 'order' is random # from server
					if (order == 0 ) {
						print_out = "PA"  +user_name+ proverbs.get(order);
					}
					else if (order ==1 ) {
						print_out = "PB"  +user_name+ proverbs.get(order);
					}
					else if (order == 2) {
						print_out = "PC"  +user_name+ proverbs.get(order);
					}
					else if (order == 3) {
						print_out = "PD"  +user_name+ proverbs.get(order);
					}
					System.out.println(print_out);         
					mark_vistied_proverb(print_out, proverb_visited);    //mark cookie as visited 
					
					proverb_set.add(proverbs.get(order)); //put displayed proverb into set   
					if (proverb_set.size() ==4) {         //check proverb cycle first,if full, reset and notice client
						proverb_set.clear();
						System.out.println("proverb cycle done");
						//reset proverb_visited array to 0
						for (int i = 0; i <4; i++) {
							proverb_visited[i] = 0;
						}
					}
				}else {
					System.out.println("returned random number already displayed");
				}
				
			}
//			//fw.close();
			socket.close();
		}catch (IOException e) {
			System.out.println("socket error");
		}
	}

	
	/**
	 * check jokes or proverbs from server and mark it as visited if displayed 
	 * Store into client cookie and send to server
	 * @param str
	 * @param list
	 */
	private static void mark_vistied_proverb(String str, int[] list) {
		if (str.startsWith("PA")) list[0] =1;
		else if (str.startsWith("PB")) list[1] =1;
		else if (str.startsWith("PC")) list[2] =1;
		else if (str.startsWith("PD")) list[3] =1;
	}


	/**
	 * check jokes or proverbs from server and mark it as visited if displayed 
	 * Store into client cookie and send to server
	 * @param str
	 * @param list
	 */
	private static void mark_vistied_joke(String str, int[] list ) {
		if (str.startsWith("JA")) list[0] =1;
		else if (str.startsWith("JB")) list[1] =1;
		else if (str.startsWith("JC")) list[2] =1;
		else if (str.startsWith("JD")) list[3] =1;
	}
}
