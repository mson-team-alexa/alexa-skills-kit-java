
package ChooseAdventure;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storyteller.AesopFable;




public final class Stories {

	private static final Logger log = LoggerFactory.getLogger(Stories.class);

	private static int loki = 0;
	private static final ArrayList<Adventures> ADVENTURES_COLLECTION = new ArrayList<Adventures>();
	
    public Stories() {
    }

   
public static Adventures getName(String name) {
		
		for (Adventures adse : ADVENTURES_COLLECTION) {
			if (adse.getStoryName().equalsIgnoreCase(name)) {
				loki = ADVENTURES_COLLECTION.indexOf(adse);
				return adse;
			}
		}
		return null;
	}

    static {
    	ArrayList<Scenario> scenarios = new ArrayList<Scenario>();
 
    	
    	scenarios.add(new Scenario("Follow" , "My Way" ,  "outcome"));
    	
    	scenarios.add(new Scenario("Scan ", "Search", "outcome"));
   
    	scenarios.add(new Scenario("Translate", "Fix",  "outcome"));
    	
    	scenarios.add(new Scenario("Fix", "Engines", "outcome"));
    	
    	scenarios.add(new Scenario("Scan", "Search",  "outcome"));
    	
    	scenarios.add(new Scenario("Fix", "Letter",  "outcome"));
    	
    	scenarios.add(new Scenario("Find", "Leave",  "outcome"));
    	

    	
    	Adventures a = new Adventures("Ajaz's Adventure" , scenarios );
    	
    	ADVENTURES_COLLECTION.add(a);
    }	
     
	
}



