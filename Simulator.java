import java.io.*;
import java.util.LinkedList;

public class Simulator{

    static Router dispatcher; //level 1 router
    static LinkedList<Router> routers = new LinkedList<Router>(); //level 2 routers
    static int totalServiceTime; //sum of total time of each packt in the network
    static int totalPacketsArrived;
    static int packetsDropped; //only incremented when sendPacketTo() throws an exception
    static double arrivalProb;
    static int numIntRouters;
    static int maxBufferSize; //max # of Packets a Router can accomodate
    static int minPacketSize;
    static int maxPacketSize;
    static int bandwidth; //max # of Packets the Dest router can accept per sim unit
    static int duration; //number of simulation units
    static int timeElapsed; 

    static int promptHelper(String prompt) throws IOException{
	//Open an input stream for reading from the keyboard
	InputStreamReader inStream = new InputStreamReader(System.in);
	BufferedReader stdin = new BufferedReader(inStream);

	String innerPrompt, input;
	int intInput = 0;
	boolean running = true;
	while (running){
	    try{
		System.out.print(prompt);
		input = stdin.readLine();
		intInput = Integer.parseInt(input);
		if (intInput <= 0)
		    intInput = 1/0; //force catch block
		running = false;
	    }
	    catch (Exception e){
		innerPrompt = "\nInvalid input detected.\nPlease make sure you are entering";
		innerPrompt += " an Integer that is greater than 0";
		System.out.println(innerPrompt);
	    }
	}
	return intInput;
    }

    public static double simulate(){
	int routerIndex, packetSize;
	int innerBandwidth = bandwidth;
	boolean ifPacketsCreated = false;
	Packet newPacket;
	dispatcher = new Router();
	
	System.out.println("\nTime: " + timeElapsed);

	//Creation of Packets sent to dispatcher
	while (innerBandwidth > 0){
	    if (Math.random() < arrivalProb){
		ifPacketsCreated = true;
		packetSize = randInt(minPacketSize, maxPacketSize);
		newPacket = new Packet(packetSize, timeElapsed);
		System.out.print("Packet " + newPacket.getId() + " arrives at dispatcher ");
		System.out.print("with size " + newPacket.getPacketSize() + ".\n");
		dispatcher.enqueue(newPacket);
	    }
	    innerBandwidth--;
	}	
	if (!ifPacketsCreated)
	    System.out.print("No packets arrived.\n");

	//Sending Packets to Router
	routerIndex = Router.sendPacketTo(routers);
	
	return 1.0;
    }

    //helper method to generate a random number between minVal and maxVal, inclusively
    private static int randInt(int minVal, int maxVal){
	return (int)((Math.random()*((maxVal+1)-minVal))+minVal);
    }   
    
    public static void main(String[] args) throws IOException{
	//Open an input stream for reading from the keyboard
	InputStreamReader inStream = new InputStreamReader(System.in);
	BufferedReader stdin = new BufferedReader(inStream);

	boolean runSimulation = true;
	
	while (runSimulation){
	    //Prompts
	    String innerPrompt, input;
	    timeElapsed = 1;
	    int intInput = 0;
	    double doubleInput = 0.0;
	    boolean running = true;

	    //BEGIN USER INPUT--------------------------------------------------------------------------
	    System.out.print("\nStarting simulator...\n");

	    numIntRouters = promptHelper("\nEnter the number of Intermediate routers: ");

	    //special promptHelper for arrivalProb
	    while (running){
		try{
		    System.out.print("\nEnter the arrival probability of a packet: ");
		    input = stdin.readLine();
		    doubleInput = Double.parseDouble(input);
		    if (!(doubleInput > 0.0 && doubleInput <= 1.0))
			doubleInput = 1/0; //force catch block
		    running = false;
		}
		catch (Exception e){
		    innerPrompt = "\nInvalid input detected.\nPlease make sure you are entering";
		    innerPrompt += " a Double that is greater than 0.0 and less than or equal to 1.0";
		    System.out.println(innerPrompt);
		}
	    }
	    arrivalProb = doubleInput;

	    maxBufferSize = promptHelper("\nEnter the maximum buffer size of a router: ");
	    minPacketSize = promptHelper("\nEnter the minimum size of a packet: ");

	    //special promptHelper for maxPacketSize
	    running = true;
	    while (running){
		try{
		    System.out.print("\nEnter the maximum size of a packet: ");
		    input = stdin.readLine();
		    intInput = Integer.parseInt(input);
		    if (intInput <= minPacketSize)
			intInput = 1/0; //force catch block
		    running = false;
		}
		catch (Exception e){
		    innerPrompt = "\nInvalid input detected.\nPlease make sure you are entering";
		    innerPrompt += " an Integer that is greater than the minimum size of a packet";
		    System.out.println(innerPrompt);
		}
	    }
	    maxPacketSize = intInput;
	
	    bandwidth = promptHelper("\nEnter the bandwidth size: ");
	    duration = promptHelper("\nEnter the simulation duration: ");
	    //END USER INPUT--------------------------------------------------------------------------

	    //CREATE ROUTERS--------------------------------------------------------------------------
	    int innerNumIntRouters = numIntRouters;
	    Router newRouter;
	    while (innerNumIntRouters > 0){
		if (innerNumIntRouters == numIntRouters)
		    newRouter = new Router(maxBufferSize);
		else
		    newRouter = new Router();
		routers.addLast(newRouter);
		innerNumIntRouters--;
	    }
	    System.out.println(routers);
		
	    
	    while (timeElapsed <= duration){
		simulate();
		timeElapsed++;
	    }

	    running = true;
	    while (running){
		try{	    
		    System.out.print("\nDo you want to try another simulation? (y/n): ");
		    input = stdin.readLine();
		    if (input.equals("y"))
			running = false;
		    else if (input.equals("n")){
			running = false;
			runSimulation = false;
		    }
		    else
			intInput = 1/0; //force catch block
		}
		catch (Exception e){
		    innerPrompt = "\nInvalid input detected.\nPlease make sure you are entering";
		    innerPrompt += " a character 'y' or 'n'";
		    System.out.println(innerPrompt);
		}
	    }	    
	}
	System.out.println("\nProgram terminating successfully...\n");
    }
}
