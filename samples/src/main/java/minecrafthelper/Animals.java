package minecrafthelper;

import java.util.HashMap;
import java.util.Map;

public class Animals {
	private static final Map<String, String> animals = new HashMap<String, String>();
	
	private Animals() {
	}
	
	static {
		animals.put("bat", "Bats are a flying, passive mob that spawns in caves. They do not drop any items or experience when killed.");
		animals.put("bats", "Bats are a flying passive mob that spawns in caves. They do not drop any items or experience when killed.");
		animals.put("chicken", "Chickens are egg laying mobs that generate on grass blocks.");
		animals.put("chickens", "Chickens are egg laying mobs that generate on grass blocks.");
		animals.put("cow", "Cows generate in herds of 4 or more on top of opaque blocks.");
		animals.put("cows", "Cows generate in herds of 4 or more on top of opaque blocks.");
		animals.put("pig", "Pigs generate on grass blocks and drop 1 to 3 pieces of raw porkchop when killed.");
		animals.put("pigs", "Pigs generate on grass blocks and drop 1 to 3 pieces of raw porkchop when killed.");
		animals.put("rabbit", "Rabbits generate in in groups of two or three in deserts, taiga, mega taiga, cold taiga, and ice plains.");
		animals.put("rabbits", "Rabbits generate in in groups of two or three in deserts, taiga, mega taiga, cold taiga, and ice plains.");
		animals.put("sheep", "Sheep generate in flocks of 4 or more on grass blocks.");
		animals.put("squid", "Squid generate in water and drop 1 to 3 ink sacs when killed.");
	}
	
	public static String get(String item) {
	   return animals.get(item);
	}
}

 