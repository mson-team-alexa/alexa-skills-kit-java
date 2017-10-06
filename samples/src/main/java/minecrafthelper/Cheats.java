package minecrafthelper;
import java.util.HashMap;
import java.util.Map;

public final class Cheats {
	

	

	

	    private static final Map<String, String> cheats = new HashMap<String, String>();

	    private Cheats() {}
	    

	    static {
	        cheats.put("Help command", "The Help command provide more information on the given command" + "The command is as follows," + "/help [CommandName]\n");
	        cheats.put("Give command", "Used to give another player an item from your inventory. Example: /give PCGamesN minecraft:planks 13 1. This would give PCGamesN 13 Spruce Wood Planks. This command is much simpler when giving single objects, but is useful for very specific trades." + "The command is as follows," + "/give [Amount] [DataValue]\n");
	        cheats.put("Teleport command", "Used to instantly transport yourself or another player to a specific location in the world. Using another player’s name in place of the coordinates will transport the target directly to said player’s location." + 
	        		"The command is as follows," + "/tp [TargetPlayer] \n");
	        cheats.put("Kill command", "Kills your character, adding another player’s name will apply the command to them." + 
	        	  "The command is as follows," + "/kill \n");
	        cheats.put("Weather command", "Allows you to choose the weather or your world. Options include: , and ." + 
	        		 "The command is as follows," + "/weather \n");
	        cheats.put("Creative Mode command",  "Changes the gamemode to Creative mode, which allows player flight, unlimited resources and stops mobs attacking you." +
	        		 "The command is as follows," + "/gamemode creative \n");
	        cheats.put("Survival Mode command", "Changes the gamemode to Survival mode, which means mobs will attack you and you’ll have to gather all resources the old-fashioned way." + 
	        		  "The command is as follows," + "/gamemode survival \n");
	        
	        cheats.put("Set time command", "Sets the time to day. Replace “1000” with “0” for dawn, “6000” for midday, “12000” for dusk and “18000” for night." + 
	       "The command is as follows," + "/time set 1000 \n" );
	        
	        cheats.put("Change difficult to Peaceful command","Changes difficulty to Peaceful mode. Replace “peaceful” with “easy”, “ normal”, or “hard” for more of a challenge." + 
	        		"The command is as follows, " +" /difficulty peaceful \n");
	        
	        cheats.put("How to find you world's seed code command", "This will produce a code for your world, note it down so that you can load up an identical one in the future" + "The command is as follows," + "/seed \n");
	      
	        cheats.put("Keep inventory when you die command", "Ensures you don’t lose your items upon dying. To revert this, type “false” in place of “true”\n" + 
	        		 "The command is as follows," + "/gamerule keepInventory true \n");
	        
	        cheats.put("Stop time command", "This will stop the game’s day/light cycle in it’s place, allowing you to live under permanent sunshine or moonlight. To resume the day/light cycle type in “/gamerule doDaylightCycle true”." + "The command is as follows," + "/gamerule do DaylightCycle false \n");
	        
	        
	        cheats.put("Summon command", "Instantly drops a desired creature or object into your world, especially handy for when you’re short a couple of tame ocelots." + "The command is as follows," + "/summon \n");
	        
	        cheats.put("Cannon command", "Shoots a TNT block at where the player’s pointing." + 
	        		 "The command is as follows," + "/cannon \n");
	        
	        cheats.put("Atlantis mode command", "Dramatically raises the world’s water level, submerging all but the highest mountains." + 
	        	 "The command is as follows," + "/atlantis \n");
	        
	        cheats.put("Jump command", "Teleports the player to where they’re facing" + 
	        	 "The command is as follows," + "/jump \n");
	       
	        cheats.put("Mob damage command", "Mobs can’t deal any damage to you." + 
	        	 "The command is as follows," + "/mobdamage \n");
	        
	        cheats.put("Ride command", "Turns the creature you’re facing into a mount." + 
	        		"The command is as follows," + "/ride \n");
	        
	        cheats.put("Instant mine command", "One-click mining with any tool." + 
	        	 "The command is as follows," + "/instantmine \n");
	        
	        cheats.put("Freeze command", "Stops mobs in their tracks" + "The command is as follows," + "/freeze \n");
	        
	        cheats.put("Fall damage command", "Turns fall damage on and off" + "The command is as follows," + "/falldamage \n");
	        
	        cheats.put("Fire damage command", "Turns fire damage on and off" + "The command is as follows," + "/firedamage \n");
	        
	        cheats.put("Water damage command", "Turns water damage on and off" + "The command is as follows," + "/waterdamage \n");
	        
	        cheats.put("Smelt item command", "Turns all items into their smelted form" + "The command is as follows," + "/superheat \n");
	        
	        cheats.put("Instant plant command", "No more waiting around for a planted seed to grow." + 
	        	 "The command is as follows," + "/instantplant \n");
	        
	        cheats.put("Store items command", "Stores all inventory items into a chest, which spawns nearby." + 
	        	"The command is as follows," + "/dropstore\n");
	        
	        
	        cheats.put("Item damage command", "Weapons no longer receive damage or degrade" + "The command is as follows," + "/itemdamage \n");
	        
	        cheats.put("Duplicate command", "Copies and drops the item stack that you have equipped." + 
	        		"" + "The command is as follows," + "/duplicate \n");

}
	    public static String get(String item) {
	        return cheats.get(item);
	    }
}