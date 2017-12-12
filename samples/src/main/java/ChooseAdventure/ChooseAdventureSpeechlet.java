package ChooseAdventure;


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

	public class ChooseAdventureSpeechlet implements Speechlet {

	    private static final Logger log = LoggerFactory.getLogger(ChooseAdventureSpeechlet.class);

	    /**
	     * Custom Slot key for the name of the story requested by the user
	     */
	    private static final String CUSTOM_SLOT_STORY_NAME = "StoryName";
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
	                + " Blast off in 5...4.3...2...1...BLAST OFF! (engine sound effect) It’s been 200 days since you traveled into space. "
	                + "There’s been minimal contact from your superiors as you travel deeper into space, waiting for your boss to give you your next task. "
	                + "Suddenly, you hear a beeping noise- it’s a message from your boss! He’s sent you the coordinates of a suspected black hole and wants you to investigate."
	                + " However, you have a feeling that there’s something hiding in space, waiting for you to find it..";
	        String repromptText =
	        		"Welcome, Commander. After years of training and countless successful missions, you have been chosen to embark on one of the most dangerous missions in the history of space travel."
	    	                + " Your task, if you choose to accept it, is to locate and research black holes in outer space. "
	    	                + " This has never been done before, and we cannot guarantee your safety. So lets go."
	    	                + " Blast off in 5...4.3...2...1...BLAST OFF! (engine sound effect) It’s been 200 days since you traveled into space. "
	    	                + "There’s been minimal contact from your superiors as you travel deeper into space, waiting for your boss to give you your next task. "
	    	                + "Suddenly, you hear a beeping noise- it’s a message from your boss! He’s sent you the coordinates of a suspected black hole and wants you to investigate."
	    	                + " However, you have a feeling that there’s something hiding in space, waiting for you to find it.."
	    	                + " The choice is yours, commander:\n" + 
	    	                "";
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
	        Slot optionSlot = intent.getSlot("OptionNum");
	    	int userOp = 0;
String response = "";
	        Scenario currentScenario = (Scenario) session.getAttribute("CURR_SCENARIO");

	       
            
            
            
	        
	        // if the scenario is not null
	        if (currentScenario != null) {
	            
	        
	        	
	        	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	        	outputSpeech.setText(currentScenario.getop1());
	        	Reprompt reprompt = new Reprompt();
	        	reprompt.setOutputSpeech(outputSpeech);
	        	SpeechletResponse resp = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	        	return resp;
	        		// Alexa should speak the corresponding option
	        	
	        } else {
	        	
	        	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	        	outputSpeech.setText("Sorry, I couldn't find that option you requested. Maybe you said it incorrectly , please try again.");
	        	Reprompt reprompt = new Reprompt();
	        	reprompt.setOutputSpeech(outputSpeech);
	        	SpeechletResponse resp = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	        	return resp;
	        		// Alexa should say some error message
	        }
	        
	        

	    	
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
	        Slot storySlot = intent.getSlot(CUSTOM_SLOT_STORY_NAME);
	        
	        
	        if (storySlot != null && storySlot.getValue() != null) {
	            String storyName = storySlot.getValue();
	            session.setAttribute(CUSTOM_SLOT_STORY_NAME, storyName);

	            //to get a scenario
	            Adventures curr_story = ChooseAdventure.Stories.getName(storyName);
	            
//	            Scenario curr_scenario = new Scenario(option1, option2, response);
	            
	            int curr_scenario_num = (int) session.getAttribute("SCENARIO_NUMBER");
	            // get the list of scenarios from the story
	            Scenario curr_scenario = curr_story.getScenList().get(curr_scenario_num);	            
	            session.setAttribute("CURR_SCENARIO", curr_scenario);
	            
	            
		        Slot optionSlot1 = intent.getSlot(CUSTOM_SLOT_OPTION_NUM);
	            userOp = Integer.parseInt(optionSlot1.getValue());
	            switch(userOp)
	            {
	            case 1: 
	            		 response = curr_scenario.getop1();
	            		 break;
	            		 
	            case 2:
	            		response = curr_scenario.getop2();
	            		break;
	           default:
	        	   		response = "Not valid a answer";
	        	   		break;
	            
	            }
	            	curr_scenario_num++;
	            	session.setAttribute("SCENARIO_NUMBER", curr_scenario_num);
	            
	            
	            // have to get the user choice 
	            			// prompt the user with a choice
	            			// grab the answer and go to switch case and return response
	            			// then go on to tell the rest of the story
	            			
	            
	            // get the story content for the corresponding story
	            
	            
	            
	            
	        	
	      
	        	
		        
	            // create an appropriate SSML response
		        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
		        outputSpeech.setSsml(response);
		
		        Reprompt reprompt = new Reprompt();
		        reprompt.setOutputSpeech(null);
		        SpeechletResponse resp = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
		        return resp;
		        
	        } else {
	        	
	        	// if the story requested by the user could not be found, create an appropriate response
	        	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
	        	outputSpeech.setText("Sorry, I couldn't find the story you requested. Maybe you said it incorrectly or  please try naming another adventure.");
	        	Reprompt reprompt = new Reprompt();
	        	reprompt.setOutputSpeech(outputSpeech);
	        	SpeechletResponse resp = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
	        	return resp;
	        }
	        
	    }

	    
}