package AbsurdRPG.CustomClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class Status {

	private static final Logger log = LoggerFactory.getLogger(Status.class);

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
	public static final String LIVING = "Living";
	public static final String DEAD = "Dead";
	public static final String IN_DANGER = "In_danger";
	public static final String IN_BATTLE = "In_battle";
	
	//Personal Status
	public static final String P_STATUS_ID = "PersonalStatusID";
	public Map<String, String> P_Status;
	public static final String PERFECT = "Perfect";
	public static final String SOME_SCARTCH = "Some_scratch";
	public static final String IS_HURT = "Is_hurt";
	public static final String BADLY_HURT = "Badly_Hurt";

    public Status(String O_Status_n, String N_Status_n, String P_Status_n) {
    	O_Status.put(O_STATUS_ID, O_Status_n);
    	N_Status.put(N_STATUS_ID, N_Status_n);
    	P_Status.put(P_STATUS_ID, P_Status_n);
    }
    
    
}


