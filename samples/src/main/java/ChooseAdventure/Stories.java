
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
 
    	scenarios.add(new Scenario("If you would like to follow the coordinates, simply say “Follow”\n" , "If you wish to follow your gut and go another way, say “My Way”\n" ,  "outcome"));
    	
    	scenarios.add(new Scenario("option 1", "option 2",  "outcome"));
    	
    	scenarios.add(new Scenario("option 1", "option 2",  "outcome"));
    	
    	scenarios.add(new Scenario("option 1", "option 2",  "outcome"));

    	
    	Adventures a = new Adventures("Ajaz's Adventure" , scenarios );
    	
    	ADVENTURES_COLLECTION.add(a);
    }	
     
	
}



