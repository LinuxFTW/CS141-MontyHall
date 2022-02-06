// Jonah Nichols
// CS141
// Project 2: The Monty Hall Problem

// Fair warning: this got more convoluted than I wanted it to be
package montyHall;

import java.util.Scanner;
import java.util.Random;

public class Monty_Main {
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Random rand = new Random();
		
		int[] userData = new int[4];

		collectUserData(scan, userData);
		
		// This should honestly be its own function so
		// I can just call that rather than have the gobbledy gook
		// hogging up main but I'm starting to get tired and it's
		// 2: 35AM and I don't want to refactor *again*. Basically
		// this does the automatic runs by checking what type of 
		// automatic run the user selected and performing said run.
		// Then, print the results and call it a day.
		if(userData[0] == 1) {
			int numRuns = userData[1];

			if(userData[2] == 0 || userData[2] == 1) {
				int wins = automaticRuns(rand, numRuns, userData[2] == 0);
				System.out.println("You won " + wins + " times! Good job!");

			} else {
				
				// If the number of runs is odd, change it and notify the user.
				if(numRuns % 2 != 0) {
					numRuns = numRuns - 1;
					System.out.println("Automatically rounded down to " + (numRuns - 1));
				}
				
				// Run the simulation for changing and keeping doors, and
				// print out the results of the run.
				int winsChange = automaticRuns(rand, numRuns/2, true);
				int winsStay = automaticRuns(rand, numRuns/2, false);
				System.out.println("Changing doors won " + winsChange + " times and");
				System.out.println("Keeping the prize door won " + winsStay + " times. Good job!");
				System.out.println("This means your win ratio for changind doors is " + ((float)winsChange / (numRuns/2)));
				System.out.println("This means that your win ratio for keeping the door is " + ((float)winsStay/(numRuns/2)));
			}
		} else {
			// For a manual run, perform a manual run.
			manualRun(rand, scan, userData);
		}
		// Close the scanner.
		scan.close();
	}
	
	// This function collects all of the user data and inputs it into the int[] userData array
	public static int collectUserData(Scanner scan, int[] userData) {
		// Print out the bare-bones basics of what it wants you to do and the first prompt.
		System.out.println("Welcome to Let's Make a Deal! Before going into the game, ");
		System.out.println("we have to ask a few questions to start with. \n");
		
		System.out.println("First, would you like it to run automatically or manually?");
		System.out.println("a for automatic, m for manually (manually is the default)");
		String tmpVar = scan.nextLine();
		
		// Depending on what the user wants, show different prompts for what they prefer.
		// Automatic, a, give back the number of times the user would like it to run and
		// what they'd prefer to simulate. Automatic will give the ratio of win:total for
		// both.
		if(tmpVar.equals("a")) {
			userData[0] = 1;
			System.out.println("Since you would like to run it automatically, how many times would you like it to run?");
			userData[1] = scan.nextInt();
			System.out.println("Since you would like to run it " + userData[1] + " times, would you like to simulate changing doors (0),");
			System.out.println("keeping doors the same (1), or half and half (2)?");
			userData[2] = scan.nextInt();
		} else if(tmpVar.equals("m")){
			// Otherwise, collect the user's preferred door, and then enter into userData.
			userData[0] = 0;
			System.out.println("Since you would like to run it manually, which door would you");
			System.out.println("like: 1, 2, or 3?");
			userData[3] = scan.nextInt();
		} else {
			System.out.println("Uh oh, something's wrong!");
		}
		
		// return 0
		return(0);
	}
	
	// This function sets which doors can be shown to the user
	// and shows available doors.
	public static void setDoors(int prizeDoor, int userDoor, Boolean[] availDoors) {
		for(int i = 0; i<availDoors.length; i++) {
			availDoors[i] = !(i == prizeDoor-1 || i == userDoor-1);
		}

	}
	
	// This function determines which door to show, by iterating through the availDoors
	// array to check if they can be shown, having the effect of giving the largest
	// door that can be shown. This does create a strategy of selecting door 3
	// and then if you get a one you know that the prize is behind door 2.
	public static int showDoor(Boolean[] availDoors) {
		int returned = 0;
		
		for(int i = 0; i<availDoors.length; i++) {
			if(availDoors[i]) {
				returned = i;
			}
		}
		
		availDoors[returned] = false;
		return(returned);
	}
	
	// This function determines if the user would like to change doors, and then returns
	// wither they would like to switch or not.
	public static Boolean changeDoor(Scanner scan, String changeDoor, Boolean automaticMode) {
		if(!automaticMode) {
			System.out.println("Would you like to switch doors? ");
			System.out.println("Note: if it is not a lowercase y, the default option is no!");
			changeDoor = scan.nextLine();
		}
		return(changeDoor.equals("y"));
	}
	
	// This function generates random numbers, hence the name randomNum.
	// It does this using the modulus operator.
	public static int randomNum(Random rand, int lowNum, int highNum) {
			int r = rand.nextInt();
			r = Math.abs(r);
			r = (r % (highNum - lowNum +1)) + lowNum;
			return(r);
	}
	
	// This function is purely for the simulation function, which does
	// the door change in a better way (honestly I think this works better
	// overall but time constraints are really hitting right now)
	public static int automaticDoorChange(int userDoor, int shownDoor) {
		int returned = 0;
		for(int i=1; i<=3; i++) {
			// As long as the index isn't the user and shown door, return
			// that value. There should only be one.
			if(i!=userDoor & i!=shownDoor) {
				returned = i;
			}
		}
		return(returned);
	}
	
	// This function does automatic runs for the user rather than the user doing it.
	public static int automaticRuns(Random rand, int numRuns, Boolean change) {
		int wins = 0;
		for(int i=0; i<numRuns; i++) {
			// Initialize needed variables for each loop. Note that userDoor is static
			// because it doesn't matter what the user's door is.
			Boolean[] availDoors = new Boolean[3];
			int prizeDoor = randomNum(rand, 1, 3);
			int userDoor = 2;
			
			// Set the availDoors boolean and determine which door was shown to the
			// fake user.k
			setDoors(prizeDoor, userDoor, availDoors);
			int shownDoor = showDoor(availDoors) + 1;
			
			// If the user changes doors, then set the user door accordingly.
			if(change) {
				userDoor = automaticDoorChange(userDoor, shownDoor);
			}
			
			// Add a win if the user won
			if(userDoor == prizeDoor) {
				wins += 1;
			}
		}
		
		// Return the number of wins
		return(wins);

	}
	
	// THis function does manual runs where the user can interact with the game.
	public static void manualRun(Random rand, Scanner scan, int[] userData) {
		// More variable initialization.
		Boolean[] availDoors = new Boolean[3];
		int prizeDoor = randomNum(rand, 1, 3);

		// Set the availDoors boolean array
		setDoors(prizeDoor, userData[3], availDoors);
		
		// Show one door and ask the user if they would like to switch.
		System.out.println("Behind door number " + (showDoor(availDoors)+1) + " is a singular stale chip.");
		// Clear buffer because for some odd reason that's needed but not for automatic mode
		scan.nextLine();
		if(changeDoor(scan, "n", userData[0] == 1)) {
			// Switch doors.
			userData[3] = showDoor(availDoors) + 1;
		}
		
		// Print out two different versions based on whether the user won or not.
		if(userData[3] == prizeDoor) {
			System.out.println("You won a BRAND NEW CAAAR!! IT INCLUDES ONLY THE FINEST OF COMPONENTS AVAILABLE");
			System.out.println("TO THE MARKET AND I'M NOT JUST SAYING THIS BECAUSE I GET PAID TO!");
		} else {
			System.out.println("You won a SINGULAR STALE CHIP!! Now please leave before I call security.");
		}
	}
}
