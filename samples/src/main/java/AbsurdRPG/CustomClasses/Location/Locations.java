package AbsurdRPG.CustomClasses.Location;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import AbsurdRPG.CustomClasses.NPCs.NPC;

public final class Locations {

	private static final Logger log = LoggerFactory.getLogger(Locations.class);

	public static final String CITY_OF_DAWNSTAR = "the City of Dawn Star";
	
	public static final String CITY_OF_MERLINSBURG = "Merlinsburg";
	
	public static final String CITY_OF_UNIFLORA = "Uniflora";
	
	//Locations
	public static Location DownhillVillage;
	
	public static int days_Needed;
	
	public static ArrayList<Location> locations;
	
	private static int days_Elapsed;
	
	public static int distanceBetween(Location A, Location B) {
		
		int days = Math.abs((A.getLocationInVector().get(0) - B.getLocationInVector().get(0))) + 
				   Math.abs((A.getLocationInVector().get(1) - B.getLocationInVector().get(1)));
		
		return days;
	}

	public static Route routeFromAToB(Location A, Location B) {

		Vector<Integer> v = new Vector<Integer>();
		
		v.add(A.getLocationInVector().get(0) - B.getLocationInVector().get(0));
		
		v.add(A.getLocationInVector().get(1) - B.getLocationInVector().get(1));
		
		Route r = new Route(v);
		
		days_Needed = distanceBetween(A, B);
		
		days_Elapsed = 0;
		
		return r;
	}
	
	public static void afterOneDay() {
		days_Elapsed += 1;
	}
	
	public static Integer returnDayElapsed() {
		return days_Elapsed;
	}
	
	public static void Populate() {	
		locations = new ArrayList<Location>();
		
		DownhillVillage = new Location("Downhill Village", 10, 10);
		
		DownhillVillage.populate(NPCForVillage(DownhillVillage));

		locations.add(DownhillVillage);
	}

	
	private static ArrayList<NPC> NPCForVillage(Location Village){
		ArrayList<NPC> NPCs = new ArrayList<NPC>();
		
		NPC VillageLead = new NPC(true, 0, Village);
		VillageLead.setIdentity("Head of the Village");
		NPC Villager = new NPC(true, 0, Village);
		Villager.setIdentity("Villagers");
		NPC Merchant = new NPC(true, 0, Village);
		Merchant.setIdentity("Merchant");
		
		NPCs.add(VillageLead);
		NPCs.add(Villager);
		NPCs.add(Merchant);
		
		return NPCs;
	}
}


