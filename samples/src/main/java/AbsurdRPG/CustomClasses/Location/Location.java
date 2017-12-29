package AbsurdRPG.CustomClasses.Location;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import AbsurdRPG.CustomClasses.NPCs.NPC;

public class Location {
	
	private static final Logger logL = LoggerFactory.getLogger(Location.class);
	
	private String Name;
	
	private int Location_X;
	
	private int Location_Y;
	
	private ArrayList<NPC> population;
	
	public Location(String name, int X, int Y) {	
		
		Name = name;
		
		Location_X = X;
		
		Location_Y = Y;
	}
	
	public Vector<Integer> getLocationInVector(){
		
		Vector<Integer> v = new Vector<Integer>();
		
		v.add(Location_X);
		
		v.add(Location_Y);
		
		return v;
	}
	
	public void populate(ArrayList<NPC> NPCs) {
		population = NPCs;
	}
	
	public String name() {
		return Name;
	}
	
	public ArrayList<NPC> returnPopulation(){
		if(population != null) {
			return population;
		}else {
			return null;
		}
	}
}
