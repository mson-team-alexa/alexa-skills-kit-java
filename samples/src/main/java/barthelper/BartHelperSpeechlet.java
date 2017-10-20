package barthelper;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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
    
    private static final String API_KEY = "MW9S-E7SL-26DU-VV8V";
    
    private static final String STATION_SLOT = "Station";
    
    private static final int MAX_HOLIDAYS = 3;
    
    private static final String HOME_SLOT = "Home";
    
    private static final String HOME_KEY = "HOME";

    
	HashMap<String, String> station_shortcodes = new HashMap<String, String>()
    {{
    		put("12th street oakland city center", "12th");
        put("16th street mission", "16th");
        put("19th street oakland", "19th");
        put("24th street mission", "24th");
        put("ashby", "ashb");
        put("balboa park", "balb");
        put("bay fair", "bayf");
        put("castro valley", "cast");
        put("civic center", "civc");
        put("coliseum", "cols");
        put("colma", "colm");
        put("concord", "conc");
        put("daly city", "daly");
        put("downtown berkeley", "dbrk");
        put("dublin pleasanton", "dubl");
        put("el cerrito del norte", "deln");
        put("del norte", "deln");
        put("el cerrito plaza", "plza");
        put("embarcadero", "embr");
        put("fremont", "frmt");
        put("fruitvale", "ftvl");
        put("glen park", "glen");
        put("hayward", "hayw");
        put("lafayette", "lafy");
        put("lake merritt", "lake");
        put("macarthur", "mcar");
        put("millbrae", "mlbr");
        put("montgomery street", "mont");
        put("north berkeley", "nbrk");
        put("north concord martinez", "ncon");
        put("oakland airport", "oakl");
        put("orinda", "orin");
        put("pittsburg bay point", "pitt");
        put("pleasant hill", "phil");
        put("powell street", "powl");
        put("richmond", "rich");
        put("rockridge", "rock");
        put("san bruno", "sbrn");
        put("san francisco airport", "sfia");
        put("san leandro", "sanl");
        put("south hayward", "shay");
        put("south san francisco", "ssan");
        put("union city", "ucty");
        put("walnut creek", "wcrk");
        put("west dublin pleasanton", "wdub");
        put("west oakland", "woak");
    }};
    
    
    

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
        } else if ("GetTrainTimes".equals(intentName)){
        	try {
				return getBARTTrainTimes(intent);
			} catch (IOException e) {
				log.error("Train Times IO Error");
				e.printStackTrace();
				return getErrorResponse(intent);
			} catch (JSONException e) {
				log.error("Train Times JSON Error");
				e.printStackTrace();
				return getErrorResponse(intent);
			}
        } else if(("setHomeIntent").equals(intentName)) {
        		return setHomeIntent(intent, session);
        } else if(("getTrainTimesFromHomeIntent").equals(intentName)) {
        		try {
					return getTrainTimesFromHome(intent, session);
				} catch (IOException e) {
					log.error("Train Times IO Error");
					e.printStackTrace();
					return getErrorResponse(intent);
				} catch (JSONException e) {
					log.error("Train Times JSON Error");
					e.printStackTrace();
					return getErrorResponse(intent);
				}
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return getHelpResponse(intent);
        } else if ("AMAZON.StopIntent".equals(intentName)) {
            return getStopResponse(intent);
        } else if ("AMAZON.CancelIntent".equals(intentName)) {
            return getCancelResponse(intent);
        } else {
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
    	log.info("HashMap: " + station_shortcodes);
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
	
	private SpeechletResponse setHomeIntent(final Intent intent, final Session session) {
        // Get the slots from the intent.
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from the list of slots.
        Slot homeSlot = slots.get(HOME_SLOT);
        String speechText, repromptText;

        // Check for favorite color and create output to user.
        if (homeSlot != null) {
            // Store the user's favorite color in the Session and create response.
            String home = homeSlot.getValue();
            session.setAttribute(HOME_KEY, home);
            speechText =
                    String.format("Your home station is now set to %s. You can now ask for directions from your home station.", home);
            repromptText =
                    "You can now ask for directions from your home station.";

        } else {
            // Render an error since we don't know what the users favorite color is.
            speechText = "I'm not sure what your home station is. You can tell me your home station "
                    + "by saying, set my home station to Ashby.";
            repromptText =
                    "I'm not sure what your home station is. You can tell me your home station "
                            + "by saying, set my home station to Ashby.";
        }

        return getSpeechletResponse(speechText, repromptText, true);
    }
	
private SpeechletResponse getBARTTrainTimes(Intent intent) throws IOException, JSONException {
    	
	Slot itemSlot = intent.getSlot(STATION_SLOT);
	
	String speechOutput = "";
    if (itemSlot != null && itemSlot.getValue() != null) {
    	
        String stationName = itemSlot.getValue();
        log.info("Station Name: " + stationName);
        String shortcode = station_shortcodes.get(stationName.toLowerCase());
  
        String trainTimesURL = "http://bartjsonapi.elasticbeanstalk.com/api/departures/" + shortcode;
    	
        log.info("BART Train Times URL: " + trainTimesURL);
    	
        URL url = new URL(trainTimesURL);
        Scanner scan = new Scanner(url.openStream());
        String trainTimesOutput = new String();
	    	while (scan.hasNext()) {
	    		trainTimesOutput += scan.nextLine();
	    	}
	    	scan.close();
    	
	    	// build a JSON object
	    	JSONObject output = new JSONObject(trainTimesOutput);
    	
	    	//get the results
	    	JSONArray etd = output.getJSONArray("etd");
	    	
	    
	    for(int i=0; i < etd.length(); i++) {
	    	JSONObject train = etd.getJSONObject(i);
	    	String destination = train.getString("destination");
	    	JSONArray departures = train.getJSONArray("estimate");
	    	JSONObject train_info = departures.getJSONObject(0);
	    	String time_till_departure = train_info.getString("minutes");
	    	
	    	if (time_till_departure == "Leaving") {
	    		train_info = departures.getJSONObject(1);
	    		time_till_departure = train_info.getString("minutes");
	    	}
	    	
	    	String platform = train_info.getString("platform");
	    	
	    speechOutput = speechOutput + " The train going to " + destination + " leaves in " + time_till_departure + " minutes from platform " + platform + ".";
	    	
	    };
	    
    } else {
    	
    	speechOutput = "Sorry, I don't recognize that as a valid station name.";
    	
    	}
    	
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText(speechOutput);

        SimpleCard card = new SimpleCard();
        card.setTitle("Upcoming Train Departures");
        card.setContent(speechOutput);

        return SpeechletResponse.newTellResponse(outputSpeech, card);
    	
	}
	

private SpeechletResponse getTrainTimesFromHome(Intent intent, Session session) throws IOException, JSONException {
	
	
	String homeStation = (String) session.getAttribute(HOME_KEY);
	
	String speechOutput = "";
    if (StringUtils.isNotEmpty(homeStation)) {
    	
        log.info("Home Station Name: " + homeStation);
        String shortcode = station_shortcodes.get(homeStation.toLowerCase());
  
        String trainTimesURL = "http://bartjsonapi.elasticbeanstalk.com/api/departures/" + shortcode;
    	
        log.info("BART Train Times URL: " + trainTimesURL);
    	
        URL url = new URL(trainTimesURL);
        Scanner scan = new Scanner(url.openStream());
        String trainTimesOutput = new String();
	    	while (scan.hasNext()) {
	    		trainTimesOutput += scan.nextLine();
	    	}
	    	scan.close();
    	
	    	// build a JSON object
	    	JSONObject output = new JSONObject(trainTimesOutput);
    	
	    	//get the results
	    	JSONArray etd = output.getJSONArray("etd");
	    	
	    
	    for(int i=0; i < etd.length(); i++) {
	    	JSONObject train = etd.getJSONObject(i);
	    	String destination = train.getString("destination");
	    	JSONArray departures = train.getJSONArray("estimate");
	    	JSONObject train_info = departures.getJSONObject(0);
	    	String time_till_departure = train_info.getString("minutes");
	    	
	    	if (time_till_departure == "Leaving") {
	    		train_info = departures.getJSONObject(1);
	    		time_till_departure = train_info.getString("minutes");
	    	}
	    	
	    	String platform = train_info.getString("platform");
	    	
	    speechOutput = speechOutput + " The train going to " + destination + " leaves in " + time_till_departure + " minutes from platform " + platform + ".";
	    	
	    };
	    
    } else {
    	
    	
    	return setHomeIntent(intent, session);
    	
    	}
    	
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText(speechOutput);

        SimpleCard card = new SimpleCard();
        card.setTitle("Upcoming Train Departures");
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
            //return SpeechletResponse.newTellResponse(speech, card);
            PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
            repromptSpeech.setText("");
	        	Reprompt reprompt = new Reprompt();
	        	reprompt.setOutputSpeech(repromptSpeech);
	        	return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }
    }

}
