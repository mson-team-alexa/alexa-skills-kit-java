package AbsurdRPG.CustomClasses;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import AbsurdRPG.CustomClasses.Inventory.Inventory;
import AbsurdRPG.CustomClasses.Location.Location;
import AbsurdRPG.CustomClasses.Location.Locations;

public class Player{

	//Status Variable
	//Overall Status
	public static String O_STATUS_ID;
	public Map<String, String> O_Status;
	public static String ON_ROAD;
	public static String IN_CITY;
	public static String IN_CAVE;
	public static String IN_VILLAGE;
	
	//Normal Status
	public static String N_STATUS_ID;
	public Map<String, String> N_Status;
	public static String NORMAL;
	public static String IN_DANGER;
	public static String IN_BATTLE;
	
	//Personal Status
	public static String P_STATUS_ID;
	public Map<String, String> P_Status;
	public static String PERFECT;
	public static String SOME_SCARTCH;
	public static String IS_HURT;
	public static String BADLY_HURT;
	
	public static final Logger log = LoggerFactory.getLogger(Player.class);

	public static boolean isAlive;
	
	public static Integer HealthPoints;
	public static Integer Max_HealthPoints;
	
    public static Location CURRENT_LOCATION;
    
    public static Status CURRENT_STATUS;
	
	private Player() {
		
	}
	
	public static void initialize() {
		isAlive = true;
		
		Max_HealthPoints= 50;
		
		HealthPoints = 50;		
		
		O_STATUS_ID = "OverallStatusID";
		ON_ROAD= "On_Road";
		IN_CITY = "In_City";
		IN_CAVE = "In_Cave";
		IN_VILLAGE = "In_Village";
		
		//Normal Status
		N_STATUS_ID = "NormalStatusID";
		NORMAL = "Normal";
		IN_DANGER = "In_danger";
		IN_BATTLE = "In_battle";
		
		//Personal Status
		P_STATUS_ID = "PersonalStatusID";
		PERFECT = "Perfect";
		SOME_SCARTCH = "Some_scratch";
		IS_HURT = "Is_hurt";
		BADLY_HURT = "Badly_Hurt";

		CURRENT_STATUS = new Status(IN_VILLAGE, NORMAL, PERFECT);
		
		CURRENT_LOCATION = Locations.DownhillVillage;
	}
	
	//Status Code
	public static Status CurrentStatus() {
    	return CURRENT_STATUS;
    }
    
    public static String getOverallStatus() {
    	return CURRENT_STATUS.O_Status.get(O_STATUS_ID);
    }
    
    public static String getNormalStatus() {
    	return CURRENT_STATUS.N_Status.get(N_STATUS_ID);
    }
    
    public static String getPersonalStatus() {
    	return CURRENT_STATUS.P_Status.get(P_STATUS_ID);
    }
    
    public void setCurrentStatus(String O_Status_n, String N_Status_n, String P_Status_n) {
    	CURRENT_STATUS.O_Status.replace(O_STATUS_ID, O_Status_n);
    	CURRENT_STATUS.N_Status.replace(N_STATUS_ID, N_Status_n);
    	CURRENT_STATUS.P_Status.replace(P_STATUS_ID, P_Status_n);
    }
    
	public void setOverallStatus(String O_Status_n) {
		CURRENT_STATUS.O_Status.replace(O_STATUS_ID, O_Status_n);
	}
	
	public void setNormalStatus(String N_Status_n) {
		CURRENT_STATUS.O_Status.replace(N_STATUS_ID, N_Status_n);
	}
	
	public void setPersonalStatus(String P_Status_n) {
		CURRENT_STATUS.O_Status.replace(P_STATUS_ID, P_Status_n);
	}
}


