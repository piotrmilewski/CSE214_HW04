/**
 * The <code>Simulator</code> class contains the main method that will be used to test the simulation. This
 * class utilizes the <code>Packet</code> and <code>Router</code> classes to send the respective packets
 * to their various destinations represented by the routers.
 *
 *
 * @author Piotr Milewski
 *    email: piotr.milewski@stonybrook.edu
 *    Stony Brook ID: 112291666
 **/

import java.io.*;
import java.util.LinkedList;

public class Simulator{

    static final int numOfDispatcherPackets = 5;
    static Router dispatcher; //level 1 router
    static LinkedList<Router> routers = new LinkedList<Router>(); //level 2 routers
    static int totalServiceTime = 0; //sum of total time of each packt in the network
    static int totalPacketsArrived = 0;
    static int packetsDropped = 0; //only incremented when sendPacketTo() throws an exception
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
	int innerDispatcherPackets = numOfDispatcherPackets;
	boolean ifPacketsCreated = false;
	Packet newPacket, sentPacket, processPacket;
	dispatcher = new Router();
	
	System.out.println("\nTime: " + timeElapsed);

	//Creation of Packets sent to dispatcher
	while (innerDispatcherPackets > 0){
	    if (Math.random() < arrivalProb){
		ifPacketsCreated = true;
		packetSize = randInt(minPacketSize, maxPacketSize);
		newPacket = new Packet(packetSize, timeElapsed);
		System.out.print("Packet " + newPacket.getId() + " arrives at dispatcher ");
		System.out.print("with size " + newPacket.getPacketSize() + ".\n");
		dispatcher.enqueue(newPacket);
	    }
	    innerDispatcherPackets--;
	}	
	if (!ifPacketsCreated)
	    System.out.print("No packets arrived.\n");
	
	//Sending Packets to Router
	while (!(dispatcher.isEmpty())){
	    routerIndex = Router.sendPacketTo(routers);
	    sentPacket = dispatcher.dequeue();
	    if (routerIndex == -1){
		packetsDropped++;
		System.out.println("Network is congested. Packet " + sentPacket.getId() + " is dropped.");
	    }
	    else{
		routers.get(routerIndex-1).enqueue(sentPacket);
		System.out.println("Packet " + sentPacket.getId() + " sent to Router " + routerIndex + ".");
	    }
	}
	
	//Process Router packets
	int tempBandwidth = bandwidth;
	for (int count = 0; count < numIntRouters; count++){
	    if (!routers.get(count).isEmpty()){
		processPacket = routers.get(count).peek();
		if (!(processPacket.getTimeArrive() == timeElapsed)){
		    processPacket.setTimeToDest(processPacket.getTimeToDest()-1);
		}
	    }
	}

	int getRidOff = 0;
	while (tempBandwidth > 0 && getRidOff != -1){
	    int fairness = 1;
	    getRidOff = -1;
	    for (int count = 0; count < numIntRouters; count++){
		if (!routers.get(count).isEmpty()){
		    processPacket = routers.get(count).peek();
		    int TimeToDest = processPacket.getTimeToDest();
		    if (TimeToDest < fairness){
			getRidOff = count;
			fairness = TimeToDest;
		    }
		}
	    }
	    if (getRidOff != -1){
		tempBandwidth--;
		processPacket = routers.get(getRidOff).dequeue();
		totalPacketsArrived++;
		totalServiceTime += (timeElapsed - processPacket.getTimeArrive());
		System.out.print("Packet " + processPacket.getId() + " has successfully reached ");
		System.out.println("its destination: +" + (timeElapsed - processPacket.getTimeArrive()));
	    }
	}
	
	//Display all routers
	for (int count = 0; count < numIntRouters; count++){
	    System.out.println("R" + (count+1) + ": " + routers.get(count));
	}
	
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
		
	    
	    while (timeElapsed <= duration){
		simulate();
		timeElapsed++;
	    }

	    System.out.println("\nSimulation ending...");
	    System.out.println("Total service time: " + totalServiceTime);
	    System.out.println("Total packets served: " + totalPacketsArrived);
	    String strDouble;
	    if (totalPacketsArrived > 0)
		strDouble = String.format("%.2f", ((double)totalServiceTime)/((double)totalPacketsArrived));
	    else
		strDouble = String.format("%.2f", 0.0);
	    System.out.println("Average service time per packet: " + strDouble);
	    System.out.println("Total packets dropped: " + packetsDropped);
	    
	    running = true;
	    while (running){
		try{	    
		    System.out.print("\nDo you want to try another simulation? (y/n): ");
		    input = stdin.readLine();
		    if (input.equals("y")){
			Packet.setPacketCount(0);
			routers = new LinkedList<Router>(); //level 2 routers
			totalServiceTime = 0;
			totalPacketsArrived = 0;
			packetsDropped = 0;
			running = false;
		    }
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
