package montyHall;

import java.util.Scanner;

public class Monty_Main {
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int[] userData = new int[3];
		Boolean[] availDoors = new Boolean[3];
		
		
		setDoors(0, 0, availDoors);
		System.out.println("Behind door number " + (showDoor(prizeDoor, userDoor, availDoors)+1) + " is a singular stale chip.");
		changeDoor(scan, "n", false);
	}
	
	public static int collectUserData(Scanner scan, int[] userData) {
		System.out.println("Welcome to Let's Make a Deal! Before going into the game, ");
		System.out.println("we have to ask a few questions to start with. \n");
		
		System.out.println("First, would you like it to run automatically or manually?");
		System.out.println("a for automatic, m for manually (manually is the default)");
		
		String tmpVar = scan.nextLine();
		if(tmpVar == "a") {
			userData[0] = 1;
			System.out.println("Since you would like to run it automatically, how many times would you like it to run?");
			userData[1] = scan.nextInt();
			System.out.println("Since you would like to run it " + userData[1] + " times, would you like to simulate changing doors,");
			System.out.println("keeping doors the same, or half and half (only available with even numbers");
		} else {
			userData[0] = 0;
			System.out.println("Since you would like to run it manually, what are your doors?");
		}
		
		return(0);
	}
	
	public static void setDoors(int prizeDoor, int userDoor, Boolean[] availDoors) {
		for(int i = 0; i<availDoors.length; i++) {
			availDoors[i] = !(i == prizeDoor || i == userDoor);
			System.out.println(availDoors[i]);
		}
	}
	
	public static int showDoor(int prizeDoor, int userDoor, Boolean[] availDoors) {
		int returned = 0;
		
		for(int i = 0; i<availDoors.length; i++) {
			if(availDoors[i]) {
				returned = i;
			}
		}

		return(returned);
	}
	
	public static Boolean changeDoor(Scanner scan, String changeDoor, Boolean automaticMode) {
		if(!automaticMode) {
			System.out.println("Would you like to switch doors, or keep your own? (y/n)");
			System.out.println("Note: if it is not a lowercase y, the default option is no!");
			scan.nextLine();
		}
		return(changeDoor == "y");
	}
}
