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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/**
 * this class will produce Joke or Proverb based on current mode to client
 * @author liang
 *
 */
class Worker extends Thread{		
	//each connection has own list for joke and proverb,
	//we will create two sets for each thread to track

	Socket socket; 
	//assign each worker a socket object and get its stream
	Worker(Socket s){
		socket = s;
	}
	
	/**
	 * implements the task for each thread
	 */
	public void run() {
		System.out.println("Joker&Proverb_worker starts");
		System.out.println("thread id :" + Mode_worker.currentThread().getId());
		try {
			//allows read from client
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//allows write to client
			PrintStream out = new PrintStream(socket.getOutputStream());
			
			out.println(JokeServer.current_mode); //tell client mode first
			try {
				String user_name;
				user_name = in.readLine();    //read user_name from client
				
				String arr_1 = in.readLine(); //read jokes or proverbs visited state from client 0 OR 1
				String arr_2 = in.readLine(); //read jokes or proverbs visited state from client 0 0R 1
				String arr_3 = in.readLine(); //read jokes or proverbs visited state from client 0 OR 1
				String arr_4 = in.readLine(); //read jokes or proverbs visited state from client o OR 1	
				
				//read user command, basically read enter key
				String user_command;
				user_command = in.readLine();

				//mode.set_mode_state(1);
				talk_back_to_client(user_name,user_command,out,arr_1,arr_2,arr_3,arr_4);
			}
			catch (IOException x) {
			}
			socket.close();
		} catch (IOException e) {
			
		}
	}


	/**
	 * main functions that tell users some jokes, if admin change mode, need reactions here as well
	 * @param name
	 * @param out
	 */
	 private static void talk_back_to_client(String user_name,String user_command, PrintStream out,String a1,String a2, String a3, String a4) {
		if (JokeServer.current_mode == 0 && user_command.equals("")) {           //check current server mode and whether client press enter key
			out.println(tell_jokes_random_number(user_name, a1,a2,a3,a4));
		}

		else if (JokeServer.current_mode == 1 && user_command.equals("")) {      //check current server mode and whether client press enter key
			out.println(tell_proverbs_random_number(user_name, a1,a2,a3,a4));
			}
	}
	
	 /**
	  * return a random number based on the cookie from client
	  * @param user_name
	  * @param a1
	  * @param a2
	  * @param a3
	  * @param a4
	  * @return
	  */
	private static int tell_proverbs_random_number(String user_name, String a1,String a2, String a3, String a4) {
		int res = -1;
		int random = getRandomNumber(4);
		//convert string to integer value 
		while(true) {
			if (random == 0 && !a1.equals("1")) {      //check proverb displayed or not, if not, we can display now
				res = random;
				break;
			}
			else if (random == 1 && !a2.equals("1")) {
				res = random;
				break;
			}
			else  if (random == 2 && !a3.equals("1")) {
				res = random;
				break;
			}
			else if (random == 3 && !a4.equals("1")) {
				res = random;
				break;
			}else {
				random = getRandomNumber(4);
				continue;
			}
		}
		return res;
	}

	 /**
	  * helper function for debug to print arr
	  * @param arr
	  */
	 private static void show_arr(int[] arr){
		 for (int a : arr) {
			 System.out.println( "visited arr is:" + arr);
		 }
	 }
	 /**
	  * return a random number based on client cookie
	  * @param user_name
	  * @param a1
	  * @param a2
	  * @param a3
	  * @param a4
	  * @return
	  */
	 private static int tell_jokes_random_number(String user_name, String a1, String a2,String a3,String a4) {
			int random = getRandomNumber(4); 
		
			/*System.out.println(a1.equals("1"));
			System.out.println(a2.equals("1"));
			System.out.println(a3.equals("1"));
			System.out.println(a4.equals("1"));
			*/
			while(true) {
				if (random == 0 && !a1.equals("1")) {
					return random;
				
				}
				else if (random == 1 && !a2.equals("1")) {
					return random;
				
				}
				else if (random == 2 && !a3.equals("1")) {
					return random;
				}
				else if (random == 3 && !a4.equals("1")) {
					return random;
				}
				else {
					random = getRandomNumber(4);
					continue;
				}
			}
	}
	 
	 
	 
	 /**
	  * helper function to check all the index is 1 or not
	  * @param arr
	  * @return
	  */
	private static boolean all_visited(int[] arr) {
		for (int i = 0; i <4;i++) {
			if (arr[i] == 0) return false;
		}
		return true;
	}

	/**
	 * return a random number in range [0,max]
	 * @param min
	 * @param max
	 * @return
	 */
	private static int getRandomNumber(int max) {
		Random r = new Random();
		return r.nextInt(max);
	}			
}

/**
 * this class created by mode_worker_listener and this class
 * can create thread to change the mode of the joke_server if 
 * admin_client connect the this port
 * @author liang
 *
 */
class Mode_worker extends Thread{
	Socket socket; 
	//assign each worker a socket object and get its stream
	Mode_worker(Socket s){
		socket = s;
	}
	
	/**
	 * implements the task for each thread
	 */
	public void run() {
		System.out.println("Mode_worker starts");
		try {
			//allows read from client
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//allows write to client
			PrintStream out = new PrintStream(socket.getOutputStream());
			
			try {
				String command;
				command = in.readLine();      	//read input_stream from client  
				System.out.println("user just typed command: " +command);

				talk_back_to_admin_client(command,out);
			}
			catch (IOException x) {
			}
			socket.close();
		} catch (IOException e) {
			
		}
	}


	/**
	 * main functions that tell users some jokes, if admin change mode, need reactions here as well
	 * @param name
	 * @param out
	 */
	static void talk_back_to_admin_client(String command, PrintStream out) {
			//check admin_Client input, if is P or J, change the current mode
			if (command.equals("p") || command.equals("P")) {
				JokeServer.current_mode = 1;
				System.out.println("admin want Proverb mode and now current state is :" + JokeServer.current_mode);
				out.println("you just typed :" + command+ "and now is proverb mode ");
			}
			else if (command.equals("j") || command.equals("J")) {
				JokeServer.current_mode = 0;
				System.out.println("admin want Joke mode and now current state is :" + JokeServer.current_mode);
				out.println("you just typed :" + command+ "and now is joke mode ");
			}
	}


}
/**
 * this class use to waiting for admin_client to connect and create new thread to change
 * mode of joke_server if admin_client want
 * @author liang
 *
 */
class Mode_worker_listener implements Runnable{

	@Override
	public void run() {
		System.out.println("mode_worker_listener starts");
		Socket socket_mode_linstener;
		int q_len = 6;
		int port = 10008;
		
		try {
			ServerSocket mode_server_socket = new ServerSocket(port,q_len);
			
			while(true) {
				socket_mode_linstener = mode_server_socket.accept();
				new Mode_worker(socket_mode_linstener).start();
				
			}
			
		} catch (IOException e) {
			
		}
	}

	
}
class server_state{
	String JokeOrProverb;
	boolean flag;
	
}

public  class JokeServer {
	
	public static  int current_mode = 0;        //default as 0 = Joke mode, 1 = proverb mode
	public static void main(String[] ags) throws IOException {
		
		//create thread to listen and change mode
		Mode_worker_listener mwl = new Mode_worker_listener();
		Thread thread_mode = new Thread(mwl);
		thread_mode.start();
		
		
		int q_len = 6;
		// initialize the port number that server and client can communicate with each
		// other
		int port = 10007; 
		
		// create connection by socket object and allow us to 
		//get socket object and its stream from client side
		Socket socket;
		
		//create server socket connection listen to port 10007,
		ServerSocket server_socket = new ServerSocket(port,q_len);
		System.out.println("hellow,do you want build a snow man?");
		
		//now, we looping forever to listen client request and create thread 
		//to do the task and give response back to client
		while(true) {
			//get client socket
			socket =  server_socket.accept();
			new Worker(socket).start();
		}
	}
}