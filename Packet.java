/**
 * The <code>Packet</code> class represents a packet that will be sent through the network.
 *
 *
 * @author Piotr Milewski
 *    email: piotr.milewski@stonybrook.edu
 *    Stony Brook ID: 112291666
 **/

import java.io.*;

public class Packet{

    static int packetCount = 0;
    int id = 0;
    int packetSize;
    int timeArrive;
    int timeToDest;

    public Packet(){
	packetCount++;
	id = packetCount;
	packetSize = 0;
	timeArrive = 0;
	timeToDest = 0;
    }

    public Packet(int newPacketSize, int newTimeArrive){
	packetCount++;
	id = packetCount;
	packetSize = newPacketSize;
	timeArrive = newTimeArrive;
	timeToDest = packetSize/100;
    }

    public static int getPacketCount(){
	return packetCount;
    }

    public static int setPacketCount(int newPacketCount){
	int oldPacketCount = packetCount;
	packetCount = newPacketCount;
	return oldPacketCount;
    }

    public int getId(){
	return id;
    }

    public int setId(int newId){
	int oldId = id;
	id = newId;
	return oldId;
    }

    public int getPacketSize(){
	return packetSize;
    }

    public int setPacketSize(int newPacketSize){
	int oldPacketSize = packetSize;
	packetSize = newPacketSize;
	return oldPacketSize;
    }

    public int getTimeArrive(){
	return timeArrive;
    }

    public int setTimeArrive(int newTimeArrive){
	int oldTimeArrive = timeArrive;
	timeArrive = newTimeArrive;
	return oldTimeArrive;
    }

    public int getTimeToDest(){
	return timeToDest;
    }

    public int setTimeToDest(int newTimeToDest){
	int oldTimeToDest = timeToDest;
	timeToDest = newTimeToDest;
	return oldTimeToDest;
    }

    public String toString(){
	String output = "[";
	if (id == 0)
	    return output + "]";
	output += id + ", ";
	output += timeArrive + ", ";
	output += timeToDest + "]";
	return output;
    }

}
