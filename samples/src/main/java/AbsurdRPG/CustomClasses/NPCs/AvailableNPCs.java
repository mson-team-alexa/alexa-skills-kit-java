package AbsurdRPG.CustomClasses.NPCs;

import java.util.ArrayList;

import AbsurdRPG.CustomClasses.Player;
import AbsurdRPG.CustomClasses.Status;
import AbsurdRPG.CustomClasses.Location.Location;
import AbsurdRPG.CustomClasses.Location.Locations;

public class AvailableNPCs {

	private static String HEAD_OF_VILLAGE;
	
	private static String VILLAGER;
	
	private static String DWELLERS;
	
	private static String SILVER_SMITH;
	private static String SILVER_SMITH_MASTER;
	
	private static String MERCHANT;
	private static String RICH_MERCHANT;
	
	private static String OVER_LORD;
	private static String KING;
	private static String QUEEN;
	
	private static String PRINCESS;
	private static String PRINCE;
	
	private static String GUARD;
	private static String GUARDIAN;
	
	private AvailableNPCs() {

		
	}
	
	public static void initialize() {
		HEAD_OF_VILLAGE = "The head of the village";
		
		VILLAGER = "Villagers";
		
		DWELLERS = "Dwellers";
		
		SILVER_SMITH = "the Silver Smith";
		SILVER_SMITH_MASTER = "the Silver Smith Master";
		
		MERCHANT = "Merchant";
		RICH_MERCHANT = "wealthy Merchant";
		
		OVER_LORD ="the Over Lord";
		KING = "the King";
		QUEEN = "the Queen";
		
		PRINCESS = "the Princess";
		PRINCE = "the Prince";
		
		GUARD = "the guards";
		GUARDIAN = "the guardians";
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
