package fastmath;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class GetHelp {

	String template =  "There are something wrong with your answer. Please keep in mind that you have to say continue every time you enter a new mode. " +
            "You will have to provide one level by saying level and level number for the practice mode, and you will have to provide a level range " +
            "of two numbers within five for the challenger stage. You will not need to say any decimal answers so please do not include point " +
            "in your answer. If you want to quit playing the current mode, just say quit mode or cancel. You are free to change your level in " +
            "practice mode, but you won't be able to do this in survival or challenger mode unless you quit and reenter.You can ask me to repeat " + 
            "the question in Practice and Challenger mode, but you can not do this in survival mode.   What would you want to do now?";;
	
	private static String speechText, repromptText;
	
	public GetHelp(){

	}
	
	public String getTemplate() {
		return template;
	}

	public SpeechletResponse notHavingAnswer() {
		speechText = "You can not give answer right now! You have not confirmed a play mode," + 
					" or you have not provided me with enough information!";
		
		repromptText = "Have you sought out the problem? ";
		
		return getSpeechletResponse(speechText, repromptText, true);
	}
	
	public SpeechletResponse notAskingAnything() {
		speechText = "You have not asked anything from me yet!";
		
		repromptText = "What mode would you want to play? ";
		
		return getSpeechletResponse(speechText, repromptText, true);
	}
	
	public SpeechletResponse noSuchMode() {
		speechText = "There is no such mode that you are asking for. You can only choose from survival, practice, or challenger. ";
		
		repromptText = "What mode would you want to play? ";
		
		return getSpeechletResponse(speechText, repromptText, true);
	}
	
	public SpeechletResponse notAskingMode() {
		speechText = "You have not answered a mode yet! ";
		
		repromptText = "What mode would you want to play? ";
		
		return getSpeechletResponse(speechText, repromptText, true);
	}
	
	public SpeechletResponse notNeedingContinue() {
		speechText = "You do not need to say continue in this mode! ";
		
		repromptText = "Are you still there? ";
		
		return getSpeechletResponse(speechText, repromptText, true);
	}
	
	public SpeechletResponse notNeedingAskingRange() {
		speechText = "You do not need to answer the range in this mode! ";
		
		repromptText = "Are you still there? ";
		
		return getSpeechletResponse(speechText, repromptText, true);
	}
	
	public SpeechletResponse cantAskRepeat() {
		speechText = "You can not ask me to repeat the question in survival mode! You have exceeded the time, please say continue to proceed. ";
		
		repromptText = "Are you still there? ";
		
		return getSpeechletResponse(speechText, repromptText, true);
	}
	
	public SpeechletResponse cantAskLevel() {
		speechText = "You can not ask me to set the level in this mode! ";
		
		repromptText = "Are you still there? ";
		
		return getSpeechletResponse(speechText, repromptText, true);
	}
	
	private SpeechletResponse getSpeechletResponse(String speechText, String repromptText,
            boolean isAskResponse) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        if (isAskResponse) {
            PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
            repromptSpeech.setText(repromptText);
            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(repromptSpeech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);

        } else {
            return SpeechletResponse.newTellResponse(speech, card);
        }
    }
}
