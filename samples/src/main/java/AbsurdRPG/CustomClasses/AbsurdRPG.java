package AbsurdRPG.CustomClasses;

import java.util.ArrayList;

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

public final class AbsurdRPG {

	private static final Logger log = LoggerFactory.getLogger(AbsurdRPG.class);
	
	private static final String TUTORIAL = "Tutorial";
    
    private static final String MAIN_SESSION_ID = "MainSessionID";	

    private static final String SUBJECT_ID = "SubjectID";
    
	public static SpeechletResponse getSpeechletResponseBySession(final Session session) {
		if(session.getAttributes().containsKey(MAIN_SESSION_ID)) {
			if(((String)session.getAttribute(MAIN_SESSION_ID)).equals("Beginning")) {
				if(((String)session.getAttribute(TUTORIAL)).equals("Talk")){
					
					ArrayList<String> speechText = Actions.initiateTalk();
					
					return newAskResponse(speechText.get(0), false, speechText.get(1), false);
				}else if(((String)session.getAttribute(TUTORIAL)).equals("ChooseSubject")){
					if(Actions.checkSubject(getSubjectFromSession(session)) != null) {
						String speechText;
						String repromptText;
						
						if(((Integer)session.getAttribute("TalkWithHeadOfVillage")) == 1) {
	        				speechText = Conversations.getQuestConversationForDownhillVillage().get(0);
	        				
	        				//accept tutorial quest
							Actions.acceptQuest(speechText,Quests.Tutorial_quest, session);
							
	        				repromptText = "You can now go outside of the village by saying go to, and, location. Or Just say, go to quest location. ";
	        			}else {
	        				speechText = Conversations.getConversationsForDownhillVillage((String)session.getAttribute(SUBJECT_ID), session).get(0);
	        				
	        				repromptText = Conversations.getConversationsForDownhillVillage((String)session.getAttribute(SUBJECT_ID), session).get(1);
	        			}
						
						return newAskResponse(speechText, false, repromptText, false);	
					}else {
						String speechText = Conversations.tutorial_warning_choose_subject;
						
						return newAskResponse(speechText, false, speechText, false);
					}
				}else {
					return null;
				}
			}else {
				return null;
			}
		}else {
			return null;
		}
	}
    

    private static String getSubjectFromSession(final Session session) {
    	return (String)session.getAttribute("SubjectID");
    }
	
    private static SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
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
}


