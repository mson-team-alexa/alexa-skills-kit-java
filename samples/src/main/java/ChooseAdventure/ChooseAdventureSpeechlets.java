package ChooseAdventure;
/**
Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at
    http://aws.amazon.com/apache2.0/
or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/

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
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

/**
* This sample shows how to create a simple speechlet for handling intent requests and managing
* session interactions.
*/
public class ChooseAdventureSpeechlets implements Speechlet {
private static final Logger log = LoggerFactory.getLogger(ChooseAdventureSpeechlets.class);

private static final String COLOR_KEY = "LIST_OF_STORY_NAMES";
private static final String COLOR_SLOT = "StoryName";



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

    // Get intent from the request object.
    Intent intent = request.getIntent();
    String intentName = (intent != null) ? intent.getName() : null;

    // Note: If the session is started with an intent, no welcome message will be rendered;
    // rather, the intent specific response will be returned.
    if ("TellMeMyStoryIntent".equals(intentName)) {
        return setColorInSession(intent, session);
    } else if ("ContinueIntent".equals(intentName)) {
        return getColorFromSession(intent, session);
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
 * @return SpeechletResponse spoken and visual welcome message
 */










private SpeechletResponse getWelcomeResponse() {
    // Create the welcome message.
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
 	                + " Blast off in 5...4.3...2...1...BLAST OFF! It's 200 days since you traveled into space. "
 	                + "There’s been minimal contact from your superiors as you travel deeper into space, waiting for your boss to give you your next task. "
 	                + "Suddenly, you hear a beeping noise- it’s a message from your boss! He’s sent you the coordinates of a suspected black hole and wants you to investigate."
 	                + " However, you have a feeling that there’s something hiding in space, waiting for you to find it.."
 	                + " The choice is yours, commander" 
 	                + "To follow the coordinates, say Follow. To follow your gut and go another way, say My Way."
 	                ;

    return getSpeechletResponse(speechText, repromptText, true);
}




///basically I realized what I did wasn't working. So I scrapped all of it and thought that I could use a bunch of if statements to achieve the same result. I copied what we did in sessions and made it fit with my code.
// I know I should have not have done this, but the code from the other files (scenario, stories, adventures) did not fully make sense. But this makes sense. And it use sessions and the custom slots. The output sounds bc I didn't have
// enough time to put SSML, but I finished all the logic code.




/**
 * Creates a {@code SpeechletResponse} for the intent and stores the extracted color in the
 * Session.
 *
 * @param intent
 *            intent for the request
 * @return SpeechletResponse spoken and visual response the given intent
 */
private SpeechletResponse setColorInSession(final Intent intent, final Session session) {
    // Get the slots from the intent.
    Map<String, Slot> slots = intent.getSlots();

    // Get the color slot from the list of slots.
    Slot favoriteColorSlot = slots.get(COLOR_SLOT);
    String speechText = "apples";
    String repromptText;

    // Check for favorite color and create output to user.
    if (favoriteColorSlot != null) {
        // Store the user's favorite color in the Session and create response.
        String favoriteColor = favoriteColorSlot.getValue();
        session.setAttribute(COLOR_KEY, favoriteColor);
        log.info(favoriteColor);
     if(favoriteColor.equals("follow")) 
        	speechText = String.format("You set the rocket to autopilot, heading towards the coordinates. It takes a few days, "
        			+" but eventually you reach the coordinates and see that your boss’s guess was right: "
        			+ "there is a block hole here. You decide to play it safe before jumping into the hole, "
        			+"but you’re not sure what to do first. Should you see if there’s any planets or life nearby?" 
        			+ "The choice is yours, commander:" + 
        			"	To scan the black hole for life, say Scan it." + 
        			"	To orbit the black hole and search for nearby planets, say Search it");
     else if(favoriteColor.equals("my way"))
		 speechText = String.format("You stick with your gut and turn around the ship, piloting it into the abyss. "
    			+"Your boss calls you, furious, ordering you to turn around, but you ignore him, "
    			+"following your instinct. You’re the one in space anyway- what say does he have? "
    			+ "As you fly into the unknown, your ship suddenly loses power. You stand up, "
    			+"heading to fix the power, but one monitor flickers on, displaying a message in a "
    			+"language you’ve never seen before. Your curiosity spikes- you want to translate the message, "
    			+"but you know you need to fix the power before your oxygen runs out…”" + 
    			"The choice is yours, commander:" + 
    			"	To translate the message, say “Translate it”" + 
    			"	To fix the power, say “Fix it");
     
     
     if(favoriteColor.equals("scan it"))
		 speechText = String.format("You scan the black hole. The results come up negative- "
    			+"there is no life in the hole. As soon as you get the results, "
    			+"the power goes out and you see a message saying that the engine has lost power. "
    			+"Your gut begins to sink as you feel the ship being pulled into black hole."
    			+"Without the engines, you will surely die, but you wonder what caused the engines to fail…" + 
    			"The choice is yours, commander:" + 
    			"	To fix the engines, say Fix" + 
    			"	To figure out why the engines lost power, say Engines" );
    else if(favoriteColor.equals("search it"))
		 speechText =String.format( "You orbit the black hole, looking for nearby planets. After a few hours, "
    			+"you see a big purple sphere in the distance- could it be a new planet? "
    			+"You steer towards the object and realize that you have found a new planet! "
    			+"As you record the planet’s coordinates in your log, you begin to wonder- "
    			+"could there be life forms on this planet? Could there be more planets out there?" + 
    			"The choice is yours, commander:" + 
    			"	To scan the planet for life, say Scan" + 
    			"	To search for more planets, say Search");
     
     
     if(favoriteColor.equals("translate it"))
		 speechText =     String.format("You decide to translate the message- contact with extraterrestrial life "
    			+"is far more important than the ship’s power. It’s takes some time, "
    			+"but eventually you think you’ve got some of the message. You can make out two words: "
    			+"AIM and DANGER. Your gut sinks- are the aliens hostile? Are they threatening you, "
    			+"or are they warning you? You realize you have two options: fix the power and escape from the aliens, "
    			+"or find out who sent the message and let them know you only want peace." + 
    			"The choice is yours, commander:" + 
    			"	To fix the power and escape, say Fix the thing" + 
    			"	To send a peace letter to the aliens, say Letter") ;
    else if(favoriteColor.equals("fix it"))
		 speechText =String.format( "You decide to ignore the message and fix the power. It takes some time, "
    			+"but eventually the lights turn back on and you feel the engine shaking the ship. "
    			+"However, seconds after you fix the power, the ship’s alarm goes off. You’re under attack! "
    			+"You begin to panic- do you try and find the threat or use an escape pod to head back to Earth?" + 
    			"The choice is yours, commander:" + 
    			"	To find the threat, say Find the thing" + 
    			"	To leave in the escape pod, say Leave thing");
     
     
     
     if(favoriteColor.equals("fix"))
		 speechText =String.format( "You successfully fix the engines, but it's too late: while you were working,  "
	    			+"your ship was pulled into the black hole. You quickly realize there is no"
	    			+" escape as you sink deeper into nothingness."
	    			+ "GAME OVER. Thank you for playing!");
   
   else if(favoriteColor.equals("engines"))
		 speechText =String.format( "Just as you start to invesitagte the power loss, you see a bright light coming through the windows. " + 
	    			"Just as quickly, a loud message begins to play: Do not be alarmed human, the voice says, "
	    			+"we know you seek the answers behind the black holes, we are docking your ship now. "
	    			+"Come aboard, and we will give you shelter and share our knowledge. "
	    			+"Congratulations, commander! You've made contact with a friendly alien species and are learning their knowledge." + 
	    			"The mission was an amazing success! "
	    			+"GAME OVER. Thank you for playing!");
     
     
     
     
   
    if(favoriteColor.equals("scan"))
		 speechText =String.format("You scan the planet and the scans come back positive! However, you realize the planet is slowly"
	    			+"sinking into the black hole- you have to help them before the aliens are consumed by the black hole! "
	    			+"You quickly land on the planet, open your doors, and let in every living alien in sight. "
	    			+"Just before the planet is absorbed into the black hole, you launch back into space." + 
	    			"Congratulations, commander! You discovered and saved an alien species! In honor of your heroicness, "
	    			+"there is a statue of you on Earth and on the new aliens’ planet!" + 
	    			"GAME OVER. Thank you for playing!");
   
   else if(favoriteColor.equals("search"))
		 speechText =String.format( "You fail to find any planets. You turn around to go back to investigate the one you found earlier, "
	    			+"but when you get there, the planet has vanished. You begin to think...could it have been swallowed by the black hole?"
	    			+"Finding nothing, you steer away from the black hole, going back out into space to wait for the next orders."
	    			+ "	GAME OVER. Thank you for playing!" );
    
    
    
    
   
    if(favoriteColor.equals("fix the thing"))
		 speechText =String.format( "Just as soon as you fix the power, you see a fleet of ships approaching. You immediately recognize "
	    			+"them as American Interglactic Police spacecrafts- your boss must have sent them after you! You rush to the controls, "
	    			+" and initiate light speed. It will use up all of the remaining fuel, but they’ll never catch you!"
	    			+"GAME OVER. Thank you for playing!");
   
   
   else if(favoriteColor.equals("letter"))
		 speechText =String.format( "After you send your letter, you see a bright light coming through the windows." + 
	    			"A loud message begins to play: Do not be alarmed human, the voice says," + 
	    			"We have received your message. We know you seek the answers behind the black holes, we are docking your ship now." + 
	    			"Come aboard, and we will give you shelter and share our knowledge." + 
	    			"Congratulations, commander! You've made contact with a friendly alien species and are learning their knowledge." + 
	    			"The mission was an amazing success!"
	    			+ "	GAME OVER. Thank you for playing!");
    
    
    
    
    
   
    if(favoriteColor.equals("find the thing"))
		 speechText =String.format("You look outside the window to see a fleet of ships approaching. You immediately recognize \"\n" + 
	    			"them as American Interglactic Police spacecrafts- your boss must have sent them after you! You rush to the controls, \"\n" + 
	    			"and are about to initiate light speed before you hear the looming voice of your boss. "
	    			+"COMMANDER, THIS IS ADMIRAL BRIG, WE ARE BOARDING YOUR SHIP TO RETURN YOU TO EARTH ON THE CHARGES OF MUTINY."
	    			+"ACCEPT YOUR SURRENDER OR WE WILL DESTROY YOUR SHIP WITH YOU IN IT."
	    			+"You are arrested and sent back to Earth where you spend the rest of your days in prison, dreaming of the stars."
	    			+ "	GAME OVER. Thank you for playing!");
  
   else if(favoriteColor.equals("leave the thing"))
		 speechText =String.format( "You run down to the lower deck where the escape pods are held. The alarms get quieter as you enter the"
	    			+"pod. You buckle yourself in and press the big red eject button. You launch into space, heading towards Earth."
	    			+"Who knows what adventures might ensue next....TO BE CONTINUED"
	    			+ "	GAME OVER. Thank you for playing!");
  
//        speechText =
//                String.format("Yes commander, say next and we will go and " + favoriteColor);
        repromptText =
                "Tell me your choice";

    } else {
        // Render an error since we don't know what the users favorite color is.
        speechText = "I'm not sure what you said, please try again";
        repromptText =
                "I'm not sure what you said";
    }

    return getSpeechletResponse(speechText, repromptText, true);
}
private SpeechletResponse getColorFromSession(final Intent intent, final Session session) {
    String speechText;
    boolean isAskResponse = false;

    // Get the user's favorite color from the session.
    String favoriteColor = (String) session.getAttribute(COLOR_KEY);

    // Check to make sure user's favorite color is set in the session.
    if(favoriteColor.equals("follow")) {
    	speechText = String.format("You set the rocket to autopilot, heading towards the coordinates. It takes a few days, "
    			+" but eventually you reach the coordinates and see that your boss’s guess was right: "
    			+ "there is a black hole here. You decide to play it safe before jumping into the hole, "
    			+"but you’re not sure what to do first. Should you see if there’s any planets or life nearby?" 
    			+ "The choice is yours, commander:" + 
    			"	To scan the black hole for life, say Scan." + 
    			"	To orbit the black hole and search for nearby planets, say Search");
		log.info("This is after one " + favoriteColor);
    }
    else if(favoriteColor.equals("my way"))
		 speechText = String.format("You stick with your gut and turn around the ship, piloting it into the abyss. "
    			+"Your boss calls you, furious, ordering you to turn around, but you ignore him, "
    			+"following your instinct. You’re the one in space anyway- what say does he have? "
    			+ "As you fly into the unknown, your ship suddenly loses power. You stand up, "
    			+"heading to fix the power, but one monitor flickers on, displaying a message in a "
    			+"language you’ve never seen before. Your curiosity spikes- you want to translate the message, "
    			+"but you know you need to fix the power before your oxygen runs out…”" + 
    			"The choice is yours, commander:" + 
    			"	To translate the message, say “Translate”" + 
    			"	To fix the power, say “Fix");
    else if(favoriteColor.equals("scan"))
		 speechText = String.format("You scan the black hole. The results come up negative- "
    			+"there is no life in the hole. As soon as you get the results, "
    			+"the power goes out and you see a message saying that the engine has lost power. "
    			+"Your gut begins to sink as you feel the ship being pulled into black hole."
    			+" Without the engines, you will surely die, but you wonder what caused the engines to fail…" + 
    			"The choice is yours, commander:" + 
    			"	To fix the engines, say Fix" + 
    			"	To figure out why the engines lost power, say Engines" );
    else if(favoriteColor.equals("search"))
		 speechText =String.format( "You orbit the black hole, looking for nearby planets. After a few hours, "
    			+"you see a big purple sphere in the distance- could it be a new planet? "
    			+"You steer towards the object and realize that you have found a new planet! "
    			+"As you record the planet’s coordinates in your log, you begin to wonder- "
    			+"could there be life forms on this planet? Could there be more planets out there?" + 
    			"The choice is yours, commander:" + 
    			"	To scan the planet for life, say Scan" + 
    			"	To search for more planets, say Search");
    else if(favoriteColor.equals("translate"))
		 speechText =     String.format("You decide to translate the message- contact with extraterrestrial life "
    			+"is far more important than the ship’s power. It’s takes some time, "
    			+"but eventually you think you’ve got some of the message. You can make out two words: "
    			+"AIM and DANGER. Your gut sinks- are the aliens hostile? Are they threatening you, "
    			+"or are they warning you? You realize you have two options: fix the power and escape from the aliens, "
    			+"or find out who sent the message and let them know you only want peace." + 
    			"The choice is yours, commander:" + 
    			"	To fix the power and escape, say Fix" + 
    			"	To send a peace letter to the aliens, say Letter") ;
    else if(favoriteColor.equals("fix"))
		 speechText =String.format( "You decide to ignore the message and fix the power. It takes some time, "
    			+"but eventually the lights turn back on and you feel the engine shaking the ship. "
    			+"However, seconds after you fix the power, the ship’s alarm goes off. You’re under attack! "
    			+"You begin to panic- do you try and find the threat or use an escape pod to head back to Earth?" + 
    			"The choice is yours, commander:" + 
    			"	To find the threat, say Find" + 
    			"	To leave in the escape pod, say Leave");

  

     else {
        // Since the user's favorite color is not set render an error message.
    	 speechText =
                "I'm not sure what you said";
    	 log.info("This is end" + favoriteColor);

        isAskResponse = true;
    }

    return getSpeechletResponse(speechText, speechText, isAskResponse);
}

/**
 * Creates a {@code SpeechletResponse} for the intent and get the user's favorite color from the
 * Session.
 *
 * @param intent
 *            intent for the request
 * @return SpeechletResponse spoken and visual response for the intent
 */


/**
 * Returns a Speechlet response for a speech and reprompt text.
 */
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
        // Create reprompt
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
