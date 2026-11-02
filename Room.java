package hotelApp;

/*Author: Sachini
 *Date finished: January 19th
 *Constructor Class
 *Program description: 
 *Hotel IoT system for staff to navigate room booking, inventory, and energy levels used per room
 */

public class Room {//FINISHED JANUARY 19th
	private int roomNum;
	private boolean occup;
	private double energyU;
	private int invenAvail;
	private boolean needsClean;
	private int numBeds;
	
	public Room (int roomNum, double energyU, int invenAvail, int numBeds) {//constructor method
		this.roomNum=roomNum;
		this.energyU=energyU;
		this.invenAvail=invenAvail;
		this.numBeds=numBeds;
		
		occup=false;
		needsClean=false;
	}
	
	public double getEnergyU() {
		return energyU;
	}

	public void setEnergyU(double energyU) {
		this.energyU = energyU;
	}

	public int getInvenAvail() {
		return invenAvail;
	}

	public void setInvenAvail(int invenAvail) {
		this.invenAvail = invenAvail;
	}

	public int getRoomNum() {
		return roomNum;
	}

	public int getBedNum() {
		return numBeds;
	}

	
	public boolean ifNeedClean() {
		return needsClean;
	}
	
	
	public void checkOut() {//check out if has already been checked in
		if (occup==true) {
			System.out.println("Room "+roomNum+" has been successfully checked out.");
			needsClean=true;
			occup=false;
		}
		else {
			System.out.println("Room is currently not available to be checked out");
		}
	}
	
	public boolean ifCheckedIn(){
		return occup;
	}
	
	public void checkIn(int beds) {//check in if not occupied, is clean, and has enough beds
		if (occup) {
			System.out.println("Room has already been checked in");
		} 
		else if (needsClean) {
			System.out.println("Room needs to be cleaned");
		} 
		else if (beds > numBeds) {
			System.out.println("Room does not have enough beds.");
		} 
		else {
			System.out.println("Room has been successfully checked in.");
			occup= true;
		}
	}

	public boolean ifNeedRefill() {//threshold num: 3
		if (invenAvail<3) {
			return true;
		}
		else
			return false;
		
	}
	
	public void refill() {//refill by increments of 10
		if (invenAvail>10) {
			System.out.println("Room already has sufficient supply");
		}
		else {
			invenAvail+=10;
			System.out.println("Room has been successfully refilled.");
		}
		
		
	}
	
	
	public void housekeep() {//housekeep if needed
		if (needsClean) {
			System.out.println("Room "+roomNum+" has been issued for housekeeping");
			needsClean=false;
		}
		else {
			System.out.println("Room does not require cleaning");
		}
	}
	
	public String display(){//figure out occupied and needs clean
		return "Room "+roomNum+"; Energy Usage: "+energyU+"; "+"Beds: "+numBeds+"; Inventory: "+invenAvail;
	}
	
	
	
	
}
