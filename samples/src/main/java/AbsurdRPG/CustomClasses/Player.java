package AbsurdRPG.CustomClasses;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import AbsurdRPG.CustomClasses.Inventory.Inventory;
import AbsurdRPG.CustomClasses.Location.Location;

public class Player{

	//Status Variale
	//Overall Status
	public static final String O_STATUS_ID = "OverallStatusID";
	public Map<String, String> O_Status;
	public static final String ON_ROAD= "On_Road";
	public static final String IN_CITY = "In_City";
	public static final String IN_CAVE = "In_Cave";
	public static final String IN_VILLAGE = "In_Village";
	
	//Normal Status
	public static final String N_STATUS_ID = "NormalStatusID";
	public Map<String, String> N_Status;
	public static final String NORMAL = "Normal";
	public static final String IN_DANGER = "In_danger";
	public static final String IN_BATTLE = "In_battle";
	
	//Personal Status
	public static final String P_STATUS_ID = "PersonalStatusID";
	public Map<String, String> P_Status;
	public static final String PERFECT = "Perfect";
	public static final String SOME_SCARTCH = "Some_scratch";
	public static final String IS_HURT = "Is_hurt";
	public static final String BADLY_HURT = "Badly_Hurt";
	
	public static final Logger log = LoggerFactory.getLogger(Player.class);

	public static boolean isAlive;
	
	public static Integer HealthPoints;
	
    public static Location CURRENT_LOCATION;
    
    public static Status CURRENT_STATUS;
	
	private Player() {
		HealthPoints = 50;

		CURRENT_STATUS = new Status(IN_VILLAGE, NORMAL, PERFECT);
	}
	
	//Status Code
	public static Status CurrentStatus() {
    	return CURRENT_STATUS;
    }
    
    public static String getOverallStatus() {
    	return CURRENT_STATUS.O_Status.get(O_STATUS_ID);
    }
    
    public static String getNormalStatus() {
    	return CURRENT_STATUS.O_Status.get(N_STATUS_ID);
    }
    
    public static String getPersonalStatus() {
    	return CURRENT_STATUS.O_Status.get(P_STATUS_ID);
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


