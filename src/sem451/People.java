package sem451;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class People implements FileNames, DataTasks{

	private Map<String, Person> people;
	public People() {
		people = new HashMap<>();
	}

	public int howManyPeople() {
		return people.size();
	}
	public void clearAllPeople() {
		people.clear();
	}
	public void printPeople() {
		for (Map.Entry<String, Person> e : people.entrySet()) 
		{
		      System.out.println(e.getValue());
		}
	}
	
	public boolean addPerson(Person p) {
		return addPerson(p.name(),p.getId(),p.getAge());
	}
	
	public boolean addPerson(String name, String id, int age) {
		if(people.containsKey(id)) 
		{
			System.out.println("Sorry, id is used! User cannot be created.");
			return false;
		}
		people.put(id, new Person(name,id,age));
		System.out.println(name+" has been added.");
		System.out.println("No. of users is: "+people.size());
		return true;
	}
	
	public boolean removePerson(String id) {
		Person p = people.remove(id);
		if(p==null) {
			System.out.println("Sorry, no such a user!");
			return false;
		}
		System.out.println(p.name()+" has been removed.");
		System.out.println("No. of users is: "+people.size());
		return true;
	}
	
	
	public Person getPersonById(String id) {
		Person p = people.get(id);
		if(p==null) {
			System.out.println("Sorry, no such a user!");
			return null;
		}
		return p;
	}
	
	public void loadDataFromFile() throws FileNotFoundException, IOException, Exception {
		  File peopleF = new File(PEOPLE_FILE);
		  if (!peopleF.exists()) {
		        System.out.println("File Not Found to load!");
		       // System.out.println("People");
		        return; // Check if the file exists
		    }	  
	    people.clear();
	    try (ObjectInputStream o2 = new ObjectInputStream(new FileInputStream(PEOPLE_FILE))) {
	        people = (Map<String, Person>) o2.readObject();
	    }
	    System.out.println("Finished Loading people data.");
	    System.out.println(people.size() + " user(s) imported.");
	}

	@Override
	public void saveDataToFile()  throws FileNotFoundException, IOException, Exception{
		if(people.isEmpty()) System.out.println("No users to save!");
		else 
		{
			ObjectOutputStream o;			
			o = new ObjectOutputStream(new FileOutputStream(PEOPLE_FILE));
			o.writeObject(people);
			o.close(); 
		}
	}
}
