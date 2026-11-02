package hotelApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*Author: Sachini
 *Date finished: January 19th
 *Tester Class
 *Program description: 
 *Hotel IoT system for staff to navigate room booking, inventory, and energy levels used per room
 */

public class HotelOperations {//FINISHED JANUARY 19th 

	public static void main(String[] args) throws InterruptedException {
		ArrayList <Room> rooms=new ArrayList<>();
		
		try {
			File file =new File("HotelData");//scan file
			Scanner scan=new Scanner (file);
			while (scan.hasNextLine()) {
				String line=scan.nextLine();
				String [] temp=line.split(",");
				int roomNum = Integer.parseInt(temp[0]);
                double energyU = Double.parseDouble(temp[1]);
				int invenAvail=Integer.parseInt(temp[2]);
				int numBeds=Integer.parseInt(temp[3]);

                rooms.add(new Room(roomNum, energyU, invenAvail, numBeds));//add room number, energy, inventory, and num of beds in one object

			}
		}

			catch (FileNotFoundException e) {
				System.out.println("File not found");
			}
		System.out.println("Welcome to Smart Hotel Operations Manager");
		ArrayList <Room> roomsE= new ArrayList<>(rooms);
		sortE(roomsE);//initially sort energy
		menu(rooms, roomsE);//call menu
		
	}

	private static void sortE(ArrayList<Room> roomsE) {//sort energy through bubble sort, most to least
		for (int i=0; i<roomsE.size()-1; i++) {
			for (int j=0; j<roomsE.size()-1-i; j++) {
				if (roomsE.get(j).getEnergyU()<roomsE.get(j+1).getEnergyU()) {
					Room temp=roomsE.get(j);
					roomsE.set(j,roomsE.get(j+1));
					roomsE.set((j+1),temp);
				}
			}
		}
		
	}

	private static void menu(ArrayList<Room> rooms, ArrayList<Room> roomsE) throws InterruptedException {
		int choice;
		do {
			System.out.println("Main menu: \n1. Display all"
					+ "\n2. Manage rooms"
					+ "\n3. Display inventory levels"
					+ "\n4. Display energy consumption"
					+ "\n5. Housekeeping"
					+ "\n6. Exit");
			choice=numInput(rooms,roomsE);
			if (choice>6) {
				System.out.println("Please input valid option");
			}
		}
		while (choice>6);
		
		switch (choice) {
		case 1: 
			displayAll(rooms, roomsE);
			menu(rooms, roomsE);
			break;
		case 2: 
			manageRooms(rooms, roomsE);
			break;
		case 3: 
			invenLevels(rooms, roomsE);
			break;
		case 4:
			energyConsum(rooms, roomsE);
			break;
		case 5: 
			housekeep(rooms, roomsE);
			break;
		case 6: 
			System.out.println("Thank you for using Smart Hotel Operations Manager");
			System.exit(0);
			break;
		}
		
		
	}

	private static void housekeep(ArrayList<Room> rooms, ArrayList<Room> roomsE) throws InterruptedException {
		boolean need=false;
		for (int i=0; i<rooms.size(); i++) {
			if (rooms.get(i).ifNeedClean()) {//if room at index needs to be cleaned
				need=true;//negate the message for if none needed
				System.out.println("Room "+rooms.get(i).getRoomNum()+" requires housekeeping");
			}
		}
		if (!need) {
			System.out.println("No rooms currently require housekeeping.");
		}
		int choice;
		do {
			System.out.println("1. Issue housekeeping \n2. Return to main menu");
			choice=numInput(rooms,roomsE);
			if (choice>2) {
				System.out.println("Invalid");
			}
		}
		while (choice>2);
		
		switch (choice) {
			case 1:
				cleanRooms(rooms,roomsE);
				break;
			case 2: 
				menu(rooms, roomsE);
				break;
		}
		
	}

	private static void cleanRooms(ArrayList<Room> rooms, ArrayList<Room> roomsE) throws InterruptedException {
		int room;
		Room num;
		do {
			System.out.println("Enter room to issue housekeeping: ");
			room=numInput(rooms,roomsE);//call search method to check that room exists
			num=findRoom(rooms,room);
			if (num==null) {
				System.out.println("Room does not exist");
			}
			
		}
		while (num==null);
		
		if (num.ifNeedClean()) {
			num.housekeep();
		}
		else {
			System.out.println("Room does not require cleaning");
		}
		int choice;
		do {
			System.out.println("1. Clean rooms \n2. Check housekeeping \n3. Main menu");
			choice=numInput(rooms,roomsE);
			if (choice>3) {
				System.out.println("Invalid");
			}
		}
		while (choice>3);
		
		switch (choice) {
			case 1:
				cleanRooms(rooms,roomsE);
				break;
			case 2: 
				housekeep(rooms, roomsE);
				break;
			case 3: 
				menu(rooms, roomsE);
				break;
		}
		
	}

	private static Room findRoom(ArrayList<Room> rooms, int room) {
		for (Room r: rooms) {
			if (r.getRoomNum()==room) {
				return r;
			}
		}
		return null;
		
	}

	private static void energyConsum(ArrayList<Room> rooms, ArrayList<Room> roomsE) throws InterruptedException {
		ArrayList <Room> abnormal=new ArrayList<>();
		System.out.println("Rooms sorted from highest to lowest energy consumption: ");
		for (int i=0; i<roomsE.size(); i++) {
			System.out.println("Room "+roomsE.get(i).getRoomNum()+"; Energy: "+roomsE.get(i).getEnergyU()+" kWh/day");
			if (roomsE.get(i).getEnergyU()>85) {
				abnormal.add(roomsE.get(i));
			}
		}
		if (!abnormal.isEmpty()) {
			System.out.println("**********Abnormal activity detected: *************");
			for (int i=0; i<abnormal.size(); i++) {
				System.out.println("Room "+abnormal.get(i).getRoomNum()+"; Energy: "+abnormal.get(i).getEnergyU()+" kWh/day");
			}
			
			int choice;
			do {
				System.out.println("1. Update energy usage \n2. Main menu");
				choice=numInput(rooms,roomsE);
				if (choice>2) {
					System.out.println("Invalid");
				}
			}
			while (choice>2);
 
			switch (choice) {
			case 1: 
				updateE(rooms,roomsE);
				break;
			case 2: 
				menu(rooms,roomsE);
				break;
			}
			
		}
		
	}

	private static void updateE(ArrayList<Room> rooms, ArrayList<Room> roomsE) throws InterruptedException {
		Scanner input = new Scanner(System.in);
		int room;
		Room num;
		do {
			System.out.println("Which room would you like to update?: ");
			room=numInput(rooms,roomsE);//call search method to check that room exists
			num=findRoom(rooms,room);
			if (num==null) {
				System.out.println("Room does not exist");
			}
			
		}
		while (num==null);
		double newE=0;
		if (num!=null) {
			try {
				do {
					System.out.println("Enter new energy value:");
					newE=input.nextDouble();
					if (newE>300) {
						System.out.println("Invalid input");
					}
				}
				while (newE>300);
			}
			catch (Exception ex) {
				System.out.println("Invalid input");
				updateE(rooms,roomsE);
			}
			num.setEnergyU(newE);
			roomsE.clear();//re-sort since value changed
			roomsE.addAll(rooms);
			sortE(roomsE);
			System.out.println("Successfully changed!");
		}
		
		menu(rooms,roomsE);
		
	}

	private static void invenLevels(ArrayList<Room> rooms, ArrayList<Room> roomsE) throws InterruptedException {//CONTINUE
		System.out.println("Displaying inventory levels...");
		ArrayList <Room> needFill=new ArrayList<>();
		for (int i=0; i<rooms.size(); i++) {
			System.out.println("Room "+rooms.get(i).getRoomNum()+"; Remaining: "+rooms.get(i).getInvenAvail());
			if (rooms.get(i).ifNeedRefill()) {
				needFill.add(rooms.get(i));
			}
		}
		
		if (!needFill.isEmpty()) {
			System.out.println("*************Refill need detected: ****************");
			for (int i=0; i<needFill.size(); i++) {
				System.out.println("Room "+needFill.get(i).getRoomNum()+"; Remaining: "+needFill.get(i).getInvenAvail());
			}
		}
			System.out.println("1. Update inventory \n2. Main menu");
			int choice=numInput(rooms,roomsE);
			switch (choice) {
			case 1: 
				updateInv(rooms,roomsE);
				break;
			case 2: 
				menu(rooms,roomsE);
				break;
			}
			
		}

		

	private static void updateInv(ArrayList<Room> rooms, ArrayList<Room> roomsE) throws InterruptedException {
		int room;
		Room found;
		do {
			System.out.println("Which room would you like to refill?: ");
			room=numInput(rooms,roomsE);//call search method to check that room exists
			found=findRoom(rooms,room);
			if (found==null) {
				System.out.println("Room does not exist");
			}
			
		}
		while (found==null);
		if (found!=null) {
			found.refill();
		}
		
		menu(rooms,roomsE);
		
	}

	private static void manageRooms(ArrayList<Room> rooms, ArrayList<Room> roomsE) throws InterruptedException {
		int choice;
		do {
			System.out.println("Would you like to check in (1) or check out? (2)");
			choice=numInput(rooms,roomsE);
			if (choice>2) {
				System.out.println("Invalid");
			}
		}
		while (choice>2);
		
		switch(choice) {
		case 1: 
			checkIn(rooms, roomsE);
			break;
		case 2: 
			checkOut(rooms, roomsE);
			break;
		}
		
	}

	private static void checkOut(ArrayList<Room> rooms, ArrayList<Room> roomsE) throws InterruptedException {
		boolean ifAvail=false;
		System.out.println("Currently checked out: ");
		for (Room r : rooms) {
			if (r.ifCheckedIn()) {
				ifAvail=true;
				System.out.println("Room "+r.getRoomNum()+"; Beds: "+r.getBedNum());
			}
		}
		if (!ifAvail) {
			System.out.println("No rooms are checked out currently.");
			menu(rooms,roomsE);
		}
		int room;
		Room num;
		do {
			System.out.println("Enter room to issue check out: ");
			room=numInput(rooms,roomsE);//call search method to check that room exists
			num=findRoom(rooms,room);
			if (num==null) {
				System.out.println("Room does not exist");
			}
			
		}
		while (num==null);
		num.checkOut();
		menu(rooms, roomsE);
	}

	private static void checkIn(ArrayList<Room> rooms, ArrayList<Room> roomsE) throws InterruptedException {
		int beds;
		do {
			System.out.println("How many beds are needed?");
			beds=numInput(rooms,roomsE);
			if (beds>5) {
				System.out.println("Maximum of 5 beds available per room.");
			}
		}
		while (beds>5);

		

		ArrayList <Room> enoughB=new ArrayList<>();
		System.out.println("Available rooms");
		for (Room r : rooms) {
			if (r.getBedNum()>=beds&&!r.ifNeedClean()&&!r.ifCheckedIn()) {
				enoughB.add(r);
			}
		}
		sortBeds(enoughB);

		for (Room a: enoughB) {
			System.out.println("Room "+a.getRoomNum()+"; Beds: "+a.getBedNum());
		}

		int room;
		Room num;
		do {
			System.out.println("Enter room to check in: ");
			room=numInput(rooms,roomsE);//call search method to check that room exists
			num=findRoom(rooms,room);
			if (num==null) {
				System.out.println("Room does not exist");
			}
			
		}
		while (num==null);
		
		num.checkIn(beds);
		
		menu(rooms, roomsE);
	
	}
	
	private static void sortBeds(ArrayList<Room> enoughB) {
		for (int i=0; i<enoughB.size()-1; i++) {
			for (int j=0; j<enoughB.size()-1-i; j++) {
				if (enoughB.get(j).getBedNum()>enoughB.get(j+1).getBedNum()) {
					Room temp=enoughB.get(j);
					enoughB.set(j,enoughB.get(j+1));
					enoughB.set((j+1),temp);
				}
			}
		}
		
	}

	private static int numInput(ArrayList<Room> rooms, ArrayList<Room> roomsE) throws InterruptedException {
		Scanner input = new Scanner(System.in);
		int answer=0;
		try {
			answer = input.nextInt();
		}
		catch (Exception ex) {
			System.out.println("\nError - Invalid input.");
			menu(rooms,roomsE);
		}
		
		if (answer<0) {
			System.out.println("Number invalid");
			menu(rooms,roomsE);
		}
		
		return answer;	
	}

	private static void displayAll(ArrayList<Room> rooms, ArrayList<Room> roomsE) {
		for (int i=0; i<rooms.size(); i++) {
			System.out.println(rooms.get(i).display());
		}
	}

}
