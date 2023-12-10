package sem451;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.io.*;
import java.text.SimpleDateFormat;

public class KkuSystem implements FileNames, ReserveTasks, OptionalReserveTasks{

	static List<ReserveBlock> reservations = new ArrayList<>();
	static People people = new People();
	
	
	public void showCLIMenu() {
		
		load(); //load previous data
		Person p; LocalDate l; int t; Room r; boolean st; Scanner sc = new Scanner(System.in); String s;
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd | HH:mm:ss").format(Calendar.getInstance().getTime());
		while(true) {
			System.out.println("\n\n\nWelcome to KKU LAB Managament System");
			System.out.println(timeStamp);
			System.out.println("=================================================");
			System.out.println("1. Make a reservation + New User");
			System.out.println("2. Update a reservation");
			System.out.println("3. Print all reservations on screen");
			System.out.println("4. Print reservations to File");
			System.out.println("5. Exit");
			System.out.println("6. Remove Reservation");
			System.out.println("7. Create New User");
			System.out.println("8. Print all users on screen");
			System.out.println("9. Remove User");
			System.out.print("\nPlease Enter a number:");
			s = sc.next();
			switch(s) {
			case "1":
		        System.out.println("Enter person name, ID, and age (press Enter after each):");
		        if (sc.hasNextLine()) {
		            sc.nextLine(); 
		        }
		        String name = sc.nextLine();
		        String ID = sc.nextLine();
		        int age = checkAge(sc);
		        p = new Person(name, ID, age);
		        
				people.addPerson(p);
				System.out.println("Enter room name:");
				r = new LabRoom(sc.next());
				System.out.println("Enter Date in yyyy-mm-dd:");
				while (true) {
				//    System.out.println("Enter Date in yyyy-mm-dd:");
				    String dateString = sc.next();

				    try {
				        l = LocalDate.parse(dateString);
				        break; // Exit the loop if the date is valid
				    } catch (DateTimeParseException e) {
				        System.out.println("Invalid date format. Please enter the date in yyyy-mm-dd format.");
				    }
				}

				System.out.println("At what Clock 1-24 (Only 1 hour can be booked)?");
				  t = checkHour(sc);
				st=this.reserveBlock(new ReserveBlock(p,l,t,r));
				break;
				
				
			case "2":
				System.out.println("Not working. Please remove and Add again.");
				break;
				
				
			case "3":
				this.printReservedBlocks(reservations);
				break;
				
				
			case "4":
				this.exportToFile2();
				break;
				
				
			case "5":
				sc.close();
				System.out.println("Saving...");
				save();
				System.out.println("Thank you.");
				System.exit(0);
				
			case "6":
				p = new Person("test","test",0);
				System.out.println("Enter room name:");
				r = new LabRoom(sc.next());
				System.out.println("Enter Date in yyyy-mm-dd:");
				while (true) {
				//    System.out.println("Enter Date in yyyy-mm-dd:");
				    String dateString = sc.next();

				    try {
				        l = LocalDate.parse(dateString);
				        break; // Exit the loop if the date is valid
				    } catch (DateTimeParseException e) {
				        System.out.println("Invalid date format. Please enter the date in yyyy-mm-dd format.");
				    }
				}

				System.out.println("At what Clock 1-24 (Only 1 hour can be booked)?");
				t = checkHour(sc);
				st=this.removeBlock(new ReserveBlock(null,l,t,r));
				break;
			
			case "7":
				  System.out.println("Enter person name, ID, and age (press Enter after each):");
			        if (sc.hasNextLine()) {
			            sc.nextLine(); 
			        }
			        String namePerson = sc.nextLine();
			        String IDPerson = sc.nextLine();
			        int agePerson = checkAge(sc);
			        
			        people.addPerson(namePerson, IDPerson, agePerson);
				break;
				
			case "8":
				people.printPeople();
				break;
				
			case "9":
				System.out.println("Enter user id to remove:");
				people.removePerson(sc.next());
				break;
			
			default:
				System.err.println("Wrong choice!\n");
				
			}
		}
		
	}
	public static void main(String args[]) {
		//reservations.add(new ReserveBlock(new Person("Ahmad","0",0),LocalDate.parse("2023-12-12"),12,new LabRoom("18S")));
		//String s = sc.next();
		//LocalDate l = LocalDate.parse(s);
		//LocalTime t = LocalTime.parse(s);
		//System.out.println(t);
		new KkuSystem().showCLIMenu();
	}
	
	//checkAge
	private static int checkAge(Scanner sc) {
	    final int MAX_AGE = 65; // Define a reasonable maximum age
	    while (true) {
	   //     System.out.println("Enter age (0 to 65): ");
	        String input = sc.nextLine();

	        try {
	            int age = Integer.parseInt(input);

	            if (age >= 25 && age <= MAX_AGE) {
	                return age;
	            } else {
	                System.out.println("Age must be between 25 and " + MAX_AGE + ":");
	            }
	        } catch (NumberFormatException e) {
	            System.out.println("Enter an integer for age:");
	        }
	    }
	}


    private static int checkHour(Scanner sc) {
        while (true) {
            if (sc.hasNextInt()) {
                int hour = sc.nextInt();
                if (hour >= 1 && hour <= 24) {
                    return hour;
                } else {
                    System.out.println("Hour must be between 1 and 24. Try again:");
                }
            } else {
                System.out.println("Invalid input. Enter a number between 1 and 24:");
                sc.next(); // Consume the invalid token
            }
        }
    }



	@Override
	/**
	  * Check person is not blocked
	  *	Check block's date, time, & room is not in the list
	  * then add it
	  **/
	public boolean reserveBlock(ReserveBlock rb) {
		if(this.checkExist(rb, reservations))
		{
			System.out.println("Sorry, booked in "+rb.getDate()+" at "+rb.getClock()+"!");
			return false;
		}
		else
		{
			reservations.add(rb);
			System.out.println("Done, Room "+rb.getRoom().getName()+" booked in "+rb.getDate()+" at "+rb.getClock()+".");
			return false;
		}
	}

	/**
	 * Search all list elements if there is any blocked rooms
	 * similar to the parameter return true.
	 * @param rb block you want to add
	 * @param rooms list of blocked rooms
	 * @return true if it finds a match in the list
	 */
	public boolean checkExist(ReserveBlock rb, List<ReserveBlock> rooms) {
		for(ReserveBlock room: rooms) {
			if(room.equals(rb))
				return true;
		}
		return false;
	}
	
	@Override
	public boolean updateBlock(ReserveBlock rb) {
		if(rb.getBy().isBlocked())
		{
			System.out.println("User cannot make a reservation!");
			return false;
		}
		else if(!this.checkExist(rb, reservations))
		{
			System.out.println("Sorry, no one has booked it in "+rb.getDate()+" at "+rb.getClock()+"!");
			System.out.println("Making a new reservation ...");
			return reserveBlock(rb);
		}
		else
		{
			removeBlock(rb);
			if(reserveBlock(rb))
			{
				System.out.println("Updated.");
				return true;
			}
			
			else 
			{
				System.out.println("Failed to update!");
				return false;
			}
		}
	}

	@Override
	public boolean removeBlock(ReserveBlock rb) {
		int in = -1;
		if(reservations.isEmpty()) {
			System.out.println("List is Empty!");
			return false;
		}
		for(int i =0; i<reservations.size();i++)
				{
					if(rb.getDate().equals(reservations.get(i).getDate()))
					{
						if(rb.getClock()==reservations.get(i).getClock()) 
						{
							if(rb.getRoom().getName().equalsIgnoreCase(reservations.get(i).getRoom().getName()))
							{
								in = i;
								break;
							}
						}
					}
				}
		if(in!=-1)
			{
				System.out.println("Removed "+reservations.get(in));
				reservations.remove(in);
				return true;
			}
		System.out.println("Sorry, could not find the block to remove!");
		return false;
	}

	@Override
	public void removeAllEndedBlocks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printReservedBlocks(List<ReserveBlock> blocks) {
		if(blocks.isEmpty())
			System.out.println("No reservations have been made!");
		else
			for(ReserveBlock block:blocks) {
				System.out.println(block);
			}
		
	}

	@Override

	  public boolean loadDataFromFile() {
      File sessionsF = new File(SESSIONS_FILE);

      // Check if the file exists
      if (!sessionsF.exists()) {
          System.out.println("File Not Found to load! ");
         // System.out.println("Sessions");

          return false;
      }
      try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(sessionsF))) {
          reservations.clear();
          reservations = (List<ReserveBlock>) inputStream.readObject();
          System.out.println("Finished loading data.");
          System.out.println(reservations.size() + " session(s) imported.");
          return true;
      } catch (IOException e) {
          System.out.println("Could not load from file!");
      } catch (Exception e) {
          System.out.println("Unknown error in load file!");
          e.printStackTrace(); 
      }

      return false;
  }


	@Override
	public boolean saveDataToFile() {
		if(KkuSystem.reservations.isEmpty())
		{
			System.out.println("Nothing to save!");
			return true;
		}
		else {
			ObjectOutputStream o;
			try {
				o = new ObjectOutputStream(new FileOutputStream(SESSIONS_FILE));
				o.writeObject(reservations);
				o.close();
				
				return true;
			} catch (FileNotFoundException e) {
				System.out.println("File Not Found to save!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	//export data to text file
	//it uses PrintWriter and always override!
	public boolean exportToFile2() {
		if(KkuSystem.reservations.isEmpty())
		{
			System.out.println("Nothing to export!");
			return true;
		}
		else {
			try {
				PrintWriter o = new PrintWriter(PRINT_FILE);
				String timeStamp = new SimpleDateFormat("yyyy-MM-dd | HH:mm:ss").format(Calendar.getInstance().getTime());
				o.println("\n================="+timeStamp+"==================");
				for(ReserveBlock bl: reservations)
				{
					o.println(bl);
				}
				o.close();
				System.out.println("Finished exporting.");
				return true;
			} catch (FileNotFoundException e) {
				System.out.println("FileNot Found to export!");
				e.printStackTrace();
			}
		}
		return false;
	}

	
	//this method save users and sessions into external file
	public void save() {
		try {
			this.saveDataToFile();
			people.saveDataToFile();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	//this method load users and sessions from external file to their collections
	public void load() {
		try {
			this.loadDataFromFile();
			people.loadDataFromFile();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    @Override
    public List<ReserveBlock> findReservedBlockAt(LocalDate date) {
        List<ReserveBlock> filteredReservations = new ArrayList<>();

        for (ReserveBlock reserveBlock : reservations) {
            if (reserveBlock.getDate().equals(date)) {
                filteredReservations.add(reserveBlock);
            }
        }

        return filteredReservations;
    }

}