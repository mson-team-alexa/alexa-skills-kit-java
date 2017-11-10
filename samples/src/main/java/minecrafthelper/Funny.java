package minecrafthelper;

import java.util.HashMap;
import java.util.Map;

public class Funny {

	 private static final Map<String, String> funny = new HashMap<String, String>();

	    private Funny() {
	    }

	    static {
	    	funny.put("Camden",
	        		"Camden is not for the boys, and is referred to as the ninja"
	        		+ " in plain sight.");
	    	
	    }
	    public static String get(String funny) {
	        return Funny.get(funny);
	    }
	}

