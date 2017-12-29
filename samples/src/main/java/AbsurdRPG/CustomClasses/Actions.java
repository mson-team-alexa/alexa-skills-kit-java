package AbsurdRPG.CustomClasses;

import java.util.ArrayList;
import AbsurdRPG.CustomClasses.NPCs.AvailableNPCs;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.speechlet.Session;

public class Actions {

	private static final Logger log = LoggerFactory.getLogger(Actions.class);

	private static final String ACTIVE_QUEST_ID = "ActiveQuestID";
	
    private Actions() {
    }

    public static void acceptQuest(String s, Quest quest, final Session session) {
    	s += ". You have accepted the quest: " + quest.name;
    	
    	session.setAttribute(ACTIVE_QUEST_ID, quest);
    	
    	log.
    	
    	Quests.quests.add(quest);
    }
    
    public static ArrayList<String> initiateTalk() {
    	String speechText = "";
    	String repromptText = "";
    	
    	ArrayList<String> NPC_list = AvailableNPCs.returnAvailableNPCs();
    	
    	ArrayList<String> Response_list = new ArrayList<String>();
    	
    	if(NPC_list == null) {
    		speechText = "You can not talk with any one now! ";
    		repromptText = "You are in danger! There is no one to talk to! ";
    		
    		Response_list.add(speechText);
    		Response_list.add(repromptText);
    		
    		return Response_list;
    	}else {
    		speechText = "You can talk with ";
        	
        	for(int i = 0; i < NPC_list.size(); i++) {
        		if(i == NPC_list.size() - 1) {
        			speechText += " . ";
        		}else {
        			speechText += NPC_list.get(i) + ", ";
        		}
        	}
        	
        	speechText += "Who would you like to talk to? ";
        	repromptText = speechText;
        	
        	Response_list.add(speechText);
        	Response_list.add(repromptText);
        	
        	return Response_list;
    	}
    }
	
	public static String checkSubject(String name){
		ArrayList<String> NPC_list = AvailableNPCs.returnAvailableNPCs();
		
		boolean exist = false;
		
		for(String s : NPC_list) {
			if(name.equals(s)) {
				exist = true;
			}
		}
		
		if(exist) {
			return name;
		}else {
			return null;
		}
	}
}


