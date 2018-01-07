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

	private  Logger log = LoggerFactory.getLogger(Status.class);

	//Overall Status
	public String O_STATUS_ID = "OverallStatusID";
	public Sub_Status O_Status;
	public String ON_ROAD= "On_Road";
	public String IN_CITY = "In_City";
	public String IN_CAVE = "In_Cave";
	public String IN_VILLAGE = "In_Village";
	
	//Normal Status
	public String N_STATUS_ID = "NormalStatusID";
	public Sub_Status N_Status;
	public String NORMAL = "Normal";
	public String LIVING = "Living";
	public String DEAD = "Dead";
	public String IN_DANGER = "In_danger";
	public String IN_BATTLE = "In_battle";
	
	//Personal Status
	public String P_STATUS_ID = "PersonalStatusID";
	public Sub_Status P_Status;
	public String PERFECT = "Perfect";
	public String SOME_SCARTCH = "Some_scratch";
	public String IS_HURT = "Is_hurt";
	public String BADLY_HURT = "Badly_Hurt";

    public Status(String O_Status_n, String N_Status_n, String P_Status_n) {
    	O_Status = new Sub_Status(O_STATUS_ID, O_Status_n);
    	
    	N_Status = new Sub_Status(N_STATUS_ID, N_Status_n);
    	
    	P_Status = new Sub_Status(P_STATUS_ID, P_Status_n);
    }
    
    public class Sub_Status{
    	
    	String ID;
    	
    	String Content;
    	
    	Sub_Status(String i, String c) {
    		ID = i;
    		
    		Content = c;
    	}
    	
    	public String get(String id){
    		if(id.equals(ID)) {
    			return Content;
    		}else {
    			return null;
    		}
    	}

    	
    	public void replace(String i, String c) {
    		if(i.equals(ID)) {
    			Content = c;
    		}
    	}
    }
    
    
}


