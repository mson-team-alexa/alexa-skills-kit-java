package ChooseAdventure;


	import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;

	import com.amazon.speech.slu.Intent;
	import com.amazon.speech.slu.Slot;
	import com.amazon.speech.speechlet.IntentRequest;
	import com.amazon.speech.speechlet.LaunchRequest;
	import com.amazon.speech.speechlet.Session;
	import com.amazon.speech.speechlet.SessionEndedRequest;
	import com.amazon.speech.speechlet.SessionStartedRequest;
	import com.amazon.speech.speechlet.Speechlet;
	import com.amazon.speech.speechlet.SpeechletException;
	import com.amazon.speech.speechlet.SpeechletResponse;
	import com.amazon.speech.ui.PlainTextOutputSpeech;
	import com.amazon.speech.ui.SsmlOutputSpeech;

import storyteller.Stories;

import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

	public class ChooseAdventureSpeechlet implements Speechlet {

	    private static final Logger log = LoggerFactory.getLogger(ChooseAdventureSpeechlet.class);

	    /**
	     * Custom Slot key for the name of the story requested by the user
	     */
	    private static final String COLOR_KEY = "NAME";
	    
	    private static final String COLOR_SLOT = "StoryName"; //nameslot
	    private static final String CUSTOM_SLOT_OPTION_NUM = "OptionNum";
	    private int SCENARIO_NUMBER = 0;

	        
	    @Override
	    public void onSessionStarted(final SessionStartedRequest request, final Session session)
	            throws SpeechletException {
	        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
	                session.getSessionId());
	        
	        SCENARIO_NUMBER = 0;
	        session.setAttribute("SCENARIO_NUMBER", SCENARIO_NUMBER);

	    }

	    @Override
	    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
	            throws SpeechletException {
	        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
	                session.getSessionId());
	        
	        return getWelcomeResponse();
	    }
	    
	    
	    

	    @Override
	    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
	            throws SpeechletException {
	        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
	                session.getSessionId());

	        Intent intent = request.getIntent();
	        String intentName = (intent != null) ? intent.getName() : null;

	        if ("TellMeMyStoryIntent".equals(intentName)) { 
	            return handleTellMeMyStoryIntent(intent, session);
	            
	        
	        } 
	        else if("TellMeMyOptionIntent".equals(intentName)) { 
	            return handleTellMeMyOptionIntent(intent, session);
	        }
	        else if ("AMAZON.HelpIntent".equals(intentName)) { // Alexa helps out by suggesting valid sample utterances
	        	return getHelpResponse();
	        	
	        } else if ("AMAZON.StopIntent".equals(intentName)) {
	            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	            outputSpeech.setText("Goodbye");
	            return SpeechletResponse.newTellResponse(outputSpeech);
	            
	        } else if ("AMAZON.CancelIntent".equals(intentName)) {
	            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	            outputSpeech.setText("Goodbye");
	            return SpeechletResponse.newTellResponse(outputSpeech);
	            
	        } 
	        
	        else {
	            throw new SpeechletException("Invalid Intent");
	        }
	    }
	    

	    
	    /**
	     * Creates and returns a {@code SpeechletResponse} with a welcome message.
	     *
	     * @return SpeechletResponse spoken welcome message
	     */
	    private SpeechletResponse getWelcomeResponse() { // start story
	        // Create the welcome message
	        String speechText =
	                "Welcome, Commander. After years of training and countless successful missions, you have been chosen to embark on one of the most dangerous missions in the history of space travel."
	                + " Your task, if you choose to accept it, is to locate and research black holes in outer space. "
	                + " This has never been done before, and we cannot guarantee your safety. So lets go."
	                + " Blast off in 5...4.3...2...1...BLAST OFF! It's 200 days since you traveled into space. "
	                + "There’s been minimal contact from your superiors as you travel deeper into space, waiting for your boss to give you your next task. "
	                + "Suddenly, you hear a beeping noise- it’s a message from your boss! He’s sent you the coordinates of a suspected black hole and wants you to investigate."
	                + " However, you have a feeling that there’s something hiding in space, waiting for you to find it.."
	                + " The choice is yours, commander" 
	                + "To follow the coordinates, say Follow. To follow your gut and go another way, say My Way."
	                ;
	        String repromptText =
	        		"Welcome, Commander. After years of training and countless successful missions, you have been chosen to embark on one of the most dangerous missions in the history of space travel."
	    	                + " Your task, if you choose to accept it, is to locate and research black holes in outer space. "
	    	                + " This has never been done before, and we cannot guarantee your safety. So lets go."
	    	                + " Blast off in 5...4.3...2...1...BLAST OFF! It’s been 200 days since you traveled into space. "
	    	                + "There’s been minimal contact from your superiors as you travel deeper into space, waiting for your boss to give you your next task. "
	    	                + "Suddenly, you hear a beeping noise- it’s a message from your boss! He’s sent you the coordinates of a suspected black hole and wants you to investigate."
	    	                + " However, you have a feeling that there’s something hiding in space, waiting for you to find it.."
	    	                + " The choice is yours, commander:\n" 
	    	                + "To follow the coordinates, say Follow. To follow your gut and go another way, say My Way."
	    	                ;
	        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
	        speech.setText(speechText);
	        
	        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
	        repromptSpeech.setText(repromptText);
	        Reprompt reprompt = new Reprompt();
	        reprompt.setOutputSpeech(repromptSpeech);
	        
	        return SpeechletResponse.newAskResponse(speech, reprompt);
	    }
	   
	    
	    
	    
	    
	    /**
	     * Creates and returns a {@code SpeechletResponse} with a help message.
	     *
	     * @return SpeechletResponse spoken help message
	     */
	    private SpeechletResponse getHelpResponse() { 
	        // Create the help message
	        String speechText =
	                "You can tell me which adventure you would like to play "
	                        + "by saying, I want to play the Space Story. "
	                        + "Now, which adventure would you like to play?";
	        String repromptText =
	                "Which adventure would you like to play?";

	        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
	        speech.setText(speechText);
	        
	        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
	        repromptSpeech.setText(repromptText);
	        Reprompt reprompt = new Reprompt();
	        reprompt.setOutputSpeech(repromptSpeech);
	        
	        return SpeechletResponse.newAskResponse(speech, reprompt);
	    }

	    
	    
	    
	    @Override
	    public void onSessionEnded(final SessionEndedRequest request, final Session session)
	            throws SpeechletException {
	        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
	                session.getSessionId());

	    }
	    
	    
	        
	    
	    private SpeechletResponse handleTellMeMyOptionIntent(Intent intent, final Session session) {
	    	String storyContent = ""; 
	    	String option1 = "";
	    	String option2 = "";
	    	int userOp = 0;
	    	String response = "";
	    
	    	// get the name of the story from the custom slot (within the JSON request)
	        Slot storySlot = intent.getSlot(COLOR_SLOT);
	    
	        Slot optionSlot = intent.getSlot("OptionNum");
	        
	        Scenario currentScenario = (Scenario) session.getAttribute("CURR_SCENARIO");
	        if (storySlot != null && storySlot.getValue() != null) {
	            String storyName = storySlot.getValue();
	            session.setAttribute(COLOR_SLOT, storyName);

	            //to get a scenario
	            Adventures curr_story = ChooseAdventure.Stories.getName(storyName);
	       
            
	        
            int curr_scenario_num = (int) session.getAttribute("SCENARIO_NUMBER");
            // get the list of scenarios from the story
            Scenario curr_scenario = curr_story.getScenList().get(curr_scenario_num);	            
            session.setAttribute("CURR_SCENARIO", curr_scenario);
            
            
	        Slot optionSlot1 = intent.getSlot(CUSTOM_SLOT_OPTION_NUM);
            userOp = Integer.parseInt(optionSlot1.getValue());
            switch(userOp)
            {
            case 1: 
        		response = curr_scenario.getResponse();
            		 break;
            		 
            case 2:
            		response = curr_scenario.getResponse2();
            		break;
           default:
        	   		response = "Not valid a answer";
        	   		break;
            
            }
            	curr_scenario_num++;
            	session.setAttribute("SCENARIO_NUMBER", curr_scenario_num);
            
            
	        
	        // if the scenario is not null
	        if (currentScenario != null) {
	            
	        
	        	
	        	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	        	outputSpeech.setText(response);
	        	Reprompt reprompt = new Reprompt();
	        	reprompt.setOutputSpeech(outputSpeech);
	        	SpeechletResponse resp = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	        	return resp;
	        		// Alexa should speak the corresponding option
	        	
	        } }
	      
	        	
	        	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	        	outputSpeech.setText("Sorry, I couldn't find that option you requested. Maybe you said it incorrectly , please try again.");
	        	Reprompt reprompt = new Reprompt();
	        	reprompt.setOutputSpeech(outputSpeech);
	        	SpeechletResponse resp = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	        	return resp;
	        		// Alexa should say some error message
	        
	       
	        

	    	
	    
	    }
	    
	    /**
	     * Tells the story requested by the user
	     *
	     * @param intent
	     *            the intent object
	     * @param session
	     *            the session object
	     * @return SpeechletResponse the speechlet response
	     */
	    private SpeechletResponse handleTellMeMyStoryIntent(Intent intent, final Session session) {
	        
	    	String storyContent = ""; 
	    	String option1 = "";
	    	String option2 = "";
	    	int userOp = 0;
	    	String response = "";
	    
	    	// get the name of the story from the custom slot (within the JSON request)
	        Slot storySlot = intent.getSlot(COLOR_SLOT);
	        
	        
	        if (storySlot != null && storySlot.getValue() != null) {
	            String storyName = storySlot.getValue();
	            session.setAttribute(COLOR_SLOT, storyName);

	            //to get a scenario
	            Adventures curr_story = ChooseAdventure.Stories.getName(storyName);
	            
	           
	    	    
		        Slot optionSlot = intent.getSlot("OptionNum");
		        
		        Scenario currentScenario = (Scenario) session.getAttribute("CURR_SCENARIO");
		       
		        
	            int curr_scenario_num = (int) session.getAttribute("SCENARIO_NUMBER");
	            // get the list of scenarios from the story
	            Scenario curr_scenario = curr_story.getScenList().get(curr_scenario_num);	            
	            session.setAttribute("CURR_SCENARIO", curr_scenario);
	            
	            
		        Slot optionSlot1 = intent.getSlot(CUSTOM_SLOT_OPTION_NUM);
//	           
		        // Get the slots from the intent.
		        Map<String, Slot> slots = intent.getSlots();

		        // Get the color slot from the list of slots.
		        Slot favoriteColorSlot = slots.get(COLOR_SLOT);
		        String speechText, repromptText;

		        // Check for favorite color and create output to user.
		        if (favoriteColorSlot != null) {
		            // Store the user's favorite color in the Session and create response.
		            String favoriteColor = favoriteColorSlot.getValue();
		            session.setAttribute(COLOR_KEY, favoriteColor);
		            
		        } else {
		            // Render an error since we don't know what the users favorite color is.
		            speechText = "Error";
		       
		        }
		        
		        
		        
		        
		        String favoriteColor = (String) session.getAttribute(COLOR_KEY);

		        // Check to make sure user's favorite color is set in the session.
		        

		        		if(favoriteColor.equals("Follow"))
		        			response = "You set the rocket to autopilot, heading towards the coordinates. It takes a few days, "
		        	    			+" but eventually you reach the coordinates and see that your boss’s guess was right: "
		        	    			+ "there is a block hole here. You decide to play it safe before jumping into the hole, "
		        	    			+"but you’re not sure what to do first. Should you see if there’s any planets or life nearby?" 
		        	    			+ "The choice is yours, commander:" + 
		        	    			"	To scan the black hole for life, say Scan." + 
		        	    			"	To orbit the black hole and search for nearby planets, say Search";
		        		else if(favoriteColor.equals("My Way"))
		        			response = "You stick with your gut and turn around the ship, piloting it into the abyss. "
		        	    			+"Your boss calls you, furious, ordering you to turn around, but you ignore him, "
		        	    			+"following your instinct. You’re the one in space anyway- what say does he have? "
		        	    			+ "As you fly into the unknown, your ship suddenly loses power. You stand up, "
		        	    			+"heading to fix the power, but one monitor flickers on, displaying a message in a "
		        	    			+"language you’ve never seen before. Your curiosity spikes- you want to translate the message, "
		        	    			+"but you know you need to fix the power before your oxygen runs out…”" + 
		        	    			"The choice is yours, commander:" + 
		        	    			"	To translate the message, say “Translate”" + 
		        	    			"	To fix the power, say “Fix";
		        		else if(favoriteColor.equals("Scan"))
		        			response = "You scan the black hole. The results come up negative- "
		        	    			+"there is no life in the hole. As soon as you get the results, "
		        	    			+"the power goes out and you see a message saying that the engine has lost power. "
		        	    			+"Your gut begins to sink as you feel the ship being pulled into black hole."
		        	    			+"Without the engines, you will surely die, but you wonder what caused the engines to fail…" + 
		        	    			"The choice is yours, commander:" + 
		        	    			"	To fix the engines, say Fix" + 
		        	    			"	To figure out why the engines lost power, say Engines" ;
		        		else if(favoriteColor.equals("Search"))
		        			response = "You orbit the black hole, looking for nearby planets. After a few hours, "
		        	    			+"you see a big purple sphere in the distance- could it be a new planet? "
		        	    			+"You steer towards the object and realize that you have found a new planet! "
		        	    			+"As you record the planet’s coordinates in your log, you begin to wonder- "
		        	    			+"could there be life forms on this planet? Could there be more planets out there?" + 
		        	    			"The choice is yours, commander:" + 
		        	    			"	To scan the planet for life, say Scan" + 
		        	    			"	To search for more planets, say Search";
		        		else if(favoriteColor.equals("Translate"))
		        			response =     			"You decide to translate the message- contact with extraterrestrial life "
		        	    			+"is far more important than the ship’s power. It’s takes some time, "
		        	    			+"but eventually you think you’ve got some of the message. You can make out two words: "
		        	    			+"AIM and DANGER. Your gut sinks- are the aliens hostile? Are they threatening you, "
		        	    			+"or are they warning you? You realize you have two options: fix the power and escape from the aliens, "
		        	    			+"or find out who sent the message and let them know you only want peace." + 
		        	    			"The choice is yours, commander:" + 
		        	    			"	To fix the power and escape, say Fix" + 
		        	    			"	To send a peace letter to the aliens, say Letter" ;
		        		else if(favoriteColor.equals("Fix"))
		        			response = "You decide to ignore the message and fix the power. It takes some time, "
		        	    			+"but eventually the lights turn back on and you feel the engine shaking the ship. "
		        	    			+"However, seconds after you fix the power, the ship’s alarm goes off. You’re under attack! "
		        	    			+"You begin to panic- do you try and find the threat or use an escape pod to head back to Earth?" + 
		        	    			"The choice is yours, commander:" + 
		        	    			"	To find the threat, say Find" + 
		        	    			"	To leave in the escape pod, say Leave";
		         else {
		            // Since the user's favorite color is not set render an error message.
		            response =
		                    "Error";
		        }

		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        //Look at outline
		        	// were going to store user's answer in sessions then compare that with the option and return the recommended response
		        // with if statements (this is the only way I understand it)
		        
		            // Get the slots from the intent.
		         

		        
		        
		           
		        

		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
		        
	            	curr_scenario_num++;
	            	session.setAttribute("SCENARIO_NUMBER", curr_scenario_num);
	            
	           
	            
	            
	            
	            
	        	
	      
	        	
	                return getSpeechletResponse(response, response);

		        
	        } else {
	        	
	        	// if the story requested by the user could not be found, create an appropriate response
	        	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	   response = "Sorry, I couldn't find the story you requested. Maybe you said it incorrectly or  please try naming another adventure.";
	        	
                return getSpeechletResponse(response, response);

	        
	        }
	        
	    }
	    private SpeechletResponse getSpeechletResponse(String speechText, String repromptText) {
	        // Create the Simple card content.
	        SimpleCard card = new SimpleCard();
	        card.setTitle("Session");
	        card.setContent(speechText);

	        // Create the plain text output.
	        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
	        speech.setText(speechText);

	        
	            // Create reprompt
	            PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
	            repromptSpeech.setText(repromptText);
	            Reprompt reprompt = new Reprompt();
	            reprompt.setOutputSpeech(repromptSpeech);

	            return SpeechletResponse.newAskResponse(speech, reprompt, card);

	       
	    }
	    
}