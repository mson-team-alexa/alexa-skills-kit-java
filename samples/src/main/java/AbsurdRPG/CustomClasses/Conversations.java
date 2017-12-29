package AbsurdRPG.CustomClasses;

import java.util.ArrayList;

import com.amazon.speech.speechlet.Session;

public class Conversations {

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
	
	// Confirm for tutorial
	public static String take_tutorial = "You have chosen to take the tutorial! ";
	
	// For tutorial
	public static String tutorial_wanring_talk = "You did not follow the step! You need to talk with the head of vilalge right now. Say, talk, to "
			+ "get available people and choose, the head of the village. ";
	public static String tutorial_invalid_character = "You did not choose a valid character! " + Actions.initiateTalk();
	public static String tutorial_warning_choose_subject = "You should choose a subject to talk with right now! " + Actions.initiateTalk();
	
	
	private Conversations() {
		
	}
	
	public static ArrayList<String> getConversationsForDownhillVillage(String name, final Session session) {
		String speechText;
		String repromptText;
		
		ArrayList<String> Response = new ArrayList<String>();
		if(name.equals(HEAD_OF_VILLAGE)) {
			session.setAttribute("TalkWithHeadOfVillage", 1);
			
			speechText = "";
			repromptText = "You need to go out of the village by saying, go to, and location. ";
			
			Response.add(speechText);
			Response.add(repromptText);
			
			return Response;
		}else if(name.equals(VILLAGER)) {
			speechText = "";
			
			repromptText = "You will have to talk with the head of the village to continue. Say, Talk, and choose, the head of the village, to continue";
			
			Response.add(speechText);
			Response.add(repromptText);
			
			return Response;
		}else {
			return null;
		}
	}
	
	public static ArrayList<String> getQuestConversationForDownhillVillage(){
		String speechText = "Go out and hunt them down! ";
		String repromptText = "Go out and hunt them down! ";
		ArrayList<String> response = new ArrayList<String>();
		response.add(speechText);
		response.add(repromptText);
		
		return response;
	}
	
}
