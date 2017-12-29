package AbsurdRPG.CustomClasses.NPCs;

import java.util.ArrayList;

import AbsurdRPG.CustomClasses.Player;
import AbsurdRPG.CustomClasses.Status;
import AbsurdRPG.CustomClasses.Location.Location;
import AbsurdRPG.CustomClasses.Location.Locations;

public class AvailableNPCs {

	private final static String HEAD_OF_VILLAGE = "The head of the village";
	
	private final static String VILLAGER = "Villagers";
	
	private final static String DWELLERS = "Dwellers";
	
	private final static String SILVER_SMITH = "the Silver Smith";
	private final static String SILVER_SMITH_MASTER = "the Silver Smith Master";
	
	private final static String MERCHANT = "Merchant";
	private final static String RICH_MERCHANT = "wealthy Merchant";
	
	private final static String OVER_LORD ="the Over Lord";
	private final static String KING = "the King";
	private final static String QUEEN = "the Queen";
	
	private final static String PRINCESS = "the Princess";
	private final static String PRINCE = "the Prince";
	
	private final static String GUARD = "the guards";
	private final static String GUARDIAN = "the guardians";
	
	private AvailableNPCs() {
		
	}
	
	public static ArrayList<String> returnAvailableNPCs(){
		
		ArrayList<String> Available_NPCs = new ArrayList<String>();
		
		if((Player.getNormalStatus()).equals(Player.IN_DANGER) || (Player.getNormalStatus()).equals(Player.IN_BATTLE)) {
			return null;
		}else {
			if(Player.getOverallStatus().equals(Player.IN_VILLAGE)) {
				Available_NPCs.add(HEAD_OF_VILLAGE);
				Available_NPCs.add(VILLAGER);
				Available_NPCs.add(MERCHANT);
				
				return Available_NPCs;
			}else if(Player.getOverallStatus().equals(Player.IN_CITY)) {
				if(Player.CURRENT_LOCATION.getClass().getName().equals(Locations.CITY_OF_DAWNSTAR)) {
					Available_NPCs.add(DWELLERS);
					Available_NPCs.add(SILVER_SMITH);
					Available_NPCs.add(MERCHANT);
					Available_NPCs.add(KING);
					Available_NPCs.add(PRINCESS);
					Available_NPCs.add(GUARD);
					
					return Available_NPCs;
				}else if(Player.CURRENT_LOCATION.getClass().getName().equals(Locations.CITY_OF_MERLINSBURG)) {
					Available_NPCs.add(DWELLERS);
					Available_NPCs.add(SILVER_SMITH);
					Available_NPCs.add(MERCHANT);
					Available_NPCs.add(QUEEN);
					Available_NPCs.add(PRINCE);
					Available_NPCs.add(GUARD);
					
					return Available_NPCs;
				}else if(Player.CURRENT_LOCATION.getClass().getName().equals(Locations.CITY_OF_UNIFLORA)) {
					Available_NPCs.add(DWELLERS);
					Available_NPCs.add(SILVER_SMITH);
					Available_NPCs.add(MERCHANT);
					Available_NPCs.add(OVER_LORD);
					Available_NPCs.add(GUARDIAN);
					
					return Available_NPCs;
				}else {
					return null;
				}
			}else {
				return null;
			}
		}
	}
	
}
