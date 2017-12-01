package barthelper;


import java.util.Map;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

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
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

import session.SessionSpeechlet;

/**
 * This sample shows how to create a Lambda function for handling Alexa Skill requests
 * leverage an external API.  
 * 
 * This Skill retrieves information about the Bay Area Rapid Transit (BART) System.
 * 
 * Following could be an example of an interaction with Alexa:
 * 
 * <p>
 * User: "Alexa, ask BART Helper what the upcoming holidays are."
 * <p>
 * Alexa:"The upcoming 3 holidays are..."
 * 
 */

public class BartHelperSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(BartHelperSpeechlet.class);

    private static final String URL_PREFIX = "https://api.bart.gov/api/sched.aspx?json=y&";
    
    private static final String URL_TRAIN = "http://bartjsonapi.elasticbeanstalk.com/api/departures/civc";
    
    private static final String API_KEY = "MW9S-E7SL-26DU-VV8V";
    
    private static final int MAX_HOLIDAYS = 3;
    
    private static final String STATION_KEY = "STATION";
    private static final String STATION_SLOT = "Station";

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
        String intentName = intent.getName();

        if ("GetHolidaysIntent".equals(intentName)) {
        	try {
				return getBARTHolidays(intent);
			} catch (IOException e) {
				log.error("Holidays IO Error");
				e.printStackTrace();
				return getErrorResponse(intent);
			} catch (JSONException e) {
				log.error("Holidays JSON Error");
				e.printStackTrace();
				return getErrorResponse(intent);
			}
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse(intent);
        } else if ("AMAZON.StopIntent".equals(intentName)) {
            return getStopResponse(intent);
        } else if ("AMAZON.CancelIntent".equals(intentName)) {
            return getCancelResponse(intent);
        } else if ("WhatsMyStationIntent".equals(intentName)) {
            return getStationFromSession(intent, session);
        } else if ("MyStationIsIntent".equals(intentName)) {
            return setStationInSession(intent, session);
        } else if ("GetTrainTimesIntent".equals(intentName)) {
        	try {
				return getTrainTimes(intent);
			} catch (IOException e) {
				log.error("Train Times IO Error");
				e.printStackTrace();
				return getErrorResponse(intent);
			} catch (JSONException e) {
				log.error("Train Times JSON Error");
				e.printStackTrace();
				return getErrorResponse(intent);
			}
        }
        
        else {
            throw new SpeechletException("Invalid Intent");
        }
		
    }
    
    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // any session cleanup logic would go here
    }
     //PROJECT CODE***********************************************************************************************   
private SpeechletResponse getTrainTimes(Intent intent) throws IOException, JSONException {
    	
    	String origin = "civc";
    	String timeURL = URL_TRAIN;
    	
    	log.info("BART Time URL: " + timeURL);
    	
    	URL url = new URL(timeURL);
    	Scanner scan = new Scanner(url.openStream());
    	String timeOutput = new String();
    	while (scan.hasNext()) {
    		timeOutput += scan.nextLine();
    	}
    	scan.close();
    	
    	// build a JSON object
    	JSONObject output = new JSONObject(timeOutput);
    	
    	//get the results
    	
    	JSONArray etd = output.getJSONArray("etd");
    	
    	for(int i =0; i<etd.length(); i++){
    		JSONObject destinationInfo = etd.getJSONObject(i);
    		String destination = destinationInfo.getString("destination");
    		log.info("destination: " + destination);
    	}
    	
    /*
    	JSONObject dest = etd.getJSONObject(0);
    	
    	JSONArray estimate = dest.getJSONArray("estimate");
    	
    	for (int x=0; x<estimate.length(); x++){
    		JSONObject minuteInfo = estimate.getJSONObject(x);
    		String minutes = minuteInfo.getString("minutes");
    		log.info("minutes: " + minutes);
    		
    		String platform = minuteInfo.getString("platform");
    		log.info("platform: " + platform);    		
    	}
    	
    	JSONObject d = (JSONObject)etd.get(0);
    	JSONObject time = (JSONObject)estimate.get(0);
    	
    	
    	
    	String speechOutput = "The train going to " + d.getString("destination") + " leaves in " + time.getString("minutes") + "minutes";
    	
    	
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText(speechOutput);

        SimpleCard card = new SimpleCard();
        card.setTitle("Upcoming BART Times");
        card.setContent(speechOutput);

        return SpeechletResponse.newTellResponse(outputSpeech, card);
        */
    	return null;
	}
    
   
  //*************************************************************************************************************  

//LAB 7 STORING MY STATION**************************************************************************************************************
	private SpeechletResponse setStationInSession(final Intent intent, final Session session) {
	    // Get the slots from the intent.
	    Map<String, Slot> slots = intent.getSlots();
	
	    // Get the station slot from the list of slots.
	    Slot myStationSlot = slots.get(STATION_SLOT);
	    String speechText, repromptText;
	
	    // Check for station and create output to user.
	    if (myStationSlot != null) {
	        // Store the user's station in the Session and create response.
	        String myStation = myStationSlot.getValue();
	        session.setAttribute(STATION_KEY, myStation);
	        speechText =
	                String.format("I now know that your station is %s. You can ask me your "
	                        + "station by saying, what's my station?", myStation);
	        repromptText =
	                "You can ask me your station by saying, what's my station?";
	
	    } else {
	        // Render an error since we don't know what the users favorite color is.
	        speechText = "I'm not sure what your station is, please try again";
	        repromptText =
	                "I'm not sure what your station is. You can tell me your "
	                        + "station by saying, my station is Civic Center";
	    }
	
	    return getSpeechletResponse(speechText, repromptText, true);
	}

//==============
	private SpeechletResponse getStationFromSession(final Intent intent, final Session session) {
        String speechText;
        boolean isAskResponse = false;

        // Get the user's station from the session.
        String myStation = (String) session.getAttribute(STATION_KEY);

        // Check to make sure user's station is set in the session.
        if (StringUtils.isNotEmpty(myStation)) {
            speechText = String.format("Your station is %s. Goodbye.", myStation);
        } else {
            // Since the user's station is not set render an error message.
            speechText =
                    "I'm not sure what your station is. You can say, my station is "
                            + "Civic Center";
            isAskResponse = true;
        }

        return getSpeechletResponse(speechText, speechText, isAskResponse);
    }
//=================
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







//**************************************************************************************************************
    /**
     * Creates a {@code SpeechletResponse} for the GetHolidaysIntent.
     *
     * @param intent
     *            intent for the request
     * @return SpeechletResponse spoken and visual response for the given intent
     */
	private SpeechletResponse getBARTHolidays(Intent intent) throws IOException, JSONException {
    	
    	String command = "holiday";
    	String holidayURL = URL_PREFIX + "key=" + API_KEY + "&cmd=" + command;
    	
    	log.info("BART Holidays URL: " + holidayURL);
    	
    	URL url = new URL(holidayURL);
    	Scanner scan = new Scanner(url.openStream());
    	String holidayOutput = new String();
    	while (scan.hasNext()) {
    		holidayOutput += scan.nextLine();
    	}
    	scan.close();
    	
    	// build a JSON object
    	JSONObject output = new JSONObject(holidayOutput);
    	
    	//get the results
    	JSONObject root = output.getJSONObject("root");
    	
    	JSONArray holidays = root.getJSONArray("holidays");
    	
    	JSONObject list = holidays.getJSONObject(0);
    	
    	JSONArray holidayList = list.getJSONArray("holiday");
    	
    	String speechOutput = "The upcoming " + MAX_HOLIDAYS + " holidays are: ";
    	for (int i=0; i < MAX_HOLIDAYS; i++) {
    		JSONObject o = (JSONObject) holidayList.get(i);
    		if (i == MAX_HOLIDAYS - 1) {
        		speechOutput = speechOutput + "and " + o.getString("name") + " on " + o.getString("date") + ".";
    		} else {
    			speechOutput = speechOutput + o.getString("name") + " on " + o.getString("date") + ", ";
    		}
    	}
    	
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText(speechOutput);

        SimpleCard card = new SimpleCard();
        card.setTitle("Upcoming BART Holidays");
        card.setContent(speechOutput);

        return SpeechletResponse.newTellResponse(outputSpeech, card);
    	
	}
    
	/**
     * Creates a {@code SpeechletResponse} for the HelpIntent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
	private SpeechletResponse getHelpResponse(Intent intent) {
    	String speechOutput =
                "With Bart Helper, you can get"
                        + " information about the Bay Area Rapid Transit system."
                        + " For example, you could say what are the upcoming BART holidays?"
                        + " Now, what would you like to know?";
                        

        String repromptText = "What would you like to know?";

        return newAskResponse(speechOutput, false, repromptText, false);
	}
	
	/**
     * Creates a {@code SpeechletResponse} when there is an error of any kind.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
	private SpeechletResponse getErrorResponse(Intent intent) {
    	
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText("I'm sorry... the BART information system seems to be down right now."
        						+ " Please try again later.");

        return SpeechletResponse.newTellResponse(outputSpeech);
		
	}

	/**
     * Creates a {@code SpeechletResponse} for the CancelIntent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
	private SpeechletResponse getCancelResponse(Intent intent) {
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText("Goodbye");

        return SpeechletResponse.newTellResponse(outputSpeech);
	}

	private SpeechletResponse getStopResponse(Intent intent) {
    	
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText("Goodbye");

        return SpeechletResponse.newTellResponse(outputSpeech);
	}


    /**
     * Function to handle the onLaunch skill behavior.
     * 
     * @return SpeechletResponse object with voice/card response to return to the user
     */
    private SpeechletResponse getWelcomeResponse() {
        String speechOutput = "Welcome to the BART Helper! What would you like to know?";
        // If the user either does not reply to the welcome message or says something that is not
        // understood, they will be prompted again with this text.
        String repromptText =
                "With BART Helper, you can get information about the Bay Area Rapid Transit system."
                        + " For example, you could say what are the upcoming BART holidays?"
                        + " Now, what would you like to know?";

        return newAskResponse(speechOutput, false, repromptText, false);
    }

    
    /**
     * Wrapper for creating the Ask response from the input strings.
     * 
     * @param stringOutput
     *            the output to be spoken
     * @param isOutputSsml
     *            whether the output text is of type SSML
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @param isRepromptSsml
     *            whether the reprompt text is of type SSML
     * @return SpeechletResponse the speechlet response
     */
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

}
