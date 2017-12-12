
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
 
    	
    	scenarios.add(new Scenario("To follow the coordinates, say Follow" , "To follow your gut and go another way, say My Way" ,  "outcome"));
    	
    	scenarios.add(new Scenario("To scan the black hole for life, say Scan ", "To orbit the black hole and search for nearby planets, say Search", "outcome"));
   
    	scenarios.add(new Scenario("To translate the message, say Translate", "To fix the power, say Fix",  "outcome"));
    	
    	scenarios.add(new Scenario("To fix the engines, say Fix", "To figure out why the engines lost power, say Engines", "outcome"));
    	
    	scenarios.add(new Scenario("To scan the planet for life, say Scan", "To search for more planets, say Search",  "outcome"));
    	
    	scenarios.add(new Scenario("To fix the power and escape, say Fix", "To send a peace letter to the aliens, say Letter",  "outcome"));
    	
    	scenarios.add(new Scenario("To find the threat, say Find", "To leave in the escape pod, say Leave",  "outcome"));
    	

    	
    	Adventures a = new Adventures("Ajaz's Adventure" , scenarios );
    	
    	ADVENTURES_COLLECTION.add(a);
    }	
     
	
}



