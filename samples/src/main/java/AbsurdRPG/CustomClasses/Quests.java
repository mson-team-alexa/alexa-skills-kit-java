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

import AbsurdRPG.CustomClasses.Location.Location;
import AbsurdRPG.CustomClasses.Location.Locations;
import AbsurdRPG.CustomClasses.Inventory.Rewards;
import AbsurdRPG.CustomClasses.Inventory.Reward;

public class Quests {

	private static final Logger log = LoggerFactory.getLogger(Quests.class);

	public static ArrayList<Quest> quests; 
	
	public static Quest Tutorial_quest;
	
	private Quests(){
		
	}
	
	public static void Initialize() {
		Tutorial_quest = new Quest("Tutorial Quest -- Kill the Barbarians", Locations.DownhillVillage, Locations.DownhillVillage.returnPopulation().get(0), Rewards.generateGreenWeaponReward(1));
		
		
	}
}


