
package storyteller;

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
import com.amazon.speech.ui.Reprompt;

public class StoryTellerSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(StoryTellerSpeechlet.class);

    /**
     * Custom Slot key for the name of the story requested by the user
     */
    private static final String CUSTOM_SLOT_STORY_NAME = "StoryName";

    private static final String STAGE_NAME = "stage";
    
    private static final int BEGINNING_STAGE = 0;
    
    private static final int ASK_NAME_STAGE = 1;
    
    private static final String STORY_ID = "story";
    
        
    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        
        // greets the user with a welcome response
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if ("TellMeAStoryIntent".equals(intentName)) { // Alexa tells an Aesop's Fable
            return handleTellMeAStoryIntent(intent, session);
            
        } else if ("TellMeTheMoralOfIntent".equals(intentName)) { // Alexa tells the moral of a fable
        	return handleTellMeTheMoralIntent(intent, session);
        	
        } else if ("AMAZON.HelpIntent".equals(intentName)) { // Alexa helps out by suggesting valid sample utterances
        	return getHelpResponse();
        	
        } else if ("AMAZON.StopIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");
            return SpeechletResponse.newTellResponse(outputSpeech);
            
        } else if ("AMAZON.CancelIntent".equals(intentName)) {
            PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.setText("Goodbye");
            return SpeechletResponse.newTellResponse(outputSpeech);
            
        } else {
            throw new SpeechletException("Invalid Intent");
        }
    }
    
    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken welcome message
     */
    private SpeechletResponse getWelcomeResponse() {
        // Create the welcome message
        String speechText =
                "Welcome to the Story Teller! Please tell me which story you would like to hear "
                        + "by saying, tell me the story of The Gnat and the Bull";
        String repromptText =
                "Which story would you like to hear?";

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
                "You can tell me which story you would like to hear "
                        + "by saying, tell me the story of blah blah blah. "
                        + "Now, which story would you like to hear?";
        String repromptText =
                "Which story would you like to hear?";
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

        // any session cleanup logic would go here
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
    private SpeechletResponse handleTellMeAStoryIntent(Intent intent, final Session session) {
        
    	String storyContent = ""; 
        
    	// get the name of the story from the custom slot (within the JSON request)
        Slot storySlot = intent.getSlot(CUSTOM_SLOT_STORY_NAME);
    	
        if(storySlot != null && storySlot.getValue() != null && Stories.getFableWithName(storySlot.getValue()) != null) {
        	
        	if(session.getAttributes().containsKey(STAGE_NAME)) {
        		
        		if((Integer)session.getAttribute(STAGE_NAME) == BEGINNING_STAGE) {
        			
        			session.setAttribute(STAGE_NAME, ASK_NAME_STAGE);
        			
        			String storyName = storySlot.getValue();
        			
        			session.setAttribute(STORY_ID, storyName);
    				
    	            // get the story content for the corresponding story
    	            storyContent = "<speak>" + Stories.getFableWithName(storyName).getStoryContent();
    		        
    	            // create an appropriate SSML response
    		        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
    		        outputSpeech.setSsml(storyContent);
    		
    		        Reprompt reprompt = new Reprompt();
    		        reprompt.setOutputSpeech(null);
    		        SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    		        return response;
    		        
        		}else if((Integer)session.getAttribute(STAGE_NAME) == ASK_NAME_STAGE) {
        			
        			String storyName = storySlot.getValue();
        			
        			if(((String)session.getAttribute(STORY_ID)).equals(storyName))
        			{	
        				String prefix = "<speak><p>You want to hear the story again? <emphasis level=\"strong\">No problem.</emphasis></p> ";
        				
        				session.setAttribute(STORY_ID, storyName);
        				
        	            // get the story content for the corresponding story
        	            storyContent = prefix + Stories.getFableWithName(storyName).getStoryContent();
        		        
        	            // create an appropriate SSML response
        		        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        		        outputSpeech.setSsml(storyContent);
        		
        		        Reprompt reprompt = new Reprompt();
        		        reprompt.setOutputSpeech(null);
        		        SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
        		        return response;
        		        
        			}else {
        				
        				String prefix = "<speak><p>You want to hear another story? <emphasis level=\"strong\">Sure.</emphasis>" + 
        				" Here is your story.</p> ";
                				
        				session.setAttribute(STORY_ID, storyName);
        				
        	            // get the story content for the corresponding story
        	            storyContent = prefix + Stories.getFableWithName(storyName).getStoryContent();
        		        
        	            // create an appropriate SSML response
        		        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        		        outputSpeech.setSsml(storyContent);
        		
        		        Reprompt reprompt = new Reprompt();
        		        reprompt.setOutputSpeech(null);
        		        SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
        		        return response;
        			}
        		}else {
                	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
                	outputSpeech.setText("Sorry, I do not know what you are asking about. ");
                	Reprompt reprompt = new Reprompt();
                	reprompt.setOutputSpeech(outputSpeech);
                	SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
                	return response;
        		}
        	}else {
        		
        		String prefix = "<speak><p><prosody pitch=\"high\"><prosody rate=\"x-slow\">Ah</prosody></prosody>... <prosody pitch=\"low\"><prosody rate=\"fast\">No</prosody></prosody>. <break time=\"2s\"/>" + 
        		"<prosody pitch=\"high\">Just</prosody><prosody rate=\"slow\"><prosody pitch=\"high\">kidding</prosody></prosody>, here is your <prosody rate=\"slow\"><prosody pitch=\"medium\">story</prosody></prosody>.</p> ";
        		
        		session.setAttribute(STAGE_NAME, ASK_NAME_STAGE);
        		
        		String storyName = storySlot.getValue();
    			
    			session.setAttribute(STORY_ID, storyName);
				
	            // get the story content for the corresponding story
	            storyContent = prefix + Stories.getFableWithName(storyName).getStoryContent();
		        
	            // create an appropriate SSML response
		        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
		        outputSpeech.setSsml(storyContent);
		
		        Reprompt reprompt = new Reprompt();
		        reprompt.setOutputSpeech(null);
		        SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
		        return response;
        	}
        }else {
        	
        	// if the story requested by the user could not be found, create an appropriate response
        	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        	outputSpeech.setText("Sorry, I couldn't find the story you requested. Please try naming another story.");
        	Reprompt reprompt = new Reprompt();
        	reprompt.setOutputSpeech(outputSpeech);
        	SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
        	return response;
        }
    }

    /**
     * Tells the moral of the story requested by the user
     *
     * @param intent
     *            the intent object
     * @param session
     *            the session object
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse handleTellMeTheMoralIntent(Intent intent, final Session session) {
    	
    	String storyMoral = ""; 
        
    	// get the name of the story from the custom slot (within the JSON request)
        Slot storySlot = intent.getSlot(CUSTOM_SLOT_STORY_NAME);
        
        if (storySlot != null && storySlot.getValue() != null && Stories.getFableWithName(storySlot.getValue()) != null) {
        	
        	if(session.getAttributes().containsKey(STAGE_NAME)) {
        		
        		if(session.getAttributes().containsKey(STORY_ID)) {
        			
        			if(storySlot.getValue().equals((String)session.getAttribute(STORY_ID))) {
        				
        				if((Integer)session.getAttribute(STAGE_NAME) == BEGINNING_STAGE) {
                			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
                        	outputSpeech.setText("You haven't tell me the story yet! Just say tell me a story of blah blah blah. Now, which story you want to hear?");
                        	Reprompt reprompt = new Reprompt();
                        	reprompt.setOutputSpeech(outputSpeech);
                        	SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
                        	return response;
                		}else if((Integer)session.getAttribute(STAGE_NAME) == ASK_NAME_STAGE) {
                			String storyName = storySlot.getValue();
                            
                			session.setAttribute(STAGE_NAME, BEGINNING_STAGE);
                			
                            // get the story content for the corresponding story
                            storyMoral = Stories.getFableWithName(storyName).getStoryMoral();
                	        
                            // create an appropriate SSML response
                	        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
                	        outputSpeech.setSsml(storyMoral);
                	
                	        Reprompt reprompt = new Reprompt();
                	        reprompt.setOutputSpeech(null);
                	        SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
                	        return response;
                		}else {
                        	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
                        	outputSpeech.setText("Sorry, I do not know what you are asking about.");
                        	Reprompt reprompt = new Reprompt();
                        	reprompt.setOutputSpeech(outputSpeech);
                        	SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
                        	return response;
                		}
        				
        				/* PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
                    	outputSpeech.setText("Sorry, I can only tell the moral after I say the story. " + 
        				"And I have not yet told you that story. If you want to hear it, just say tell me the story of blah blah blah.");
                    	Reprompt reprompt = new Reprompt();
                    	reprompt.setOutputSpeech(outputSpeech);
                    	SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
                    	return response;*/
        			}
	
        			
        			
        	}else {
        		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
            	outputSpeech.setText("You have not asked me to tell a story!");
            	Reprompt reprompt = new Reprompt();
            	reprompt.setOutputSpeech(outputSpeech);
            	SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
            	return response;
        	}       
        } else {
        	
        	// if the story requested by the user could not be found, create an appropriate response
        	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        	outputSpeech.setText("Just opened the app right? No worries, you will get more adapt while you use more. " +
        			"So you haven't tell me the story yet! Just say tell me a story of blah blah blah. Now, which story you want to hear?");
        	Reprompt reprompt = new Reprompt();
        	reprompt.setOutputSpeech(outputSpeech);
        	SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
        	return response;
        	}
        }else {
        	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        	outputSpeech.setText("Sorry, I couldn't find the moral of the story you requested. Please try naming another story.");
        	Reprompt reprompt = new Reprompt();
        	reprompt.setOutputSpeech(outputSpeech);
        	SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
        	return response;
        }
        
        return null;
    }
    
}
