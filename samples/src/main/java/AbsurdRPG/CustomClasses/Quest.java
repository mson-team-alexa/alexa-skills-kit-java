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

import AbsurdRPG.CustomClasses.Inventory.Reward;
import AbsurdRPG.CustomClasses.Location.Location;
import AbsurdRPG.CustomClasses.NPCs.NPC;

import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class Quest {

	private static final Logger log = LoggerFactory.getLogger(Quest.class);

	public Location location;
	
	public Reward reward;
    
	public NPC quest_giver;
	
	public String name;
	
	private boolean complete;
	
	private String description;
	
	public Quest(String N, Location L, NPC giver, Reward R) {
		complete = false;
		
		name = N;
		location = L;
		quest_giver = giver;
		reward = R;
	}
	
	public void complete_quest() {
		complete = true;
	}
	
	public boolean get_complete_status() {
		return complete;
	}
	
	public void set_description(String s) {
		description = s;
	}
	
	public String get_description() {
		return description;
	}
}


