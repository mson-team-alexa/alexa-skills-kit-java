package ChooseAdventure;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public final class Adventures  {

	private static final Logger log = LoggerFactory.getLogger(Stories.class);

	private static ArrayList<Scenario> SCENARIO_COLLECTION = new ArrayList<Scenario>();
	private String name;
	
	
    public Adventures(String name1, ArrayList<Scenario> scenarios) {
    	SCENARIO_COLLECTION = scenarios;
    	name = name1;	
    }
    public 	ArrayList<Scenario> getScenList()
    {
		return SCENARIO_COLLECTION;
    	
    }
    public String getStoryName()
    {
    		return "Space Story";
    }


	
}










