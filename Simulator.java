import java.io.*;
import java.util.LinkedList;

public class Simulator{

    Router dispatcher; //level 1 router
    LinkedList<Router> routers; //level 2 routers
    int totalServiceTime; //sum of total time of each packt in the network
    int totalPacketsArrived;
    int packetsDropped; //only incremented when sendPacketTo() throws an exception
    double arrivalProb;
    int numIntRouters;
    int maxBufferSize; //max # of Packets a Router can accomodate
    int minPacketSize;
    int maxPacketSize;
    int bandwidth; //max # of Packets the Dest router can accept per sim unit
    int duration; //number of simulation units

    public double simulate(){
	return 1.0;
    }

    //helper method to generate a random number between minVal and maxVal, inclusively
    private int randInt(int minVal, int maxVal){
	return (int)((Math.random()*((maxVal+1)-minVal))+minVal);
    }

    public static void main(String[] args){
	
    }

}
