package guessinggame;

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
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;


public class GuessingGameSpeechlet implements Speechlet {
private static final Logger log = LoggerFactory.getLogger(GuessingGameSpeechlet.class);

/**
 * Custom Slot key for the name animal given by the user
 */
private static final String ANIMALS = "animal";


/*This will need to be replaced with sessions*/
public String correctAnimal = "correctAnimal";
public String numPoints = "points";
public String numStrikes = "strikes";
public int whichAnimal = 2;

// create lists of sounds of each difficulty
private static final ArrayList<Sound> EASY_SOUNDS = new ArrayList<Sound>();
private static final ArrayList<Sound> MEDIUM_SOUNDS = new ArrayList<Sound>();
private static final ArrayList<Sound> HARD_SOUNDS = new ArrayList<Sound>();

// adding sounds to lists
static {
EASY_SOUNDS.add(new Sound("https://s3.amazonaws.com/final-project-mson/EuzargZH-cat-meow-2-cat-stevens-2034822903.mp3", "meow!", "cat"));
MEDIUM_SOUNDS.add(new Sound("https://s3.amazonaws.com/final-project-mson/ORw3KFcz-ee89c132fbf64a7cbb5ac65df7f7b5fb-hippo-001.mp3", "URGHJGJYGJGHHHGHH", "hippo"));
HARD_SOUNDS.add(new Sound("https://s3.amazonaws.com/final-project-mson/pMCJLWNd-giantanteater.mp3", "SHHHRRRHRHHRH", "giant anteater"));
}

//String[] animalsounds = new String[] {"https://s3.amazonaws.com/final-project-mson/pMCJLWNd-giantanteater.mp3","https://s3.amazonaws.com/final-project-mson/ORw3KFcz-ee89c132fbf64a7cbb5ac65df7f7b5fb-hippo-001.mp3","https://s3.amazonaws.com/final-project-mson/EuzargZH-cat-meow-2-cat-stevens-2034822903.mp3"};

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
    		// The reason these are set here is bc playGameResponse is called again after each level
    		session.setAttribute(numPoints, 0);
    		session.setAttribute(numStrikes, 0);
        return playGameResponse(session);
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

private guessinggame.GuessingGameSpeechlet.Sound getSound(final Session session) {
	int points = (int) session.getAttribute(numPoints);
	//String animalSound = null;
	if(points == 0) {
		int index = (int) Math.floor(Math.random() * EASY_SOUNDS.size());
		return EASY_SOUNDS.get(index);
	} else if(points == 10){
		int index = (int) Math.floor(Math.random() * MEDIUM_SOUNDS.size());
		return MEDIUM_SOUNDS.get(index);
	} else if(points == 20) {
		int index = (int) Math.floor(Math.random() * HARD_SOUNDS.size());
		return HARD_SOUNDS.get(index);
	}
	return null;
}


private SpeechletResponse playGameResponse(final Session session) {
	
	Sound animalSound = getSound(session);
	session.setAttribute(correctAnimal, animalSound.animal_name);
    String speechText = "<speak> Guess the animal that makes this sound: " + " <audio src=\"" + animalSound.mp3_link + "\"/> </speak>";
    
    //Create reprompt
    SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
    outputSpeech.setSsml(speechText);

    Reprompt reprompt = new Reprompt();
    reprompt.setOutputSpeech(outputSpeech);
    SpeechletResponse response = SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    return response;
}


private static class Sound {
	
    private final String mp3_link;
    //private final String cardRepresentation;
	private final String animal_name;
    	
    // takes a direct mp3 link and text representation of a sound
    Sound(String mp3_link, String cardRepresentation, String animal_name) {
        this.mp3_link = mp3_link;
        //this.cardRepresentation = cardRepresentation;
        this.animal_name = animal_name;
    }
}	

private SpeechletResponse handleGuessTheAnimal(Intent intent, final Session session) {
	// get the type of animal guessed
    Slot animalSlot = intent.getSlot(ANIMALS);
    String animal = (String) session.getAttribute(correctAnimal);
    int totalPoints = (int) session.getAttribute(numPoints);
    int totalStrikes = (int) session.getAttribute(numStrikes);
    String speech = "";
    if (animalSlot != null && animalSlot.getValue() != null) {
        String animalName = animalSlot.getValue();

        //If the animal guessed is correct
        if(animalName.equals(animal)) {
        		speech = "Good job! That's correct.";
        		totalPoints += 10;
        		session.setAttribute(numPoints, totalPoints);
        		//If you've gotten enough points to level up
                if(totalPoints == 10 || totalPoints == 20) {
                		speech+="Nice work! You've leveled up! Now the animal sounds will be harder to guess.";
                }
        		playGameResponse(session);
        }
        //If the animal guessed is incorrect
        if(!(animalName.equals(animal))) {
        		speech= "Sorry, That's not correct.";
        		totalStrikes++;
        		session.setAttribute(numStrikes, totalStrikes);
        		 //If you've gotten three strikes
                if(totalStrikes == 3) {
                		speech+="Game over! You got " + numPoints + " points.";
                }
                else
                		playGameResponse(session);
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
