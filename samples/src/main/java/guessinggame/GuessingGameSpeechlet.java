package guessinggame;

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
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;


public class GuessingGameSpeechlet implements Speechlet {
private static final Logger log = LoggerFactory.getLogger(GuessingGameSpeechlet.class);

/**
 * Custom Slot key for the name animal given by the user
 */
private static final String ANIMALS = "Animals";


/*This will need to be replaced with sessions*/
public String correctAnimal = "cat";
public int numPoints = 10;
public int numStrikes = 0;
public int whichAnimal = 2;
String[] animalsounds = new String[] {"https://s3.amazonaws.com/final-project-mson/pMCJLWNd-giantanteater.mp3","https://s3.amazonaws.com/final-project-mson/ORw3KFcz-ee89c132fbf64a7cbb5ac65df7f7b5fb-hippo-001.mp3","https://s3.amazonaws.com/final-project-mson/EuzargZH-cat-meow-2-cat-stevens-2034822903.mp3"};

/**/


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
    return getWelcomeResponse();
}

@Override
public SpeechletResponse onIntent(final IntentRequest request, final Session session)
        throws SpeechletException {
    log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
            session.getSessionId());

    Intent intent = request.getIntent();
    String intentName = (intent != null) ? intent.getName() : null;

    //change based on intents
    if ("PlayGameIntent".equals(intentName)) {
        return playGameResponse();
    }else if("GuessAnimalIntent".equals(intentName)) {
    		return handleGuessTheAnimal(intent, session);
	}else if ("AMAZON.HelpIntent".equals(intentName)) {
        return getHelpResponse();
    } else {
        throw new SpeechletException("Invalid Intent");
    }
}

@Override
public void onSessionEnded(final SessionEndedRequest request, final Session session)
        throws SpeechletException {
    log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
            session.getSessionId());
    // any cleanup logic goes here
}

/**
 * Creates and returns a {@code SpeechletResponse} with a welcome message.
 *
 * @return SpeechletResponse spoken and visual response for the given intent
 */
private SpeechletResponse getWelcomeResponse() {
    String speechText = "Welcome to the Guessing Game. You can say, \"Let's play.\"";

    // Create the Simple card content.
    SimpleCard card = new SimpleCard();
    card.setTitle("Welcome");
    card.setContent(speechText);

    // Create the plain text output.
    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
    speech.setText(speechText);

    // Create reprompt
    Reprompt reprompt = new Reprompt();
    reprompt.setOutputSpeech(speech);

    return SpeechletResponse.newAskResponse(speech, reprompt, card);
}

/**
 * Creates a {@code SpeechletResponse} for the help intent.
 *
 * @return SpeechletResponse spoken and visual response for the given intent
 */
private SpeechletResponse getHelpResponse() {
    String speechText = "In this game, I will play a sound and you will try to guess the animal that made that sound. To start, say \"Let's play\"";

    // Create the Simple card content.
    SimpleCard card = new SimpleCard();
    card.setTitle("Help");
    card.setContent(speechText);

    // Create the plain text output.
    PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
    speech.setText(speechText);

    // Create reprompt
    Reprompt reprompt = new Reprompt();
    reprompt.setOutputSpeech(speech);

    return SpeechletResponse.newAskResponse(speech, reprompt, card);
}

private SpeechletResponse playGameResponse() {
    String speechText = "<speak> Guess the animal that makes this sound: "
    		+ " <audio src=\"" + animalsounds[2] + "\"/> </speak>";
    
    //Create reprompt
    SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
    outputSpeech.setSsml(speechText);

    Reprompt reprompt = new Reprompt();
    reprompt.setOutputSpeech(outputSpeech);
    SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    return response;
}

private SpeechletResponse handleGuessTheAnimal(Intent intent, final Session session) {
	// get the type of animal guessed
    Slot animalSlot = intent.getSlot(ANIMALS);
    
    String speech = "";
    if (animalSlot != null && animalSlot.getValue() != null) {
        String animalName = animalSlot.getValue();
        
        //If the animal guessed is correct
        if(animalName == correctAnimal) {
        		speech = "Good job! That's correct.";
        		numPoints += 10;
        		//If you've gotten enough points to level up
                if(numPoints == 10 || numPoints == 20) {
                		speech+="Nice work! You've leveled up! Now the animal sounds will be harder to guess.";
                }
        		playGameResponse();
        }
        //If the animal guessed is incorrect
        if(animalName != correctAnimal) {
        		speech= "Sorry, That's not correct.";
        		numStrikes++;
        		 //If you've gotten three strikes
                if(numStrikes == 3) {
                		speech+="Game over! You got " + numPoints + " points.";
                }
                else
                		playGameResponse();
        }
        
        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        outputSpeech.setSsml(speech);
        
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(null);
        SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
        return response;
        
    }
    else {
    	// Create the plain text output.
        
        speech = "Sorry, I've never heard of that animal.";

        SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
        outputSpeech.setSsml(speech);
        
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(null);
        SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
        return response;
    }
}
}
