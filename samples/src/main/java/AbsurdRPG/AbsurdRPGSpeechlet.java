/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package AbsurdRPG;

import java.util.ArrayList;

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
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;

import AbsurdRPG.CustomClasses.AbsurdRPG;
import AbsurdRPG.CustomClasses.Actions;
import AbsurdRPG.CustomClasses.Conversations;
import AbsurdRPG.CustomClasses.Quests;
import AbsurdRPG.CustomClasses.Location.Locations;
import AbsurdRPG.CustomClasses.Inventory.*;

import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class AbsurdRPGSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(AbsurdRPGSpeechlet.class);
    
    private static final String TUTORIAL = "Tutorial";
    
    private static final String ACTIVE_QUEST_ID = "ActiveQuestID";
    
    private static final String SUBJECT_ID = "SubjectID";
    
    private static final String MAIN_SESSION_ID = "MainSessionID";

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        session.setAttribute(MAIN_SESSION_ID, "Beginning");
        
        session.setAttribute(SUBJECT_ID, null);
        
        Quests.Initialize();
        
        Locations.Populate();
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

        if(((String)session.getAttribute(MAIN_SESSION_ID)).equals("Beginning")){

        	if(session.getAttributes().containsKey(TUTORIAL)) {
        		if(((String)session.getAttribute(TUTORIAL)).equals("Talk")){
        			if("InitiateConversationIntent".equals(intentName)) {	
        				session.setAttribute(TUTORIAL, "ChooseSubject");
        				session.setAttribute("TalkWithHeadOfVillage", 0);
        				return AbsurdRPG.getSpeechletResponseBySession(session);
        			}else {
        				String speechText = Conversations.tutorial_wanring_talk;
        				
        				return newAskResponse(speechText, false, speechText, false);
        			}
        		}else if(((String)session.getAttribute(TUTORIAL)).equals("ChooseSubject")) {
        			if("ChooseSubjectIntent".equals(intentName)) {
        				Slot nameSlot = intent.getSlot("NPC_name");
        				
            			if(nameSlot != null && nameSlot.getValue() != null) {
            				
            				if(Actions.checkSubject(nameSlot.getValue()) != null) {
            					
            					if(nameSlot.getValue().equals(Locations.DownhillVillage.returnPopulation().get(0).name)) {
            						session.setAttribute("TalkWithHeadOfVillage", 1);
            					}
            					
            					session.setAttribute(SUBJECT_ID, nameSlot.getValue());
            					
            					SpeechletResponse sR = AbsurdRPG.getSpeechletResponseBySession(session);
                				
            					if(((Integer)session.getAttribute("TalkWithHeadOfVillage")) == 1) {
            						session.removeAttribute("TalkWithHeadOfVillage");
            						
                    				session.setAttribute(TUTORIAL, "GoOut");
                    			}else {
                    				session.setAttribute(TUTORIAL, "Talk");
                    			}
            					
            					return sR;
            					
            				}else {
            					return AbsurdRPG.getSpeechletResponseBySession(session);
            				}
            			}else {
            				String speechText = Conversations.tutorial_invalid_character;
    						
    						return newAskResponse(speechText, false, speechText, false);
            			}
        			}else {
        				String speechText = Conversations.tutorial_wanring_talk;
        				
        				return newAskResponse(speechText, false, speechText, false);
        			}
        		}
        		else if(((String)session.getAttribute(TUTORIAL)).equals("GoOut")) {
        			return null;
        		}else {
        			return null;
        		}
        	}else {
        		if ("SkipTutorialIntent".equals(intentName)) {
                	session.setAttribute(TUTORIAL, "Confirm");
                	
                	return null;
                }else if("TakeTutorialIntent".equals(intentName)){
                	String speechText = Conversations.take_tutorial;
                	
                	session.setAttribute(TUTORIAL, "Talk");
                	
                	return newAskResponse(speechText,false, speechText, false);
                }else {
                    throw new SpeechletException("Invalid Intent");
                }
        	}
        }else {
        	return null;
        }
    }
    

    public static Intent getSpeechletIntent(Intent intent) {
    	return intent;
    }
    
    private SpeechletResponse getWelcomeResponse() {
        // Create the welcome message
        String speechText =
                "Welcome to the RPG! ";
        String repromptText =
                "Oh yeah?";

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
        repromptSpeech.setText(repromptText);
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptSpeech);
        
        return SpeechletResponse.newAskResponse(speech, reprompt);
    }
    
   
   
    private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
            String repromptText, boolean isRepromptSsml) {
    	
        OutputSpeech outputSpeech, repromptOutputSpeech;
        if (isOutputSsml) {
            outputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
        } else {
            outputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
        }

        if (isRepromptSsml) {
            repromptOutputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
        } else {
            repromptOutputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
        }
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }

	@Override
	public void onSessionEnded(SessionEndedRequest arg0, Session arg1) throws SpeechletException {
		// TODO Auto-generated method stub
		
	}

}
