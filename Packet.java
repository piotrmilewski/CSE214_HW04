import java.io.*;

public class Packet{

    static int packetCount = 0;
    int id;
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

    public int getPacketCount(){
	return packetCount;
    }

    public int setPacketCount(int newPacketCount){
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
	output += id + ", ";
	output += timeArrive + ", ";
	output += timeToDest + "]";
	return output;
    }

}
